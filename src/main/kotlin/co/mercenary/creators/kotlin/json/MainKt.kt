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
import co.mercenary.creators.kotlin.json.base.JSONArray
import co.mercenary.creators.kotlin.json.base.JSONObject
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import com.fasterxml.jackson.core.type.TypeReference
import com.jayway.jsonpath.TypeRef
import java.io.*
import java.nio.file.Path

typealias JSONArray = JSONArray

typealias JSONObject = JSONObject

typealias LINK = java.net.URL

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

fun json() = JSONObject()

fun json(k: String, v: Any?) = JSONObject(k, v)

fun json(args: Map<String, Any?>) = JSONObject(args)

fun json(args: Pair<String, Any?>) = JSONObject(args)

fun json(vararg args: Pair<String, Any?>) = JSONObject(*args)

fun json(args: Iterable<Pair<String, Any?>>) = JSONObject(args)

fun json(args: Sequence<Pair<String, Any?>>) = JSONObject(args)

fun json(data: LINK) = JSONStatic.toJSONObject(data)

fun json(data: File) = JSONStatic.toJSONObject(data)

fun json(data: Path) = JSONStatic.toJSONObject(data)

fun json(data: String) = JSONStatic.toJSONObject(data)

fun json(data: ByteArray) = JSONStatic.toJSONObject(data)

fun json(data: InputStreamSupplier) = JSONStatic.toJSONObject(data)

fun json(data: Reader, done: Boolean = true) = JSONStatic.toJSONObject(data, done)

fun json(data: InputStream, done: Boolean = true) = JSONStatic.toJSONObject(data, done)

fun toJSONArray() = JSONArray()

fun toJSONArray(args: List<Any?>) = JSONArray(args)

fun toJSONArray(vararg args: Any?) = JSONArray(*args)

fun toJSONArray(args: Iterable<Any?>) = JSONArray(args)

fun toJSONArray(args: Sequence<Any?>) = JSONArray(args)

fun toJSONArray(data: LINK) = JSONStatic.toJSONArray(data)

fun toJSONArray(data: File) = JSONStatic.toJSONArray(data)

fun toJSONArray(data: Path) = JSONStatic.toJSONArray(data)

fun toJSONArray(data: String) = JSONStatic.toJSONArray(data)

fun toJSONArray(data: ByteArray) = JSONStatic.toJSONArray(data)

fun toJSONArray(data: InputStreamSupplier) = JSONStatic.toJSONArray(data)

fun toJSONArray(data: Reader, done: Boolean = true) = JSONStatic.toJSONArray(data, done)

fun toJSONArray(data: InputStream, done: Boolean = true) = JSONStatic.toJSONArray(data, done)

fun toJSONString(func: () -> Any, pretty: Boolean = true): String = toJSONString(func.invoke(), pretty)

fun toJSONString(data: Any, pretty: Boolean = true): String = when (data) {
    is JSONAware -> data.toJSONString(pretty)
    else -> JSONStatic.toJSONString(data, pretty)
}

fun isJSONValue(data: Any?) = JSONStatic.canSerializeValue(data)

fun isJSONClass(data: Class<*>?) = JSONStatic.canSerializeClass(data)

inline fun <reified T> isJSONClass() = JSONStatic.canSerializeClass(T::class.java)

inline fun <reified T> toJavaClass(data: T) = JSONStatic.toJavaClass(data, T::class.java)

inline fun <reified T : Any> toDeepCopy(data: T): T = JSONStatic.toDeepCopy(data, T::class.java)

inline fun <reified T : Any> toDataType(data: Any): T = JSONStatic.toDataType(data, object : TypeReference<T>() {})

inline fun <reified T : Any> jsonRead(data: LINK) = JSONStatic.jsonRead(data, object : TypeReference<T>() {})

inline fun <reified T : Any> jsonRead(data: File) = JSONStatic.jsonRead(data, object : TypeReference<T>() {})

inline fun <reified T : Any> jsonRead(data: Path) = JSONStatic.jsonRead(data, object : TypeReference<T>() {})

inline fun <reified T : Any> jsonRead(data: String) = JSONStatic.jsonRead(data, object : TypeReference<T>() {})

inline fun <reified T : Any> jsonRead(data: ByteArray) = JSONStatic.jsonRead(data, object : TypeReference<T>() {})

inline fun <reified T : Any> jsonRead(data: Reader, done: Boolean = true) = JSONStatic.jsonRead(data, object : TypeReference<T>() {}, done)

inline fun <reified T : Any> jsonRead(data: InputStream, done: Boolean = true) = JSONStatic.jsonRead(data, object : TypeReference<T>() {}, done)

inline fun <reified T : Any> jsonRead(data: InputStreamSupplier) = JSONStatic.jsonRead(data, object : TypeReference<T>() {})

inline fun <reified T : Any, A> JSONAccess<A>.asDataTypeOf(look: A): T? = JSONStatic.asDataTypeOf(accessOf(look), object : TypeReference<T>() {})
