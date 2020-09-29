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

import co.mercenary.creators.kotlin.util.CreatorsDsl
import com.jayway.jsonpath.TypeRef
import kotlin.reflect.KClass

interface EvaluationContext {

    @CreatorsDsl
    fun <T : Any> json(): T

    @CreatorsDsl
    fun <T : Any> eval(path: String, type: Class<T>): T

    @CreatorsDsl
    fun <T : Any> eval(path: String, type: KClass<T>): T

    @CreatorsDsl
    fun <T : Any> eval(path: String, type: TypeRef<T>): T

    @CreatorsDsl
    fun <T : Any> read(path: String, type: Class<T>): T?

    @CreatorsDsl
    fun <T : Any> read(path: String, type: KClass<T>): T?

    @CreatorsDsl
    fun <T : Any> read(path: String, type: TypeRef<T>): T?

    @CreatorsDsl
    fun <T : Any> eval(path: CompiledPath, type: Class<T>): T

    @CreatorsDsl
    fun <T : Any> eval(path: CompiledPath, type: KClass<T>): T

    @CreatorsDsl
    fun <T : Any> eval(path: CompiledPath, type: TypeRef<T>): T

    @CreatorsDsl
    fun <T : Any> read(path: CompiledPath, type: Class<T>): T?

    @CreatorsDsl
    fun <T : Any> read(path: CompiledPath, type: KClass<T>): T?

    @CreatorsDsl
    fun <T : Any> read(path: CompiledPath, type: TypeRef<T>): T?

    @CreatorsDsl
    fun delete(path: String): EvaluationContext

    @CreatorsDsl
    fun delete(path: CompiledPath): EvaluationContext

    @CreatorsDsl
    fun rename(path: String, last: String, name: String): EvaluationContext

    @CreatorsDsl
    fun rename(path: CompiledPath, last: String, name: String): EvaluationContext

    @CreatorsDsl
    fun add(path: String, data: Any?): EvaluationContext

    @CreatorsDsl
    fun add(path: CompiledPath, data: Any?): EvaluationContext

    @CreatorsDsl
    fun set(path: String, data: Any?): EvaluationContext

    @CreatorsDsl
    fun set(path: CompiledPath, data: Any?): EvaluationContext

    @CreatorsDsl
    fun put(path: String, name: String, data: Any?): EvaluationContext

    @CreatorsDsl
    fun put(path: CompiledPath, name: String, data: Any?): EvaluationContext

    @CreatorsDsl
    fun map(path: String, func: (Any, EvaluationContext) -> Any): EvaluationContext

    @CreatorsDsl
    fun map(path: CompiledPath, func: (Any, EvaluationContext) -> Any): EvaluationContext

    @CreatorsDsl
    fun deep(): EvaluationContext
}