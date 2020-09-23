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

data class TypicodeUserData(val id: Int, val name: String, val username: String, val email: String, val address: TypicodeAddressData, val phone: String, val website: String, val company: TypicodeCompanyData) : AbstractTypicodeAware {

    override fun toString() = toJSONString()

    companion object {

        @JvmStatic
        @CreatorsDsl
        @IgnoreForSerialize
        fun size() = 10

        @JvmStatic
        @CreatorsDsl
        @IgnoreForSerialize
        fun path() = "http://jsonplaceholder.typicode.com/users"
    }
}