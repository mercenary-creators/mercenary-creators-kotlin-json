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

package co.mercenary.creators.kotlin.json.util.typicode

import co.mercenary.creators.kotlin.json.JSONObjectAware
import java.net.URL

data class TypicodeTodoData(val userId: Int, val id: Int, val title: String, val completed: Boolean) : JSONObjectAware {
    override fun toString() = toJSONString()

    companion object {
        const val LIST_SIZE = 200
        const val BASE_PATH = "http://jsonplaceholder.typicode.com/todos"
        fun link() = URL(BASE_PATH)
    }
}