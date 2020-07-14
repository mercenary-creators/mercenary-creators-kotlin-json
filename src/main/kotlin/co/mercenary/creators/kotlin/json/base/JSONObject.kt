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

import co.mercenary.creators.kotlin.json.*
import co.mercenary.creators.kotlin.util.*

class JSONObject : LinkedHashMap<String, Any?>, JSONBase<String, JSONObject> {

    @CreatorsDsl
    constructor() : super()

    @CreatorsDsl
    constructor(args: Map<String, Any?>) : super(args.toMap())

    @CreatorsDsl
    constructor(k: String, v: Any?) : super(dictOf(k to v).toMap())

    @CreatorsDsl
    constructor(vararg args: Pair<String, Any?>) : super(args.toMap())

    @CreatorsDsl
    override val size: Int
        @IgnoreForSerialize
        get() = sizeOf()

    @CreatorsDsl
    override fun clear() = super.clear()

    @CreatorsDsl
    override fun clone() = copyOf()

    @CreatorsDsl
    override fun copyOf() = deepOf()

    @CreatorsDsl
    override fun sizeOf() = super.size

    @CreatorsDsl
    @IgnoreForSerialize
    override fun isEmpty() = super.isEmpty().isTrue()

    @CreatorsDsl
    override infix fun isDefined(look: String) = isNotEmpty() && look in keys

    @CreatorsDsl
    override fun findOf(look: String) = super.get(look)

    @CreatorsDsl
    override fun equals(other: Any?) = when (other) {
        is JSONObject -> this === other || sizeOf() == other.sizeOf() && super.equals(other)
        else -> false
    }

    @CreatorsDsl
    override fun hashCode() = super.hashCode()

    @CreatorsDsl
    override fun toString() = toJSONString(true)

    companion object {
        private const val serialVersionUID = 2L
    }
}


