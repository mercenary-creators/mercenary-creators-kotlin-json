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
import java.util.*

interface JSONAccess<A> {

    @IgnoreForSerialize
    fun finderOf(): (A) -> Any?

    @AssumptionDsl
    infix fun isDefined(look: A): Boolean

    @AssumptionDsl
    infix fun isNull(look: A) = JSONStatic.isNull(accessOf(look))

    @AssumptionDsl
    infix fun isLong(look: A) = JSONStatic.isLong(accessOf(look))

    @AssumptionDsl
    infix fun isDate(look: A) = JSONStatic.isDate(accessOf(look))

    @AssumptionDsl
    infix fun isArray(look: A) = JSONStatic.isArray(accessOf(look))

    @AssumptionDsl
    infix fun isObject(look: A) = JSONStatic.isObject(accessOf(look))

    @AssumptionDsl
    infix fun isNumber(look: A) = JSONStatic.isNumber(accessOf(look))

    @AssumptionDsl
    infix fun isString(look: A) = JSONStatic.isString(accessOf(look))

    @AssumptionDsl
    infix fun isDouble(look: A) = JSONStatic.isDouble(accessOf(look))

    @AssumptionDsl
    infix fun isInteger(look: A) = JSONStatic.isInteger(accessOf(look))

    @AssumptionDsl
    infix fun isBoolean(look: A) = JSONStatic.isBoolean(accessOf(look))

    @AssumptionDsl
    infix fun isFunction(look: A) = JSONStatic.isFunction(accessOf(look))

    fun getTypeOf(look: A) = JSONStatic.getTypeOf(accessOf(look))

    @AssumptionDsl

    fun isTypeOf(look: A, type: JSONTypeOf) = type == getTypeOf(look)
    fun asLong(look: A): Long? = JSONStatic.asLong(accessOf(look))
    fun asDate(look: A): Date? = JSONStatic.asDate(accessOf(look))
    fun asInteger(look: A): Int? = JSONStatic.asInteger(accessOf(look))
    fun asDouble(look: A): Double? = JSONStatic.asDouble(accessOf(look))
    fun asString(look: A): String? = JSONStatic.asString(accessOf(look))
    fun asBoolean(look: A): Boolean? = JSONStatic.asBoolean(accessOf(look))
    fun asArray(look: A): JSONArray? = JSONStatic.asArray(accessOf(look))
    fun asObject(look: A): JSONObject? = JSONStatic.asObject(accessOf(look))
    fun accessOf(look: A): Any? = if (isDefined(look)) finderOf().invoke(look) else null
}