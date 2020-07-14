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

package co.mercenary.creators.kotlin.json.path

import co.mercenary.creators.kotlin.json.base.*
import co.mercenary.creators.kotlin.util.*
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import com.jayway.jsonpath.*
import com.jayway.jsonpath.Option.SUPPRESS_EXCEPTIONS
import com.jayway.jsonpath.spi.json.JacksonJsonProvider
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider
import java.io.*
import java.net.*
import java.nio.channels.ReadableByteChannel
import java.nio.file.Path
import kotlin.reflect.KClass

@IgnoreForSerialize
object JSONPath {

    private val CACHED = atomicMapOf<String, JSONCompiledPath>()

    private val MAPPER: JSONMapper by lazy {
        JSONMapper(false)
    }

    private val CONFIG: Configuration by lazy {
        Configuration.builder().jsonProvider(JSONJacksonProvider(MAPPER)).mappingProvider(JSONJacksonMappingProvider(MAPPER)).options(SUPPRESS_EXCEPTIONS).build()
    }

    @JvmStatic
    @CreatorsDsl
    private fun lookup(path: String) = cached(path).compiled

    @JvmStatic
    @CreatorsDsl
    private fun lookup(path: CompiledPath) = lookup(path.toPathSpec())

    @JvmStatic
    @CreatorsDsl
    private fun cached(path: String) = CACHED.computeIfAbsent(path) { JSONCompiledPath(it) }

    @JvmStatic
    @CreatorsDsl
    private fun make(data: Any): EvaluationContext {
        return when (data) {
            is URI -> JSONEvaluationContext(data.toInputStream())
            is URL -> JSONEvaluationContext(data.toInputStream())
            is File -> JSONEvaluationContext(data.toInputStream())
            is Path -> JSONEvaluationContext(data.toInputStream())
            is Reader-> JSONEvaluationContext(data.toInputStream())
            is ByteArray -> JSONEvaluationContext(data.toInputStream())
            is InputStreamSupplier -> JSONEvaluationContext(data.toInputStream())
            is ReadableByteChannel -> JSONEvaluationContext(data.toInputStream())
            is InputStream -> JSONEvaluationContext(data)
            is CharSequence -> JSONEvaluationContext(data)
            is DocumentContext -> JSONEvaluationContext(data)
            else -> JSONEvaluationContext(data)
        }
    }

    @JvmStatic
    @CreatorsDsl
    fun path(data: Any) = make(data)

    @JvmStatic
    @CreatorsDsl
    fun compile(path: String): CompiledPath = cached(path)

    internal class JSONJacksonMappingProvider(mapper: JSONMapper) : JacksonMappingProvider(mapper)

    internal class JSONJacksonProvider(mapper: JSONMapper) : JacksonJsonProvider(mapper) {
        override fun createMap() = JSONObject()
        override fun createArray() = JSONArray()
    }

    internal class JSONCompiledPath(internal val compiled: JsonPath) : CompiledPath {

        constructor(path: String) : this(JsonPath.compile(path))

        override fun toString(): String = toPathSpec()
        override fun toPathSpec(): String = compiled.path
        override fun isDefinite(): Boolean = compiled.isDefinite
    }

    internal class JSONEvaluationContext @CreatorsDsl constructor(internal val context: DocumentContext) : EvaluationContext {

        @CreatorsDsl
        constructor(data: Any) : this(JsonPath.parse(data, CONFIG))

        @CreatorsDsl
        constructor(data: InputStream) : this(JsonPath.parse(data, CONFIG))

        @CreatorsDsl
        constructor(data: CharSequence) : this(JsonPath.parse(data.toString(), CONFIG))

        override fun deep() = make(json() as Any)
        override fun add(path: String, data: Any) = make(context.add(lookup(path), data))
        override fun add(path: CompiledPath, data: Any) = make(context.add(lookup(path), data))
        override fun set(path: String, data: Any) = make(context.set(lookup(path), data))
        override fun set(path: CompiledPath, data: Any) = make(context.set(lookup(path), data))
        override fun put(path: String, name: String, data: Any) = make(context.put(lookup(path), name, data))
        override fun put(path: CompiledPath, name: String, data: Any) = make(context.put(lookup(path), name, data))
        override fun delete(path: String) = make(context.delete(lookup(path)))
        override fun delete(path: CompiledPath) = make(context.delete(lookup(path)))
        override fun rename(path: String, last: String, name: String) = make(context.renameKey(lookup(path), last, name))
        override fun rename(path: CompiledPath, last: String, name: String) = make(context.renameKey(lookup(path), last, name))
        override fun <T : Any> eval(path: String, type: Class<T>): T = context.read(lookup(path), type)
        override fun <T : Any> eval(path: String, type: TypeRef<T>): T = context.read(lookup(path), type)
        override fun <T : Any> eval(path: String, type: KClass<T>): T = context.read(lookup(path), type.java)
        override fun <T : Any> read(path: String, type: Class<T>): T? = context.read(lookup(path), type)
        override fun <T : Any> read(path: String, type: TypeRef<T>): T? = context.read(lookup(path), type)
        override fun <T : Any> read(path: String, type: KClass<T>): T? = context.read(lookup(path), type.java)
        override fun <T : Any> eval(path: CompiledPath, type: Class<T>): T = context.read(lookup(path), type)
        override fun <T : Any> eval(path: CompiledPath, type: TypeRef<T>): T = context.read(lookup(path), type)
        override fun <T : Any> eval(path: CompiledPath, type: KClass<T>): T = context.read(lookup(path), type.java)
        override fun <T : Any> read(path: CompiledPath, type: Class<T>): T? = context.read(lookup(path), type)
        override fun <T : Any> read(path: CompiledPath, type: TypeRef<T>): T? = context.read(lookup(path), type)
        override fun <T : Any> read(path: CompiledPath, type: KClass<T>): T? = context.read(lookup(path), type.java)
        override fun map(path: String, func: (Any, EvaluationContext) -> Any) = make(context.map(lookup(path), mapper(func, this)))
        override fun map(path: CompiledPath, func: (Any, EvaluationContext) -> Any) = make(context.map(lookup(path), mapper(func, this)))
        override fun <T : Any> json(): T = context.json()

        companion object {

            @JvmStatic
            @CreatorsDsl
            private fun mapper(mapper: (Any, EvaluationContext) -> Any, context: EvaluationContext) = { data: Any, _: Configuration -> mapper(data, context) } as MapFunction
        }
    }
}