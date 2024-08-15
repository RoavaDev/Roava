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

package dev.roava.json.group

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupMemberData (
    @JsonProperty("nextPageCursor")
    val nextPageCursor: String?,
    @JsonProperty("previousPageCursor")
    val previousCursor: String?,
    @JsonProperty("data")
    val data: List<GroupMemberListData>
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupMemberListData (
    @JsonProperty("user")
    val user: GroupMemberUserData,
    @JsonProperty("role")
    val role: GroupMemberRoleData
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupMemberUserData (
    @JsonProperty("username")
    val username: String?,
    @JsonProperty("userId")
    val userId: Long?,
    @JsonProperty("displayName")
    val displayName: String?,
    @JsonProperty("hasVerifiedBadge")
    val hasVerifiedBadge: Boolean?,
    @JsonProperty("buildersClubMembershipType")
    val buildersClubMembershipType: Long?
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupMemberRoleData (
    @JsonProperty("id")
    val roleId: Long?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("rank")
    val rank: Long?,
    @JsonProperty("memberCount")
    val memberCount: Long?
)