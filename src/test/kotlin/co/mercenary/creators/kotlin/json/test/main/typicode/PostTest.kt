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

package co.mercenary.creators.kotlin.json.test.main.typicode

import co.mercenary.creators.kotlin.*
import org.junit.jupiter.api.Test

class PostTest : KotlinTest() {
    @Test
    fun test() {
        val list = toJSONArray(TypicodePostData.link())
        info { list }
        list.size.shouldBe(TypicodePostData.LIST_SIZE) {
            list.size
        }
    }
}