/*
 * Copyright (c) 2019, Mercenary Creators Company. All rights reserved.
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

package co.mercenary.creators.kotlin.json

import co.mercenary.creators.kotlin.json.module.MercenaryKotlinModule
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import co.mercenary.creators.kotlin.util.toInputStream
import com.fasterxml.jackson.core.JsonGenerator.Feature.*
import com.fasterxml.jackson.core.JsonParser.Feature.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.core.util.*
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import java.io.*
import java.net.URL
import java.nio.channels.ReadableByteChannel
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

open class JSONMapper : ObjectMapper {

    constructor(pretty: Boolean = true) : super() {
        pretty(pretty)
            .registerModules(EXTENDED_MODULES)
            .setDateFormat(JSON_DATE_FORMAT).setTimeZone(DEFAULT_TIMEZONE)
            .enable(ALLOW_COMMENTS).enable(ESCAPE_NON_ASCII).enable(WRITE_BIGDECIMAL_AS_PLAIN)
            .disable(AUTO_CLOSE_SOURCE).disable(AUTO_CLOSE_TARGET).disable(FAIL_ON_UNKNOWN_PROPERTIES)
    }

    protected constructor(parent: JSONMapper) : super(parent)

    override fun copy() = JSONMapper(this)

    override fun canSerialize(type: Class<*>?) = if (null == type) false else super.canSerialize(type)

    fun pretty(pretty: Boolean): ObjectMapper = if (pretty) setDefaultPrettyPrinter(TO_PRETTY_PRINTS).enable(INDENT_OUTPUT) else disable(INDENT_OUTPUT)

    fun canSerializeClass(type: Class<*>?) = canSerialize(type)

    fun canSerializeValue(value: Any?) = if (null == value) false else canSerialize(value.javaClass)

    fun toByteArray(data: Any): ByteArray = writeValueAsBytes(data)

    fun toJSONString(value: Any): String = writeValueAsString(value)

    fun <T : Any> toDataType(value: Any, type: Class<T>): T = convertValue(value, type)

    fun <T : Any> toDataType(value: Any, type: KClass<T>): T = convertValue(value, type.java)

    fun <T : Any> toDataType(value: Any, type: TypeReference<T>): T = convertValue(value, type)

    fun <T> toDeepCopy(value: T): T = readerFor((value as Any).javaClass).readValue(writeValueAsBytes(value))

    fun <T : Any> toDeepCopy(value: T, type: Class<T>): T = readerFor(type).readValue(writeValueAsBytes(value))

    fun <T : Any> toDeepCopy(value: T, type: KClass<T>): T = readerFor(type.java).readValue(writeValueAsBytes(value))

    fun <T : Any> toDeepCopy(value: T, type: TypeReference<T>): T = readerFor(type).readValue(writeValueAsBytes(value))

    fun <T : Any> jsonRead(value: URL, type: TypeReference<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: String, type: TypeReference<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: ByteArray, type: TypeReference<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: File, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: Path, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: InputStreamSupplier, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: ReadableByteChannel, type: TypeReference<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: Reader, type: TypeReference<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    fun <T : Any> jsonRead(value: InputStream, type: TypeReference<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    fun <T : Any> jsonRead(value: URL, type: Class<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: String, type: Class<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: ByteArray, type: Class<T>): T = readerFor(type).readValue(value)

    fun <T : Any> jsonRead(value: File, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: Path, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: InputStreamSupplier, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: ReadableByteChannel, type: Class<T>): T = value.toInputStream().use { readerFor(type).readValue(it) }

    fun <T : Any> jsonRead(value: Reader, type: Class<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    fun <T : Any> jsonRead(value: InputStream, type: Class<T>, done: Boolean = true): T = if (done) value.use { readerFor(type).readValue<T>(it) } else readerFor(type).readValue<T>(value)

    fun <T : Any> jsonRead(value: URL, type: KClass<T>): T = readerFor(type.java).readValue(value)

    fun <T : Any> jsonRead(value: String, type: KClass<T>): T = readerFor(type.java).readValue(value)

    fun <T : Any> jsonRead(value: ByteArray, type: KClass<T>): T = readerFor(type.java).readValue(value)

    fun <T : Any> jsonRead(value: File, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: Path, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: InputStreamSupplier, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: ReadableByteChannel, type: KClass<T>): T = value.toInputStream().use { readerFor(type.java).readValue(it) }

    fun <T : Any> jsonRead(value: Reader, type: KClass<T>, done: Boolean = true): T = if (done) value.use { readerFor(type.java).readValue<T>(it) } else readerFor(type.java).readValue<T>(value)

    fun <T : Any> jsonRead(value: InputStream, type: KClass<T>, done: Boolean = true): T = if (done) value.use { readerFor(type.java).readValue<T>(it) } else readerFor(type.java).readValue<T>(value)

    companion object {
        private const val serialVersionUID = 2L
        private val DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC")
        private val TO_INDENT_PRINTS = DefaultIndenter().withIndent(" ".repeat(4))
        private val JSON_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS z")
        private val TO_PRETTY_PRINTS = DefaultPrettyPrinter().withArrayIndenter(TO_INDENT_PRINTS).withObjectIndenter(TO_INDENT_PRINTS)
        private val EXTENDED_MODULES = listOf(Jdk8Module(), ParameterNamesModule(), JavaTimeModule(), KotlinModule(), MercenaryKotlinModule(), JodaModule())
    }
}