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

import co.mercenary.creators.kotlin.json.*
import co.mercenary.creators.kotlin.util.*
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import com.fasterxml.jackson.core.type.TypeReference
import java.io.*
import java.math.*
import java.net.*
import java.nio.channels.ReadableByteChannel
import java.nio.file.Path
import java.util.*
import java.util.concurrent.atomic.*
import kotlin.reflect.KClass

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

    @JvmStatic
    @CreatorsDsl
    private fun getTypeOf(look: Number): JSONTypeOf = if (isNumber(look)) JSONTypeOf.NUMBER else JSONTypeOf.UNDEFINED

    @JvmStatic
    @CreatorsDsl
    fun getTypeOf(look: Any?) = when (look) {
        null -> JSONTypeOf.NULL
        is Date -> JSONTypeOf.DATE
        is Number -> getTypeOf(look)
        is String -> JSONTypeOf.STRING
        is CharSequence -> JSONTypeOf.STRING
        is Boolean -> JSONTypeOf.BOOLEAN
        is AtomicBoolean -> JSONTypeOf.BOOLEAN
        isArray(look) -> JSONTypeOf.ARRAY
        isObject(look) -> JSONTypeOf.OBJECT
        isFunction(look) -> JSONTypeOf.FUNCTION
        else -> JSONTypeOf.UNDEFINED
    }

    @JvmStatic
    @CreatorsDsl
    fun isFunction(look: Any?) = when (look) {
        null -> false
        is Function0<*> -> true
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isArray(look: Any?) = when (look) {
        null -> false
        is List<*> -> true
        is Array<*> -> true
        is Sequence<*> -> true
        is Iterable<*> -> true
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isObject(look: Any?) = when (look) {
        null -> false
        is Map<*, *> -> true
        isFunction(look) -> false
        isDataClass(look) -> true
        else -> asObject(look) != null
    }

    @JvmStatic
    @CreatorsDsl
    fun isDataClass(look: Any?) = when (look) {
        null -> false
        else -> look.javaClass.kotlin.isData
    }

    @JvmStatic
    @CreatorsDsl
    fun isString(look: Any?) = when (look) {
        null -> false
        is String -> true
        is CharSequence -> true
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isBoolean(look: Any?) = when (look) {
        null -> false
        is Boolean -> true
        is AtomicBoolean -> true
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isNumber(look: Any?) = when (look) {
        null -> false
        is Number -> isDouble(look)
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isDate(look: Any?) = when (look) {
        null -> false
        is Date -> true
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isNull(look: Any?) = when (look) {
        null -> true
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isLong(look: Any?) = when (look) {
        null -> false
        is Long, Int, Short, Char, Byte -> true
        is AtomicLong -> true
        is AtomicInteger -> true
        is BigInteger -> look in LV_MIN..LV_MAX
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isInteger(look: Any?) = when (look) {
        null -> false
        is Int, Short, Char, Byte -> true
        is AtomicInteger -> true
        is Long -> look in IR_MIN..IR_MAX
        is BigInteger -> look in IV_MIN..IV_MAX
        is AtomicLong -> look.toBigInteger() in IV_MIN..IV_MAX
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun isDouble(look: Any?) = when (look) {
        null -> false
        is Float -> look.isValid()
        is Double -> look.isValid()
        is Int, Long, Short, Char, Byte -> true
        is BigDecimal -> look <= DV_MAX && look >= DV_MIN
        is BigInteger -> look <= DI_MAX && look >= DI_MIN
        is AtomicLong -> true
        is AtomicInteger -> true
        else -> false
    }

    @JvmStatic
    @CreatorsDsl
    fun asInteger(look: Any?): Int? = when (look) {
        null -> null
        is Number -> if (isInteger(look)) look.toInt() else null
        else -> null
    }

    @JvmStatic
    @CreatorsDsl
    fun asLong(look: Any?): Long? = when (look) {
        null -> null
        is Number -> if (isLong(look)) look.toLong() else null
        else -> null
    }

    @JvmStatic
    @CreatorsDsl
    fun asDate(look: Any?): Date? = when (look) {
        null -> null
        is Date -> look
        else -> null
    }

    @JvmStatic
    @CreatorsDsl
    fun asString(look: Any?): String? = when (look) {
        null -> null
        is String -> look
        is CharSequence -> look.toString()
        else -> null
    }

    @JvmStatic
    @CreatorsDsl
    fun asBoolean(look: Any?): Boolean? = when (look) {
        null -> null
        is Boolean -> look.toBoolean()
        is AtomicBoolean -> look.toBoolean()
        else -> null
    }

    @JvmStatic
    @CreatorsDsl
    fun asDouble(look: Any?): Double? = when (look) {
        null -> null
        is Number -> if (isDouble(look)) look.toDouble() else null
        else -> null
    }

    @JvmStatic
    @CreatorsDsl
    fun asObject(look: Any?): JSONObject? = when (look) {
        null -> null
        is JSONObject -> look
        else -> asDataTypeOf(look, JSONObject::class)
    }

    @JvmStatic
    @CreatorsDsl
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
    @CreatorsDsl
    fun <T : Any> asDataType(look: Any, type: Class<T>): T? = try {
        toDataType(look, type)
    }
    catch (cause: Throwable) {
        Throwables.thrown(cause)
        null
    }

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> asDataTypeOf(look: Any?, type: Class<T>): T? = look?.let { asDataType(it, type) }

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> asDataType(look: Any, type: KClass<T>): T? = try {
        toDataType(look, type)
    }
    catch (cause: Throwable) {
        Throwables.thrown(cause)
        null
    }

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> asDataTypeOf(look: Any?, type: KClass<T>): T? = look?.let { asDataType(it, type) }

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> asDataType(look: Any, type: TypeReference<T>): T? = try {
        toDataType(look, type)
    }
    catch (cause: Throwable) {
        Throwables.thrown(cause)
        null
    }

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> asDataTypeOf(look: Any?, type: TypeReference<T>): T? = look?.let { asDataType(it, type) }

    @JvmStatic
    @CreatorsDsl
    fun toByteArray(data: Any) = NORMAL.toByteArray(data)

    @JvmStatic
    @CreatorsDsl
    fun canSerializeValue(data: Any?) = NORMAL.canSerializeValue(data)

    @JvmStatic
    @CreatorsDsl
    fun canSerializeClass(type: Class<*>?) = NORMAL.canSerializeClass(type)

    @JvmStatic
    @CreatorsDsl
    fun canSerializeClass(type: KClass<*>?) = NORMAL.canSerializeClass(type)

    @JvmStatic
    @CreatorsDsl
    @JvmOverloads
    fun toJSONString(data: Any, pretty: Boolean = true) = mapperOf(pretty).toJSONString(data)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: URI, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: URL, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: File, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: Path, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: String, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: ByteArray, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: InputStreamSupplier, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: ReadableByteChannel, type: TypeReference<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: Reader, type: TypeReference<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @CreatorsDsl
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: InputStream, type: TypeReference<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: URI, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: URL, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: File, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: Path, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: String, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: ByteArray, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: InputStreamSupplier, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: ReadableByteChannel, type: Class<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: Reader, type: Class<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @CreatorsDsl
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: InputStream, type: Class<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: URI, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: URL, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: File, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: Path, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: String, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: ByteArray, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: InputStreamSupplier, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> jsonReadOf(data: ReadableByteChannel, type: KClass<T>) = NORMAL.jsonRead(data, type)

    @JvmStatic
    @CreatorsDsl
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: Reader, type: KClass<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @CreatorsDsl
    @JvmOverloads
    fun <T : Any> jsonReadOf(data: InputStream, type: KClass<T>, done: Boolean = true) = NORMAL.jsonRead(data, type, done)

    @JvmStatic
    @CreatorsDsl
    fun <T> toDeepCopy(data: T) = NORMAL.toDeepCopy(data)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> toDeepCopy(data: T, type: Class<T>) = NORMAL.toDeepCopy(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> toDeepCopy(data: T, type: KClass<T>) = NORMAL.toDeepCopy(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> toDeepCopy(data: T, type: TypeReference<T>) = NORMAL.toDeepCopy(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> toDataType(data: Any, type: Class<T>) = NORMAL.toDataType(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> toDataType(data: Any, type: KClass<T>) = NORMAL.toDataType(data, type)

    @JvmStatic
    @CreatorsDsl
    fun <T : Any> toDataType(data: Any, type: TypeReference<T>) = NORMAL.toDataType(data, type)

    @JvmStatic
    @CreatorsDsl
    private fun mapperOf(pretty: Boolean = true) = if (pretty) PRETTY else NORMAL
}
