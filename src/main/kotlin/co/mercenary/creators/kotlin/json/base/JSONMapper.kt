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
import co.mercenary.creators.kotlin.util.time.TimeAndDate
import com.fasterxml.jackson.annotation.JsonIgnoreType
import com.fasterxml.jackson.core.JsonGenerator.Feature.*
import com.fasterxml.jackson.core.JsonParser.Feature.*
import com.fasterxml.jackson.core.json.JsonWriteFeature.ESCAPE_NON_ASCII
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.core.util.*
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import java.io.*
import java.net.*
import java.nio.channels.ReadableByteChannel
import java.nio.file.Path
import kotlin.reflect.KClass

@IgnoreForSerialize
open class JSONMapper : ObjectMapper, StandardInterfaces<JSONMapper> {

    @CreatorsDsl
    @JvmOverloads
    constructor(pretty: Boolean = true) : super() {
        pretty(pretty)
            .registerModules(EXTENDED_MODULES)
            .setDateFormat(JSON_DATE_FORMAT).setTimeZone(DEFAULT_TIMEZONE)
            .enable(ALLOW_COMMENTS).enable(ESCAPE_NON_ASCII.mappedFeature()).enable(WRITE_BIGDECIMAL_AS_PLAIN)
            .disable(AUTO_CLOSE_SOURCE).disable(AUTO_CLOSE_TARGET).disable(FAIL_ON_UNKNOWN_PROPERTIES)
    }

    @CreatorsDsl
    protected constructor(parent: JSONMapper) : super(parent)

    @CreatorsDsl
    override fun copy() = copyOf()

    @CreatorsDsl
    override fun clone() = copyOf()

    @CreatorsDsl
    override fun copyOf() = JSONMapper(this)

    @CreatorsDsl
    override fun canSerialize(type: Class<*>?): Boolean {
        return when {
            type == null -> false
            type.isAnnotationPresent(JsonIgnoreType::class.java) -> {
                type.getAnnotation(JsonIgnoreType::class.java).value.isNotTrue()
            }
            type.isAnnotationPresent(IgnoreForSerialize::class.java) -> {
                type.getAnnotation(IgnoreForSerialize::class.java).value.isNotTrue()
            }
            else -> super.canSerialize(type)
        }
    }

    override fun hashCode() = super.hashCode()

    override fun equals(other: Any?) = when (other) {
        is JSONMapper -> this === other || super.equals(other)
        else -> false
    }

    @CreatorsDsl
    override fun toMapNames() = dictOf("type" to nameOf())

    @CreatorsDsl
    private fun pretty(pretty: Boolean): ObjectMapper = if (pretty) setDefaultPrettyPrinter(TO_PRETTY_PRINTS).enable(INDENT_OUTPUT) else disable(INDENT_OUTPUT)

    @CreatorsDsl
    fun canSerializeClass(type: Class<*>?) = canSerialize(type)

    @CreatorsDsl
    fun canSerializeClass(type: KClass<*>?) = if (null == type) false else canSerializeClass(type.java)

    @CreatorsDsl
    fun canSerializeValue(value: Any?) = if (null == value) false else canSerializeClass(value.javaClass)

    @CreatorsDsl
    fun toByteArray(data: Any): ByteArray = writeValueAsBytes(data)

    @CreatorsDsl
    fun toJSONString(value: Any): String = writeValueAsString(value)

    @CreatorsDsl
    fun <T : Any> toDataType(value: Any, type: Class<T>): T = convertValue(value, type)

    @CreatorsDsl
    fun <T : Any> toDataType(value: Any, type: KClass<T>): T = convertValue(value, type.java)

    @CreatorsDsl
    fun <T : Any> toDataType(value: Any, type: TypeReference<T>): T = convertValue(value, type)

    @CreatorsDsl
    fun <T> toDeepCopy(value: T): T = readerFor((value as Any).javaClass).readValue(toByteArray(value))

    @CreatorsDsl
    fun <T : Any> toDeepCopy(value: T, type: Class<T>): T = readerFor(type).readValue(toByteArray(value))

    @CreatorsDsl
    fun <T : Any> toDeepCopy(value: T, type: KClass<T>): T = readerFor(type.java).readValue(toByteArray(value))

    @CreatorsDsl
    fun <T : Any> toDeepCopy(value: T, type: TypeReference<T>): T = readerFor(type).readValue(toByteArray(value))

    fun <T : Any> jsonRead(value: URI, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: URL, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: String, type: TypeReference<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: ByteArray, type: TypeReference<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: File, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: Path, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: InputStreamSupplier, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: ReadableByteChannel, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    @JvmOverloads
    fun <T : Any> jsonRead(value: Reader, type: TypeReference<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    @JvmOverloads
    fun <T : Any> jsonRead(value: InputStream, type: TypeReference<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    fun <T : Any> jsonRead(value: URI, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: URL, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: String, type: Class<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: ByteArray, type: Class<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: File, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: Path, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: InputStreamSupplier, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: ReadableByteChannel, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    @JvmOverloads
    fun <T : Any> jsonRead(value: Reader, type: Class<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    @JvmOverloads
    fun <T : Any> jsonRead(value: InputStream, type: Class<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    fun <T : Any> jsonRead(value: URI, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: URL, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: String, type: KClass<T>): T = readerFor(type.java).readValue(value)

    fun <T : Any> jsonRead(value: ByteArray, type: KClass<T>): T = readerFor(type.java).readValue(value)

    fun <T : Any> jsonRead(value: File, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: Path, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: InputStreamSupplier, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: ReadableByteChannel, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    @JvmOverloads
    fun <T : Any> jsonRead(value: Reader, type: KClass<T>, done: Boolean = true): T = if (done) value.use { readerFor(type.java).readValue<T>(it) } else readerFor(type.java).readValue<T>(value)

    @JvmOverloads
    fun <T : Any> jsonRead(value: InputStream, type: KClass<T>, done: Boolean = true): T = if (done) value.use { readerFor(type.java).readValue<T>(it) } else readerFor(type.java).readValue<T>(value)


    companion object {
        private const val serialVersionUID = 2L

        @CreatorsDsl
        private val EXTENDED_MODULES = findModules()

        @CreatorsDsl
        private val DEFAULT_TIMEZONE = TimeAndDate.getDefaultTimeZone()

        @CreatorsDsl
        private val JSON_DATE_FORMAT = TimeAndDate.getDefaultDateFormat()

        @CreatorsDsl
        private val TO_INDENT_PRINTS = DefaultIndenter().withIndent("    ")

        @CreatorsDsl
        private val TO_PRETTY_PRINTS = DefaultPrettyPrinter().withArrayIndenter(TO_INDENT_PRINTS).withObjectIndenter(TO_INDENT_PRINTS)
    }


}