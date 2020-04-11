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

@file:kotlin.jvm.JvmName("MainKt")

package co.mercenary.creators.kotlin.json

import co.mercenary.creators.kotlin.json.base.*
import co.mercenary.creators.kotlin.util.AssumptionDsl
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import com.fasterxml.jackson.core.type.TypeReference
import com.jayway.jsonpath.TypeRef
import java.io.*
import java.net.*
import java.nio.channels.ReadableByteChannel
import java.nio.file.Path
import kotlin.reflect.KClass

typealias json = co.mercenary.creators.kotlin.json.base.JSONObject

typealias JSONArray = co.mercenary.creators.kotlin.json.base.JSONArray

typealias JSONObject = co.mercenary.creators.kotlin.json.base.JSONObject

typealias JSONPath = co.mercenary.creators.kotlin.json.path.JSONPath

typealias CompiledPath = co.mercenary.creators.kotlin.json.path.CompiledPath

typealias EvaluationContext = co.mercenary.creators.kotlin.json.path.EvaluationContext

typealias TypicodePostData = co.mercenary.creators.kotlin.json.util.typicode.TypicodePostData

typealias TypicodeTodoData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeTodoData

typealias TypicodeUserData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeUserData

typealias TypicodePhotoData = co.mercenary.creators.kotlin.json.util.typicode.TypicodePhotoData

typealias TypicodeAlbumData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeAlbumData

typealias TypicodeCommentData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeCommentData

inline fun <reified T : Any> EvaluationContext.eval(path: String): T = eval(path, object : TypeRef<T>() {})

inline fun <reified T : Any> EvaluationContext.eval(path: CompiledPath): T = eval(path, object : TypeRef<T>() {})

inline fun <reified T : Any> EvaluationContext.read(path: String): T? = read(path, object : TypeRef<T>() {})

inline fun <reified T : Any> EvaluationContext.read(path: CompiledPath): T? = read(path, object : TypeRef<T>() {})

fun <T : Any> T.toJSONPretty(): String = when (this) {
    is JSONAware -> toJSONString(true)
    else -> JSONStatic.toJSONString(this, true)
}

@AssumptionDsl
fun isJSONValue(data: Any?) = JSONStatic.canSerializeValue(data)

@AssumptionDsl
fun isJSONClass(data: Class<*>?) = JSONStatic.canSerializeClass(data)

@AssumptionDsl
fun isJSONClass(data: KClass<*>?) = JSONStatic.canSerializeClass(data)

inline fun <reified T : Any> toDeepCopy(data: T): T = JSONStatic.toDeepCopy(data, T::class.java)

inline fun <reified T : Any, A> JSONAccess<A>.asDataTypeOf(look: A): T? = JSONStatic.asDataTypeOf(accessOf(look), object : TypeReference<T>() {})

inline fun <reified T : Any> Any.toDataType(): T = JSONStatic.toDataType(this, object : TypeReference<T>() {})

inline fun <reified T : Any> URI.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> URL.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> File.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> Path.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> String.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> ByteArray.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> InputStreamSupplier.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> ReadableByteChannel.jsonReadOf(): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {})

inline fun <reified T : Any> Reader.jsonReadOf(done: Boolean = true): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {}, done)

inline fun <reified T : Any> InputStream.jsonReadOf(done: Boolean = true): T = JSONStatic.jsonReadOf(this, object : TypeReference<T>() {}, done)

