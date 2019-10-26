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

package co.mercenary.creators.kotlin.json.base

class JSONObject : LinkedHashMap<String, Any?>, JSONBase<String, JSONObject> {

    constructor() : super()

    constructor(args: Map<String, Any?>) : super(args)

    constructor(args: PropertiesMapProvider) : super(args.toPropertiesMap())

    constructor(k: String, v: Any?) {
        set(k, v)
    }

    constructor(vararg args: Pair<String, Any?>) {
        putAll(args)
    }

    constructor(args: Iterable<Pair<String, Any?>>) {
        putAll(args)
    }

    constructor(args: Sequence<Pair<String, Any?>>) {
        putAll(args)
    }

    override fun toString() = toJSONString()

    override fun copyOf() = JSONStatic.toDeepCopy(this, JSONObject::class)

    override fun isDefined(look: String) = look in keys

    override fun finderOf() = this::get

    operator fun set(k: String, v: Any?) = apply { put(k, v) }

    companion object {
        private const val serialVersionUID = 2L
    }
}


