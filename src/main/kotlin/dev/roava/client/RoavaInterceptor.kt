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

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * For Intercepting calls and adding the X-CSRF-TOKEN to the header if the original request failed (for internal use only).
 */
internal class RoavaInterceptor: Interceptor {
    private val header = "X-CSRF-TOKEN"

    private var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val orig = chain.request()
        val request = orig.newBuilder()
            .header(header, token ?: "")
            .build()

        var response = chain.proceed(request)

        if (response.code() == 403 && !hasToken(response)) {
            token = response.header(header)

            token?.let {
                response.close()

                response = chain.proceed(retry(response.request(), it)!!)
            }
        }

        return response
    }

    private fun hasToken(response: Response?): Boolean {
        response?.let {
            return !it.request().header(header).isNullOrEmpty()
        }

        return false
    }

    private fun retry(request: Request?, token: String): Request? {
        return request?.newBuilder()
            ?.header(header, token)
            ?.build()
    }
}