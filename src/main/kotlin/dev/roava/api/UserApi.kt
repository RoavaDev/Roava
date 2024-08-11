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

package dev.roava.api

import dev.roava.json.user.UserData
import dev.roava.json.user.UserListData
import dev.roava.json.user.UserNameHistoryData
import dev.roava.json.user.UserNameRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * UserApi (for internal use only)
 */
interface UserApi {
    @GET("/v1/users/{userId}")
    fun getUserInformation(@Path("userId") userId: Long): Call<UserData>

    @POST("/v1/usernames/users")
    fun getUsernameInformation(@Body data: UserNameRequest): Call<UserListData>

    @GET("/v1/users/{userId}/username-history")
    fun getPastUsernames(@Path("userId") userId: Long, @Query("limit") limit: String, @Query("cursor") cursor: String? = null): Call<UserNameHistoryData>
}