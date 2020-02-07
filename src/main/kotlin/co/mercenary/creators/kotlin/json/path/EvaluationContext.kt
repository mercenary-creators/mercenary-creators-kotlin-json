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

import com.jayway.jsonpath.TypeRef
import kotlin.reflect.KClass

interface EvaluationContext {
    fun <T : Any> json(): T
    fun <T : Any> eval(path: String, type: Class<T>): T
    fun <T : Any> eval(path: String, type: KClass<T>): T
    fun <T : Any> eval(path: String, type: TypeRef<T>): T
    fun <T : Any> read(path: String, type: Class<T>): T?
    fun <T : Any> read(path: String, type: KClass<T>): T?
    fun <T : Any> read(path: String, type: TypeRef<T>): T?
    fun <T : Any> eval(path: CompiledPath, type: Class<T>): T
    fun <T : Any> eval(path: CompiledPath, type: KClass<T>): T
    fun <T : Any> eval(path: CompiledPath, type: TypeRef<T>): T
    fun <T : Any> read(path: CompiledPath, type: Class<T>): T?
    fun <T : Any> read(path: CompiledPath, type: KClass<T>): T?
    fun <T : Any> read(path: CompiledPath, type: TypeRef<T>): T?
    fun delete(path: String): EvaluationContext
    fun delete(path: CompiledPath): EvaluationContext
    fun rename(path: String, last: String, name: String): EvaluationContext
    fun rename(path: CompiledPath, last: String, name: String): EvaluationContext
    fun add(path: String, data: Any): EvaluationContext
    fun add(path: CompiledPath, data: Any): EvaluationContext
    fun set(path: String, data: Any): EvaluationContext
    fun set(path: CompiledPath, data: Any): EvaluationContext
    fun put(path: String, name: String, data: Any): EvaluationContext
    fun put(path: CompiledPath, name: String, data: Any): EvaluationContext
    fun map(path: String, func: (Any, EvaluationContext) -> Any): EvaluationContext
    fun map(path: CompiledPath, func: (Any, EvaluationContext) -> Any): EvaluationContext
    fun deep(): EvaluationContext
}