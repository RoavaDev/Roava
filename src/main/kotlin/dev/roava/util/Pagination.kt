/*
 * MIT License
 *
 * Copyright (c) 2024 RoavaDev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.roava.util

import com.fasterxml.jackson.annotation.JsonProperty
import dev.roava.client.RoavaClient
import dev.roava.client.RoavaRequest
import retrofit2.Call
import kotlin.reflect.KFunction

// T would be a data class
class Pagination<T>(
    private val url: String,
    private val client: RoavaClient,

    @JsonProperty("nextPageCursor")
    private val nextCursor: String?,

    @JsonProperty("previousPageCursor")
    private val previousCursor: String?,

    @JsonProperty("data")
    private val data: List<T>? // this is where we actually get what we are after
) {
    // this function might or might not be necessary
    private fun paginate(function: () -> Any): Pagination<T> {
        // return a new pagination object where you take the current URL with the cursor and return a new pagination object
        return function.execute()
    }

    fun next(): Pagination<T>? {
        return this.nextCursor?.let { this.paginate(it) }
    }

    fun previous(): Pagination<T>? {
        return this.previousCursor?.let { this.paginate(it) };
    }
}