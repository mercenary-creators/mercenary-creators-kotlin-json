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

import co.mercenary.creators.kotlin.util.CreatorsDsl
import co.mercenary.creators.kotlin.util.io.InputStreamSupplier
import com.fasterxml.jackson.core.type.TypeReference
import com.jayway.jsonpath.TypeRef
import java.io.*
import java.net.*
import java.nio.channels.ReadableByteChannel
import java.nio.file.Path

typealias json = co.mercenary.creators.kotlin.json.base.JSONObject

typealias JSONArray = co.mercenary.creators.kotlin.json.base.JSONArray

typealias JSONObject = co.mercenary.creators.kotlin.json.base.JSONObject

typealias JSONStatic = co.mercenary.creators.kotlin.json.base.JSONStatic

typealias JSONPath = co.mercenary.creators.kotlin.json.path.JSONPath

typealias CompiledPath = co.mercenary.creators.kotlin.json.path.CompiledPath

typealias EvaluationContext = co.mercenary.creators.kotlin.json.path.EvaluationContext

typealias TypicodePostData = co.mercenary.creators.kotlin.json.util.typicode.TypicodePostData

typealias TypicodeTodoData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeTodoData

typealias TypicodeUserData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeUserData

typealias TypicodePhotoData = co.mercenary.creators.kotlin.json.util.typicode.TypicodePhotoData

typealias TypicodeAlbumData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeAlbumData

typealias TypicodeCommentData = co.mercenary.creators.kotlin.json.util.typicode.TypicodeCommentData

@CreatorsDsl
inline fun <reified T : Any> EvaluationContext.eval(path: String): T = eval(path, object : TypeRef<T>() {})

@CreatorsDsl
inline fun <reified T : Any> EvaluationContext.eval(path: CompiledPath): T = eval(path, object : TypeRef<T>() {})

@CreatorsDsl
inline fun <reified T : Any> EvaluationContext.read(path: String): T? = read(path, object : TypeRef<T>() {})

@CreatorsDsl
inline fun <reified T : Any> EvaluationContext.read(path: CompiledPath): T? = read(path, object : TypeRef<T>() {})

@CreatorsDsl
inline fun <reified T : Any> typeReferenceOf(): TypeReference<T> = object : TypeReference<T>() {}

@CreatorsDsl
inline fun <reified T : Any> deepOf(data: T): T = JSONStatic.toDeepCopy(data, T::class.java)

@CreatorsDsl
inline fun <reified T : JSONAware> T.deepOf(): T = JSONStatic.toDeepCopy(this, T::class.java)
@CreatorsDsl
inline fun <reified T : Any, A> JSONAccess<A>.asDataTypeOf(look: A): T? = JSONStatic.asDataTypeOf(accessOf(look), typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> Any.toDataType(): T = JSONStatic.toDataType(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> URI.readOf(): T = JSONStatic.jsonReadOf(this,typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> URL.readOf(): T = JSONStatic.jsonReadOf(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> File.readOf(): T = JSONStatic.jsonReadOf(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> Path.readOf(): T = JSONStatic.jsonReadOf(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> String.readOf(): T = JSONStatic.jsonReadOf(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> ByteArray.readOf(): T = JSONStatic.jsonReadOf(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> InputStreamSupplier.readOf(): T = JSONStatic.jsonReadOf(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> ReadableByteChannel.readOf(): T = JSONStatic.jsonReadOf(this, typeReferenceOf())
@CreatorsDsl
inline fun <reified T : Any> Reader.readOf(done: Boolean = true): T = JSONStatic.jsonReadOf(this, typeReferenceOf(), done)
@CreatorsDsl
inline fun <reified T : Any> InputStream.readOf(done: Boolean = true): T = JSONStatic.jsonReadOf(this, typeReferenceOf(), done)

