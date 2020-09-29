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
import com.fasterxml.jackson.annotation.*

class LogsData @CreatorsDsl constructor(private val data: LoggingLevel) : JSONAware, Copyable<LogsData>, Cloneable {

    @JsonCreator
    @CreatorsDsl
    private constructor(@JsonProperty("level") from: String) : this(LoggingLevel.from(from))

    @get:JsonProperty("level")
    private val level: String
        get() = data.toString()

    @CreatorsDsl
    override fun clone() = copyOf()

    @CreatorsDsl
    override fun copyOf() = deepOf()

    @CreatorsDsl
    override fun toString() = toJSONString()

    @CreatorsDsl
    override fun hashCode() = data.hashCode()

    @CreatorsDsl
    override fun equals(other: Any?) = when (other) {
        is LogsData -> this === other || data == other.data
        else -> false
    }
}