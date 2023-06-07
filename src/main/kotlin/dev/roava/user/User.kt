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

package dev.roava.user

import dev.roava.api.FriendApi
import dev.roava.api.GroupApi
import dev.roava.api.InventoryApi
import dev.roava.api.UserApi
import dev.roava.client.RoavaRequest
import dev.roava.group.Group
import dev.roava.json.user.UserData
import dev.roava.json.user.UserNameRequest

/**
 * A class which represents a User which is not authenticated by the [dev.roava.client.RoavaClient].
 */
class User {
    private val request: RoavaRequest = RoavaRequest()

    val displayName: String
    val name: String
    val id: Long

    internal constructor(userData: UserData) {
        displayName = userData.displayName!!
        name = userData.name!!
        id = userData.id!!
    }

    /**
     * Creates a new [User] object provided the user ID of the User.
     *
     * <b>Sample</b>
     *
     * Java
     * ```
     * // This represents the Roblox account as a User
     * User user = new User(1);
     * // Prints the user's name
     * System.out.println(user.name);
     * ```
     *
     * @param[userId] The User's user ID
     * @throws[RuntimeException]
     */
    @Throws(RuntimeException::class)
    constructor(userId: Long) {
        try {
            val userData = request.createRequest(UserApi::class.java, "users")
                .getUserInformation(userId)
                .execute()
                .body()

            displayName = userData?.displayName!!
            name = userData.name!!
            id = userData.id!!
        } catch(exception: Exception) {
            throw RuntimeException("The provided user ID is invalid!")
        }
    }

    /**
     * Creates a new [User] object provided the username of the User.
     *
     * <b>Sample</b>
     *
     * Java
     * ```
     * // This represents the Roblox account as a User
     * User user = new User("Roblox");
     * // Prints the User's name
     * System.out.println(client.name);
     * ```
     * @param[username] The User's username
     * @throws[RuntimeException]
     */
    @Throws(RuntimeException::class)
    constructor(username: String) {
        try {
            val usernameData = request.createRequest(UserApi::class.java, "users")
                .getUsernameInformation(UserNameRequest(listOf(username)))
                .execute()
                .body()

            val userData = usernameData?.data?.get(0)

            displayName = userData?.displayName!!
            name = userData.name!!
            id = userData.id!!
        } catch(exception: Exception) {
            throw RuntimeException("The provided username is invalid!")
        }
    }

    /**
     * Method to get the User's current friends represented as a List of individual [User]s
     *
     * @throws[RuntimeException]
     * @return[List]
     */
    @Throws(RuntimeException::class)
    fun getFriends(): List<User> {
        var friends = mutableListOf<User>()

        try {
            val userData = request.createRequest(FriendApi::class.java, "friends")
                .getUserFriends(id)
                .execute()
                .body()

            for (user in userData?.data!!) {
                friends.add(User(user))
            }
        } catch(exception: Exception) {
            throw RuntimeException("Could not fetch the user's friends!")
        }

        return friends.toList()
    }

    /**
     * Method to get the User's current groups represented as a List of individual [dev.roava.group.Group]s
     *
     * @throws[RuntimeException]
     * @return[List]
     */
    @Throws(RuntimeException::class)
    fun getGroups(): List<Group> {
        val groups = mutableListOf<Group>()

        try {
            val groupData = request.createRequest(GroupApi::class.java, "groups")
                .getUserRoleInfo(id)
                .execute()
                .body()

            for (group in groupData?.data!!) {
                groups.add(Group(group.groupData!!))
            }
        } catch(exception: Exception) {
            throw RuntimeException("Could not fetch the user's groups!")
        }

        return groups.toList()
    }

    /**
     * Method to get if a User is in a group
     *
     * @param[groupId] The group's ID
     * @throws[RuntimeException]
     * @return[Boolean]
     */
    @Throws(RuntimeException::class)
    fun isInGroup(groupId: Int): Boolean {
        val groups = this.getGroups()

        for (group in groups) {
            if (group.id == groupId) {
                return true
            }
        }

        return false
    }

    /**
     * Method to get if a User has a gamepass
     *
     * @param[gamepassId] The gamepass's ID
     * @throws[RuntimeException]
     * @return[Boolean]
     */
    @Throws(RuntimeException::class)
    fun hasGamepass(gamepassId: Long) = runCatching {
        val gamepasses = request.createRequest(InventoryApi::class.java, "inventory")
            .getIsOwned(id, "gamepass", gamepassId)
            .execute()
            .body()

        gamepasses ?: false
    }.getOrElse {
        throw RuntimeException("Could not fetch the user's items!")
    }
}