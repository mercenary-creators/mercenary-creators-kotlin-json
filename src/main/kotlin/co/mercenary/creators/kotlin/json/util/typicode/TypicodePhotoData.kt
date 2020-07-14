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

package co.mercenary.creators.kotlin.json.util.typicode

import co.mercenary.creators.kotlin.util.*

data class TypicodePhotoData(val albumId: Int, val id: Int, val title: String, val url: String, val thumbnailUrl: String) : Typicode {

    override fun toString() = toJSONString()

    companion object {

        @JvmStatic
        @CreatorsDsl
        @IgnoreForSerialize
        fun size() = 5000

        @JvmStatic
        @CreatorsDsl
        @IgnoreForSerialize
        fun path() = "http://jsonplaceholder.typicode.com/photos"
    }
}