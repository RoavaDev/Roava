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
    // FIXME: Cache the X-CSRF-TOKEN to not spam unnecessary requests
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())

        if (response.code() == 403 && !hasToken(response)) {
            val token: String? = response.header("X-CSRF-TOKEN")

            token?.let {
                response.close()

                response = chain.proceed(retry(response.request(), it)!!)
            }
        }

        return response
    }

    private fun hasToken(response: Response?): Boolean {
        response?.let {
            return !it.request().header("X-CSRF-TOKEN").isNullOrEmpty()
        }

        return false
    }

    private fun retry(request: Request?, token: String): Request? {
        return request?.newBuilder()
            ?.header("X-CSRF-TOKEN", token)
            ?.build()
    }
}