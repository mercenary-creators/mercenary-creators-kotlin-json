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

package co.mercenary.creators.kotlin.json.module

import co.mercenary.creators.kotlin.util.*
import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

object TimeDurationDeerializer : StdDeserializer<TimeDuration>(TimeDuration::class.java) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext?): TimeDuration {
        if (parser.currentToken == JsonToken.VALUE_STRING) {
            return TimeDuration.parseCharSequence(parser.text)
        }
        throw MercenaryFatalExceptiion("not a string for TimeDuration")
    }
}