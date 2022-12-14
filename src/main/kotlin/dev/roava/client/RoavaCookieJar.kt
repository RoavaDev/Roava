/*
 * MIT License
 *
 * Copyright (c) 2022 RoavaDev
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

package dev.roava.client

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Cookie Jar to store the [RoavaRequest]'s cookies (for internal use only).
 */
internal class RoavaCookieJar(_cookie: String): CookieJar  {
    private val cookie = Cookie.Builder()
        .name(".ROBLOSECURITY")
        .value(_cookie)
        .domain("roblox.com")
        .build()

    private val cookies = mutableListOf(cookie)

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        for (cookie in cookies) {
            if (!this.cookies.contains(cookie)) {
                this.cookies.add(cookie)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return cookies
    }
}