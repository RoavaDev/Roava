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

package dev.roava.group

import dev.roava.api.GroupApi
import dev.roava.client.RoavaClient
import dev.roava.client.RoavaRequest
import dev.roava.json.group.GroupData
import dev.roava.json.group.GroupRankListData
import dev.roava.json.group.RoleRequest
import dev.roava.user.User
import retrofit2.HttpException

/**
 * A class which represents a Group.
 */
class Group {
    private val request: RoavaRequest = RoavaRequest()
    private lateinit var client: dev.roava.client.RoavaClient

    val name: String
    val description: String
    val memberCount: Int
    val id: Int
    val owner: User

    internal constructor(groupData: GroupData) {
        name = groupData.name!!
        description = groupData.description!!
        memberCount = groupData.memberCount!!
        id = groupData.id!!
        owner = User(groupData.owner!!)
    }

    /**
     * Creates a new [Group] objected provided the group ID of the Group.
     *
     * <b>Sample</b>
     *
     * Java
     * ```
     * // This represents the Roblox group as a Group
     * Group group = new Group(7);
     * // Prints the group's name
     * System.out.println(group.name);
     * ```
     *
     * @param[groupId] The group's ID
     * @throws[RuntimeException]
     */
    @Throws(RuntimeException::class)
    constructor(groupId: Int) {
        try {
            val groupData = request.createRequest(GroupApi::class.java, "groups")
                .getGroupInfo(groupId)
                .execute()
                .body()!!

            name = groupData.name!!
            description = groupData.description!!
            memberCount = groupData.memberCount!!
            id = groupData.id!!
            owner = User(groupData.owner!!)
        } catch(exception: Exception) {
            throw RuntimeException("The provided group ID is invalid!")
        }
    }

    /**
     * Method to set the Group's [RoavaClient] for authentication purposes.
     */
    fun setClient(client: dev.roava.client.RoavaClient) {
        this.client = client
    }

    /**
     * Method to get the Group's roles.
     *
     * @throws[RuntimeException]
     * @return[List]
     */
    @Throws(RuntimeException::class)
    fun getRoles(): List<GroupRole> {
        val roles: MutableList<GroupRole> = mutableListOf()

        try {
            val roleData = request.createRequest(GroupApi::class.java, "groups")
                .getGroupRoles(id)
                .execute()
                .body()

            for (role in roleData?.roles!!) {
                roles.add(GroupRole(role))
            }
        } catch(exception: Exception) {
            throw RuntimeException("Could not get the group's roles!")
        }

        return roles.toList()
    }

    /**
     * Method to get a specific Group role provided the role's name.
     *
     * @param[roleName] The role's name
     * @throws[RuntimeException]
     * @return[GroupRole]
     */
    fun getRole(roleName: String): GroupRole {
        val roles = getRoles()

        for (role in roles) {
            if (role.name == roleName) {
                return role;
            }
        }

        throw RuntimeException("The provided role name does not exist!")
    }

    /**
     * Method to get a specific Group role provided the role's rank or roleset ID
     *
     * @param[roleNumber] The role's rank or roleset ID
     * @throws[RuntimeException]
     * @return[GroupRole]
     */
    @Throws(RuntimeException::class)
    fun getRole(roleNumber: Int): GroupRole {
        // If the role number provided is above 255, we can assume it's a roleset ID
        if (roleNumber > 255) {
            try {
                val roleData = request.createRequest(GroupApi::class.java, "groups")
                    .getGroupRoleInfo(roleNumber)
                    .execute()
                    .body()

                roleData?.roles?.get(0)?.let {
                    // Check if the role is part of the Group
                    if (it.groupId == id) {
                        return GroupRole(it)
                    }
                }

                throw RuntimeException("The provided role ID is not a part of this group!")
            } catch(exception: Exception) {
                throw RuntimeException("The provided role ID does not exist!")
            }
        }

        // Get all the Group's roles
        val roles = getRoles()

        for (role in roles) {
            // Check if the rank is the same as the role number provided
            if (role.rank == roleNumber) {
                return role
            }
        }

        throw RuntimeException("The provided role rank does not exist!")
    }

