/*
 * Copyright (c) 2020, Mercenary Creators Company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.mercenary.creators.kotlin.json.base

import co.mercenary.creators.kotlin.util.*
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import com.fasterxml.jackson.core.type.TypeReference
import java.io.*
import java.math.*
import java.net.*
import java.nio.channels.ReadableByteChannel
import java.nio.file.Path
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.*

object JSONStatic {

    private val PRETTY: JSONMapper by lazy {
        JSONMapper(true)
    }

    private val NORMAL: JSONMapper by lazy {
        JSONMapper(false)
    }

    private const val IR_MAX = Int.MAX_VALUE

    private const val IR_MIN = Int.MIN_VALUE

    private val IV_MAX = Int.MAX_VALUE.toBigInteger()

    private val IV_MIN = Int.MIN_VALUE.toBigInteger()

    private val LV_MAX = Long.MAX_VALUE.toBigInteger()

    private val LV_MIN = Long.MIN_VALUE.toBigInteger()

    private val DV_MAX = Double.MAX_VALUE.toBigDecimal()

    private val DV_MIN = Double.MAX_VALUE.toBigDecimal().negate()

    private val DI_MAX = Double.MAX_VALUE.toBigDecimal().toBigInteger()

    private val DI_MIN = Double.MAX_VALUE.toBigDecimal().negate().toBigInteger()

    private fun getTypeOf(look: Number): JSONTypeOf = if (isNumber(look)) JSONTypeOf.NUMBER else JSONTypeOf.UNDEFINED

    @JvmStatic
    fun getTypeOf(look: Any?) = when (look) {
        null -> JSONTypeOf.NULL
        is Date -> JSONTypeOf.DATE
        is Number -> getTypeOf(look)
        is String -> JSONTypeOf.STRING
        is Boolean -> JSONTypeOf.BOOLEAN
        isArray(look) -> JSONTypeOf.ARRAY
        isObject(look) -> JSONTypeOf.OBJECT
        isFunction(look) -> JSONTypeOf.FUNCTION
        else -> JSONTypeOf.UNDEFINED
    }

    @JvmStatic
    @AssumptionDsl
    fun isFunction(look: Any?) = when (look) {
        null -> false
        is Function0<*> -> true
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isArray(look: Any?) = when (look) {
        null -> false
        is List<*> -> true
        is Array<*> -> true
        is Sequence<*> -> true
        is Iterable<*> -> true
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isObject(look: Any?) = when (look) {
        null -> false
        is Map<*, *> -> true
        isFunction(look) -> false
        isDataClass(look) -> true
        else -> asObject(look) != null
    }

    @JvmStatic
    @AssumptionDsl
    fun isDataClass(look: Any?) = when (look) {
        null -> false
        else -> look.javaClass.kotlin.isData
    }

    @JvmStatic
    @AssumptionDsl
    fun isString(look: Any?) = when (look) {
        null -> false
        is String -> true
        is CharSequence -> true
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isBoolean(look: Any?) = when (look) {
        null -> false
        is Boolean -> true
        is AtomicBoolean -> true
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isNumber(look: Any?) = when (look) {
        null -> false
        is Number -> isDouble(look)
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isDate(look: Any?) = when (look) {
        null -> false
        is Date -> true
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isNull(look: Any?) = when (look) {
        null -> true
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isLong(look: Any?) = when (look) {
        null -> false
        is Long, Int, Short, Char, Byte -> true
        is BigInteger -> look in LV_MIN..LV_MAX
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isInteger(look: Any?) = when (look) {
        null -> false
        is Int, Short, Char, Byte -> true
        is Long -> look in IR_MIN..IR_MAX
        is BigInteger -> look in IV_MIN..IV_MAX
        else -> false
    }

    @JvmStatic
    @AssumptionDsl
    fun isDouble(look: Any?) = when (look) {
        null -> false
        is Float -> look.isFinite()
        is Double -> look.isFinite()
        is Int, Long, Short, Char, Byte -> true
        is BigDecimal -> look <= DV_MAX && look >= DV_MIN
        is BigInteger -> look <= DI_MAX && look >= DI_MIN
        else -> false
    }

    @JvmStatic
    fun asInteger(look: Any?): Int? = when (look) {
        null -> null
        is Number -> if (isInteger(look)) look.toInt() else null
        else -> null
    }

    @JvmStatic
    fun asLong(look: Any?): Long? = when (look) {
        null -> null
        is Number -> if (isLong(look)) look.toLong() else null
        else -> null
    }

    @JvmStatic
    fun asDate(look: Any?): Date? = when (look) {
        null -> null
        is Date -> look
        else -> null
    }

    @JvmStatic
    fun asString(look: Any?): String? = when (look) {
        null -> null
        is String -> look
        is CharSequence -> look.toString()
        else -> null
    }

    @JvmStatic
    fun asBoolean(look: Any?): Boolean? = when (look) {
        null -> null
        is Boolean -> look
        is AtomicBoolean -> look.toBoolean()
        else -> null
    }

    @JvmStatic
    fun asDouble(look: Any?): Double? = when (look) {
        null -> null
        is Number -> if (isDouble(look)) look.toDouble() else null
        else -> null
    }

    @JvmStatic
    fun asObject(look: Any?): JSONObject? = when (look) {
        null -> null
        is JSONObject -> look
        else -> asDataTypeOf(look, JSONObject::class)
    }

    @JvmStatic
    fun asArray(look: Any?): JSONArray? = when (look) {
        null -> null
        is JSONArray -> look
        is List<*> -> JSONArray(look)
        is Array<*> -> JSONArray(look)
        is Sequence<*> -> JSONArray(look)
        is Iterable<*> -> JSONArray(look)
        else -> asDataTypeOf(look, JSONArray::class)
    }

    @JvmStatic
    fun <T : Any> asDataType(look: Any, type: Class<T>): T? = try {
        toDataType(look, type)
    }
    catch (cause: Throwable) {
        Throwables.thrown(cause)
        null
    }

    @JvmStatic
    fun <T : Any> asDataTypeOf(look: Any?, type: Class<T>): T? = look?.let { asDataType(it, type) }

    @JvmStatic
    fun <T : Any> asDataType(look: Any, type: KClass<T>): T? = try {
        toDataType(look, type)
    }
    catch (cause: Throwable) {
        Throwables.thrown(cause)
        null
    }

    @JvmStatic
    fun <T : Any> asDataTypeOf(look: Any?, type: KClass<T>): T? = look?.let { asDataType(it, type) }

    @JvmStatic
    fun <T : Any> asDataType(look: Any, type: TypeReference<T>): T? = try {
        toDataType(look, type)
    }
    catch (cause: Throwable) {
        Throwables.thrown(cause)
        null
    }

    @JvmStatic
    fun <T : Any> asDataTypeOf(look: Any?, type: TypeReference<T>): T? = look?.let { asDataType(it, type) }

    @JvmStatic
    fun toByteArray(data: Any) = NORMAL.toByteArray(data)

    @JvmStatic
    @AssumptionDsl
    fun canSerializeValue(data: Any?) = NORMAL.canSerializeValue(data)

    @JvmStatic
    @AssumptionDsl
    fun canSerializeClass(type: Class<*>?) = NORMAL.canSerializeClass(type)

    @JvmStatic
    @AssumptionDsl
    fun canSerializeClass(type: KClass<*>?) = NORMAL.canSerializeClass(type)

    @JvmStatic
    @JvmOverloads
    fun toJSONString(data: Any, pretty: Boolean = true) = mapperOf(pretty).toJSONString(data)

    @JvmStatic
    @JvmOverloads
    fun toJSONString(data: () -> Any, pretty: Boolean = true) = mapperOf(pretty).toJSONString(data.invoke())

    @JvmStatic
    @JvmOverloads
    fun <T : JSONAware> toJSONString(data: T, pretty: Boolean = true) = mapperOf(pretty).toJSONString(data)

    @JvmStatic
    fun toJSONArray(data: URI) = jsonReadOf(data, JSONArray::class.java)

    @JvmStatic
    fun toJSONArray(data: URL) = jsonReadOf(data, JSONArray::class.java)

    @JvmStatic
    fun toJSONArray(data: File) = jsonReadOf(data, JSONArray::class.java)

    @JvmStatic
    fun toJSONArray(data: Path) = jsonReadOf(data, JSONArray::class.java)

    @JvmStatic
    fun toJSONArray(data: String) = jsonReadOf(data, JSONArray::class.java)

    @JvmStatic
    fun toJSONArray(data: ByteArray) = jsonReadOf(data, JSONArray::class.java)

    @JvmStatic
    fun toJSONArray(data: InputStreamSupplier) = jsonReadOf(data, JSONArray::class.java)

    @JvmStatic
    @JvmOverloads
    fun toJSONArray(data: Reader, done: Boolean = true) = jsonReadOf(data, JSONArray::class.java, done)

    @JvmStatic
    @JvmOverloads
    fun toJSONArray(data: InputStream, done: Boolean = true) = jsonReadOf(data, JSONArray::class.java, done)

    @JvmStatic
    fun toJSONObject(data: URI) = jsonReadOf(data, JSONObject::class.java)

    @JvmStatic
    fun toJSONObject(data: URL) = jsonReadOf(data, JSONObject::class.java)

    @JvmStatic
    fun toJSONObject(data: File) = jsonReadOf(data, JSONObject::class.java)

    @JvmStatic
    fun toJSONObject(data: Path) = jsonReadOf(data, JSONObject::class.java)

    @JvmStatic
    fun toJSONObject(data: String) = jsonReadOf(data, JSONObject::class.java)

    @JvmStatic
    fun toJSONObject(data: InputStreamSupplier) = jsonReadOf(data, JSONObject::class.java)

    @JvmStatic
    fun toJSONObject(data: ByteArray) = jsonReadOf(data, JSONObject::class.java)

    @JvmStatic
    @JvmOverloads
    fun toJSONObject(data: Reader, done: Boolean = true) = jsonReadOf(data, JSONObject::class.java, done)

    @JvmStatic
    @JvmOverloads
    fun toJSONObject(data: InputStream, done: Boolean = true) = jsonReadOf(data, JSONObject::class.java, done)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: URI, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: URL, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: File, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: Path, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: String, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: ByteArray, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: InputStreamSupplier, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: ReadableByteChannel, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: Reader, type: TypeReference<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: InputStream, type: TypeReference<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: URI, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: URL, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: File, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: Path, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: String, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: ByteArray, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: InputStreamSupplier, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: ReadableByteChannel, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: Reader, type: Class<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: InputStream, type: Class<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: URI, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: URL, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: File, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: Path, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: String, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: ByteArray, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: InputStreamSupplier, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    fun <T : Any> jsonReadOf(data: ReadableByteChannel, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: Reader, type: KClass<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: InputStream, type: KClass<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    fun <T> toDeepCopy(data: T) = NORMAL.toDeepCopy(data)

    @JvmStatic
    fun <T : Any> toDeepCopy(data: T, type: Class<T>) = NORMAL.toDeepCopy(data, type)

    @JvmStatic
    fun <T : Any> toDeepCopy(data: T, type: KClass<T>) = NORMAL.toDeepCopy(data, type)

    @JvmStatic
    fun <T : Any> toDeepCopy(data: T, type: TypeReference<T>) = NORMAL.toDeepCopy(data, type)

    @JvmStatic
    fun <T : Any> toDataType(data: Any, type: Class<T>) = NORMAL.toDataType(data, type)

    @JvmStatic
    fun <T : Any> toDataType(data: Any, type: KClass<T>) = NORMAL.toDataType(data, type)

    @JvmStatic
    fun <T : Any> toDataType(data: Any, type: TypeReference<T>) = NORMAL.toDataType(data, type)

    private fun mapperOf(pretty: Boolean = true) = if (pretty) PRETTY else NORMAL
}
