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

package co.mercenary.creators.kotlin.json.test.main.path

import co.mercenary.creators.kotlin.json.*
import org.junit.jupiter.api.Test

class JSONPathTest : KotlinTest() {
    @Test
    fun test() {
        val path = JSONPath.path(loader["data.json"])
        warn { path }
        warn { JSONPath.compile("$") }
        warn { JSONPath.compile("$.null-property") }
        warn { JSONPath.compile("$.store.book[*]") }
        warn { JSONPath.compile("$.store.book[0]") }
        val data = path.eval<JSONObject>("$")
        info { data }
        data.size shouldBe 10
        val bool = path.eval<Boolean>("$.boolean-property")
        info { bool }
        bool shouldBe true
        val nope = path.read<Any>("$.null-property")
        info { nope }
        nope shouldBe null
        val json = path.eval<JSONObject>("$.store.book[0]")
        info { json }
        json["category"] shouldBe "reference"
        val book = path.eval<BookData>("$.store.book[1]")
        info { book }
        book.category shouldBe "fiction"
        val lord = path.eval<BookData>("$.store.book[3]")
        info { lord }
        val list = path.eval<List<BookData>>("$.store.book[*]")
        info { list }
        list.size shouldBe 4
        list[3].author shouldBe "J. R. R. Tolkien"
        info { path.deep().delete("$.null-property").delete("$.foo").delete("$.@id").set("$.int-max-property", 1111).add("$.store.book", BookData("reference", "Dean S. Jones", "Kotlin 1.4", "0-395-19395-8", 52.99)).put("$", "dean", 57).eval<JSONObject>("$") }
    }
}