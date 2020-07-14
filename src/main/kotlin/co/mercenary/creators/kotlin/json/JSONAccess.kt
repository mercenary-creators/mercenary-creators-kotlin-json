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

package co.mercenary.creators.kotlin.json

import co.mercenary.creators.kotlin.json.base.JSONArray
import co.mercenary.creators.kotlin.json.base.JSONObject
import co.mercenary.creators.kotlin.util.*
import java.util.*

interface JSONAccess<A> {

    @CreatorsDsl
    val size: Int

    @CreatorsDsl
    @IgnoreForSerialize
    fun isEmpty(): Boolean

    @CreatorsDsl
    @IgnoreForSerialize
    fun isNotEmpty(): Boolean = isEmpty().isNotTrue()

    @CreatorsDsl
    fun sizeOf(): Int

    @CreatorsDsl
    fun findOf(look: A): Any?

    @CreatorsDsl
    infix fun isDefined(look: A): Boolean

    @CreatorsDsl
    infix fun isNull(look: A) = JSONStatic.isNull(accessOf(look))

    @CreatorsDsl
    infix fun isLong(look: A) = JSONStatic.isLong(accessOf(look))

    @CreatorsDsl
    infix fun isDate(look: A) = JSONStatic.isDate(accessOf(look))

    @CreatorsDsl
    infix fun isArray(look: A) = JSONStatic.isArray(accessOf(look))

    @CreatorsDsl
    infix fun isObject(look: A) = JSONStatic.isObject(accessOf(look))

    @CreatorsDsl
    infix fun isNumber(look: A) = JSONStatic.isNumber(accessOf(look))

    @CreatorsDsl
    infix fun isString(look: A) = JSONStatic.isString(accessOf(look))

    @CreatorsDsl
    infix fun isDouble(look: A) = JSONStatic.isDouble(accessOf(look))

    @CreatorsDsl
    infix fun isInteger(look: A) = JSONStatic.isInteger(accessOf(look))

    @CreatorsDsl
    infix fun isBoolean(look: A) = JSONStatic.isBoolean(accessOf(look))

    @CreatorsDsl
    infix fun isFunction(look: A) = JSONStatic.isFunction(accessOf(look))

    @CreatorsDsl
    fun getTypeOf(look: A) = JSONStatic.getTypeOf(accessOf(look))

    @CreatorsDsl
    fun asLong(look: A): Long? = JSONStatic.asLong(accessOf(look))

    @CreatorsDsl
    fun asDate(look: A): Date? = JSONStatic.asDate(accessOf(look))

    @CreatorsDsl
    fun asInteger(look: A): Int? = JSONStatic.asInteger(accessOf(look))

    @CreatorsDsl
    fun asDouble(look: A): Double? = JSONStatic.asDouble(accessOf(look))

    @CreatorsDsl
    fun asString(look: A): String? = JSONStatic.asString(accessOf(look))

    @CreatorsDsl
    fun asBoolean(look: A): Boolean? = JSONStatic.asBoolean(accessOf(look))

    @CreatorsDsl
    fun asArray(look: A): JSONArray? = JSONStatic.asArray(accessOf(look))

    @CreatorsDsl
    fun asObject(look: A): JSONObject? = JSONStatic.asObject(accessOf(look))

    @CreatorsDsl
    fun accessOf(look: A): Any? = if (isDefined(look)) findOf(look) else null
}