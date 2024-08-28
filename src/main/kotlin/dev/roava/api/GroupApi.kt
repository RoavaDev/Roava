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

import dev.roava.json.group.*
import dev.roava.json.user.UserRolesData
import dev.roava.util.Pagination
import retrofit2.Call
import retrofit2.http.*

/**
 * GroupApi (for internal use only)
 */
interface GroupApi {
    @GET("/v1/groups/{groupId}")
    fun getGroupInfo(@Path("groupId") groupId: Int): Call<GroupData>

    @GET("/v1/users/{userId}/groups/roles")
    fun getUserRoleInfo(@Path("userId") userId: Long): Call<UserRolesData>

    @GET("/v1/groups/{groupId}/roles")
    fun getGroupRoles(@Path("groupId") groupId: Int): Call<RoleListData>

    @GET("v1/roles")
    fun getGroupRoleInfo(@Query("ids") roleId: Int): Call<RoleListData>

    @PATCH("/v1/groups/{groupId}/users/{userId}")
    fun rankUser(@Path("groupId") groupId: Int, @Path("userId") userId: Long, @Body roleId: RoleRequest): Call<Void>

    @DELETE("/v1/groups/{groupId}/users/{userId}")
    fun exileUser(@Path("groupId") groupId: Int, @Path("userId") userId: Long): Call<Void>

    @GET("v1/groups/{groupId}/roles/{roleSetId}/users")
    fun getGroupRankMembers(@Path("groupId") groupId: Int, @Path("roleSetId") roleSetId: Int, @Query("limit") limit: Int, @Query("cursor") cursor: String? = null): Call<GroupRankData>

    @GET("v1/groups/{groupId}/users")
    fun getGroupMembers(@Path("groupId") groupId: Int, @Query("limit") limit: Int, @Query("cursor") cursor: String? = null): Call<Pagination<GroupMemberData>>
}