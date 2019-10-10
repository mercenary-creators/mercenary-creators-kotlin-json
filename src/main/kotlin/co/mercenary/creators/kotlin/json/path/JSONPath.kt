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

package co.mercenary.creators.kotlin.json.path

import co.mercenary.creators.kotlin.json.LINK
import co.mercenary.creators.kotlin.json.base.*
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import co.mercenary.creators.kotlin.util.toInputStream
import com.jayway.jsonpath.*
import com.jayway.jsonpath.Option.SUPPRESS_EXCEPTIONS
import com.jayway.jsonpath.spi.json.JacksonJsonProvider
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider
import java.io.*
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

object JSONPath {

    private val CACHED: ConcurrentHashMap<String, JSONCompiledPath> by lazy {
        ConcurrentHashMap<String, JSONCompiledPath>()
    }

    private val MAPPER: JSONMapper by lazy {
        JSONMapper(false)
    }

    private val CONFIG: Configuration by lazy {
        Configuration.builder().jsonProvider(JSONJacksonProvider(MAPPER)).mappingProvider(JSONJacksonMappingProvider(MAPPER)).options(SUPPRESS_EXCEPTIONS).build()
    }

    internal fun lookup(path: String) = cached(path).compiled

    internal fun lookup(path: CompiledPath) = lookup(path.toPathSpec())

    internal fun cached(path: String) = CACHED.computeIfAbsent(path) { JSONCompiledPath(it) }

    @JvmStatic
    fun path(data: Any) = JSONEvaluationContext(JsonPath.parse(data, CONFIG)).context()

    @JvmStatic
    fun path(data: LINK) = JSONEvaluationContext(JsonPath.parse(data, CONFIG)).context()

    @JvmStatic
    fun path(data: File) = JSONEvaluationContext(JsonPath.parse(data.toInputStream(), CONFIG)).context()

    @JvmStatic
    fun path(data: Path) = JSONEvaluationContext(JsonPath.parse(data.toInputStream(), CONFIG)).context()

    @JvmStatic
    fun path(data: String) = JSONEvaluationContext(JsonPath.parse(data, CONFIG)).context()

    @JvmStatic
    fun path(data: ByteArray) = JSONEvaluationContext(JsonPath.parse(data.toInputStream(), CONFIG)).context()

    @JvmStatic
    fun path(data: InputStream) = JSONEvaluationContext(JsonPath.parse(data, CONFIG)).context()

    @JvmStatic
    fun path(data: InputStreamSupplier) = JSONEvaluationContext(JsonPath.parse(data.toInputStream(), CONFIG)).context()

    @JvmStatic
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

    internal class JSONEvaluationContext(internal val context: DocumentContext) : EvaluationContext {
        internal fun context(): EvaluationContext = this
        override fun deep() = path(json() as Any)
        override fun add(path: String, data: Any) = JSONEvaluationContext(context.add(lookup(path), data)).context()
        override fun add(path: CompiledPath, data: Any) = JSONEvaluationContext(context.add(lookup(path), data)).context()
        override fun set(path: String, data: Any) = JSONEvaluationContext(context.set(lookup(path), data)).context()
        override fun set(path: CompiledPath, data: Any) = JSONEvaluationContext(context.set(lookup(path), data)).context()
        override fun put(path: String, name: String, data: Any) = JSONEvaluationContext(context.put(lookup(path), name, data)).context()
        override fun put(path: CompiledPath, name: String, data: Any) = JSONEvaluationContext(context.put(lookup(path), name, data)).context()
        override fun delete(path: String) = JSONEvaluationContext(context.delete(lookup(path))).context()
        override fun delete(path: CompiledPath) = JSONEvaluationContext(context.delete(lookup(path))).context()
        override fun rename(path: String, last: String, name: String) = JSONEvaluationContext(context.renameKey(lookup(path), last, name)).context()
        override fun rename(path: CompiledPath, last: String, name: String) = JSONEvaluationContext(context.renameKey(lookup(path), last, name)).context()
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
        override fun map(path: String, func: (Any, EvaluationContext) -> Any) = JSONEvaluationContext(context.map(lookup(path), mapper(func, context()))).context()
        override fun map(path: CompiledPath, func: (Any, EvaluationContext) -> Any) = JSONEvaluationContext(context.map(lookup(path), mapper(func, context()))).context()
        override fun <T : Any> json(): T = context.json()

        companion object {
            @JvmStatic
            private fun mapper(mapper: (Any, EvaluationContext) -> Any, context: EvaluationContext) = { data: Any, _: Configuration -> mapper(data, context) } as MapFunction
        }
    }
}