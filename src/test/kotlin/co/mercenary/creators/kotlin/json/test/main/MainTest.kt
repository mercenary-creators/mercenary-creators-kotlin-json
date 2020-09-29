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

package co.mercenary.creators.kotlin.json.test.main

import co.mercenary.creators.kotlin.json.*
import co.mercenary.creators.kotlin.util.*
import org.junit.jupiter.api.Test

class MainTest : KotlinTest() {

    @Test
    fun test() {
        val data = json("name" pair author, "time" pair 57.years - 2.weeks, "list" pair sequenceOf(2, 4).toList(), "horz" pair "Maël Hörz\n")
        info { data.copyOf() }
        data.size shouldBe 4
        data["date"] = dateOf()
        info { data }
        data.size shouldBe 5
        warn { data isDefined "date" }
        warn { data isDate "date" }
        warn { data isLong "date" }
        warn { data isDefined "json" }
        data.evaluate().delete("$.date").put("$", "json", json("self" pair "Dean S. Jones"))
        error { data }
        error { data isDefined "date" }
        error { data isDefined "json" }
        data.evaluate().put("$.json", "case", json("good" pair true, "size" pair DEFAULT_BUFFER_SIZE))
        data.evaluate().add("$.list", 6)
        dashes()
        error { data }
        val main = MainData(57.years - 2.weeks, sequenceOf(2, 4))
        val buff = main.toString()
        info { buff }
        val back = buff.readOf<MainData>()
        info { back }
        warn { this }
        error { here() }
        info { this }
        val maps = json("level" pair loggerOf().getLevel())
        dashes()
        warn { maps }
        warn { maps.nameOf() }
        val args = maps.toDataType<LogsData>()
        warn { args }
        warn { args.nameOf() }
        dashes()
        val keys = LogsData(loggerOf().getLevel())
        error { keys }
        val text = keys.toString()
        info { text }
        val make = text.readOf<LogsData>()
        error { make }
    }
}