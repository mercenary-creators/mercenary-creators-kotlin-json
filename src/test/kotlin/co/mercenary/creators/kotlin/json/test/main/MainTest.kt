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

package co.mercenary.creators.kotlin.json.test.main

import co.mercenary.creators.kotlin.json.*
import co.mercenary.creators.kotlin.util.*
import org.junit.jupiter.api.Test

class MainTest : KotlinTest() {
    @Test
    fun test() {
        val data = json("author" to author, "age" to 53.years + 4.weeks, "list" to sequenceOf(2, 4))
        info { data }
        data.size shouldBe 3
        data["date"] = getTimeStamp().toDate()
        info { data }
        data.size shouldBe 4
        val main = MainData(53.years + 4.weeks, sequenceOf(2, 4))
        val buff = main.toString()
        info { buff }
        val back = jsonRead<MainData>(buff)
        info { back }
    }
}