    /**
     * Method to get the rank of a User in a group provided a User ID
     *
     * @param[userId] The User's ID
     * @throws[RuntimeException]
     * @return[GroupRole]
     */
    @Throws(RuntimeException::class)
    fun getUserRank(userId: Long): GroupRole {
        try {
            val groupData = request.createRequest(GroupApi::class.java, "groups")
                .getUserRoleInfo(userId)
                .execute()
                .body()

            for (group in groupData?.data!!) {
                if (group.groupData?.id!! == id) {
                    return GroupRole(group.roleData!!)
                }
            }

            throw RuntimeException("The provided user is not a part of this group!")
        } catch(exception: Exception) {
            throw RuntimeException("Could not fetch the user's groups!")
        }
    }

    @Throws(RuntimeException::class)
    fun getUserRank(user: User): GroupRole {
        return getUserRank(user.id)
    }

    /**
     * Method to set the rank of a user in a group provided both the user's ID and the role's rank or roleset ID
     *
     * @param[userId] The user's ID
     * @param[roleNumber] The role's rank or roleset ID
     * @throws[RuntimeException]
     */
    @Throws(RuntimeException::class)
    fun rankUser(userId: Long, roleNumber: Int) {
        if (client == null) {
            throw RuntimeException("No client has been provided!")
        }

        // Make the role number mutable
        var roleNumber = roleNumber

        // Get the roleset ID if a rank is provided as a role number
        if (roleNumber <= 255) {
            roleNumber = getRole(roleNumber).id
        }

        val result = runCatching {
            client.request.createRequest(GroupApi::class.java, "groups")
                .rankUser(id, userId, RoleRequest(roleNumber))
                .execute()
        }

        result.onFailure { exception ->
            if (exception is HttpException) {
                val errorCode = exception.code()
                val message = exception.message()

                throw RuntimeException("Ranking user with id $userId failed with message \"$message\" and response code $errorCode")
            } else {
                throw RuntimeException("An unknown error has occurred while ranking the user!")
            }
        }
    }

    @Throws(RuntimeException::class)
    fun rankUser(user: User, roleNumber: Int) {
        rankUser(user.id, roleNumber)
    }

    /**
     * Method to exile a user provided the user's ID
     *
     * @param[userId] The user's ID
     * @throws[RuntimeException]
     */
    @Throws(RuntimeException::class)
    fun exileUser(userId: Long) {
        if (client == null) {
            throw RuntimeException("No client has been provided!")
        }

        runCatching {
            client.request.createRequest(GroupApi::class.java, "groups")
                .exileUser(id, userId)
                .execute()
        }.onFailure {
            throw RuntimeException("Could not exile the provided user!")
        }
    }

    @Throws(RuntimeException::class)
    fun exileUser(user: User) {
        exileUser(user.id)
    }

    /**
     * Method to grab all group members with the provided roleset
     *
     * @return[GroupRankListData]
     * @param[roleSetId] The roleSetId of the group rank.
     * @throws[RuntimeException]
     */
    @Throws(RuntimeException::class)
    fun getGroupRankMembers(roleSetId: Int): List<GroupRankListData>{
        val members: MutableList<GroupRankListData> = mutableListOf()
        var nextPageCursor: String? = null
        fun makeRequest(){
            val result = runCatching {
                request.createRequest(GroupApi::class.java, "groups")
                    .getGroupRankMembers(this.id,roleSetId, 100, nextPageCursor)
                    .execute()
            }
            result.onFailure { exception ->
                if (exception is HttpException) {
                    val errorCode = exception.code()
                    val message = exception.message()

                    throw RuntimeException("Grabbing members in the group with id ${this.id} & roleSetId of $roleSetId failed with message \"$message\" and response code $errorCode")
                } else {
                    throw RuntimeException("an unknown error has occurred while fetching the members with that rank!\n${exception.message}")
                }
            }
            result.onSuccess {
                nextPageCursor = it.body()?.nextPageCursor
                val data = it.body()?.data ?: throw RuntimeException("An unknown error has occurred")
                for(i in data){
                    members += GroupRankListData(i.hasVerifiedBadge,i.userId,i.username,i.displayName)
                }
            }
        }
        makeRequest()
        while(nextPageCursor != null) {
            makeRequest()
        }
        return members
    }

}