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

package co.mercenary.creators.kotlin.json

class JSONArray : ArrayList<Any?>, JSONBase<Int, JSONArray> {

    constructor() : super()

    constructor(size: Int) : super(size)

    constructor(list: List<Any?>) : super(list)

    constructor(vararg list: Any?) : super(list.toList())

    constructor(list: Iterable<Any?>) {
        addAll(list)
    }

    constructor(list: Sequence<Any?>) {
        addAll(list)
    }

    override fun toString() = toJSONString()

    override fun copyOf() = JSONStatic.toDeepCopy(this, JSONArray::class)

    override fun isDefined(look: Int) = look in 0 until size

    override fun finderOf() = this::get

    companion object {
        private const val serialVersionUID = 2L
    }
}