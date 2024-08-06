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

import com.fasterxml.jackson.databind.ObjectMapper
import dev.roava.api.*
import dev.roava.client.RoavaRequest
import dev.roava.group.Group
import dev.roava.json.user.ThumbnailListData
import dev.roava.json.user.UserData
import dev.roava.json.user.UserNameRequest
import retrofit2.HttpException
import java.net.URI

/**
 * A class which represents a User which is not authenticated by the [dev.roava.client.RoavaClient].
 */
class User {
    private val request: RoavaRequest = RoavaRequest()

    val displayName: String
    val name: String
    val id: Long
    val description: String

    internal constructor(userData: UserData) {
        displayName = userData.displayName!!
        name = userData.name!!
        id = userData.id!!
        description = userData.description ?: ""
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
            description = userData.description ?: ""
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
            description = userData.description ?:""
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

        val result = runCatching {
            request.createRequest(GroupApi::class.java, "groups")
                .getUserRoleInfo(id)
                .execute()
        }

        result.onFailure { exception ->  
            if (exception is HttpException) {
                val errorCode = exception.code()
                val message = exception.message()

                throw RuntimeException("Ranking user with id ${this.id} failed with message \"$message\" and response code $errorCode")
            } else {
                throw RuntimeException("An unknown error has occurred while fetching the user's groups!")
            }
        }.onSuccess {
            val groupData = it.body()
            
            for (group in groupData?.data!!) {
                groups.add(Group(group.groupData!!))
            }
        }

        return groups.toList()
    }

        /**
     * Method to get a User's Avatar
     *
     * Available Values:
     * 30x30, 48x48, 60x60, 75x75, 100x100, 110x110, 140x140, 150x150, 150x200, 180x180, 250x250, 352x352, 420x420, 720x720
     * @throws[RuntimeException]
     * @return[String]
     */
    @Throws(RuntimeException::class)
    fun getAvatar(size: String,isCircular: Boolean): String {
        var thumbnail = ""
        val result = runCatching {
            request.createRequest(ThumbnailApi::class.java, "thumbnails")
                .getAvatar(id,size,"Png",isCircular)
                .execute()
        }
        result.onFailure { exception ->
            if (exception is HttpException) {
                val errorCode = exception.code()
                val message = exception.message()

                throw RuntimeException("Grabbing thumbnail of user with id ${this.id} failed with message \"$message\" and response code $errorCode")
            } else {
                throw RuntimeException("an unknown error has occurred while fetching the user's thumbnail!\n${exception.message}")
            }
        }.onSuccess {
            thumbnail = it.body()?.data?.get(0)?.thumbnail?: ""
        }
        return thumbnail
    }

    /**
     * Method to get a User's Headshot
     *
     * Available Values:
     * 30x30, 48x48, 60x60, 75x75, 100x100, 110x110, 140x140, 150x150, 150x200, 180x180, 250x250, 352x352, 420x420, 720x720
     * @throws[RuntimeException]
     * @return[String]
     */
    @Throws(RuntimeException::class)
    fun getHeadShot(size: String,isCircular: Boolean): String {
        var thumbnail = ""
        val result = runCatching {
            request.createRequest(ThumbnailApi::class.java, "thumbnails")
                .getHeadShot(id, size, "Png", isCircular)
                .execute()
        }
        result.onFailure { exception ->
            if (exception is HttpException) {
                val errorCode = exception.code()
                val message = exception.message()

                throw RuntimeException("Grabbing headshot of user with id ${this.id} failed with message \"$message\" and response code $errorCode")
            } else {
                throw RuntimeException("an unknown error has occurred while fetching the user's headshot!\n${exception.message}")
            }
        }.onSuccess {
            thumbnail = it.body()?.data?.get(0)?.thumbnail ?: ""
        }
        return thumbnail
    }
    /**
     * Method to get a User's Bust
     *
     * Available Values:
     * 48x48, 50x50, 60x60, 75x75, 100x100, 150x150, 180x180, 352x352, 420x420
     * @throws[RuntimeException]
     * @return[String]
     */
    @Throws(RuntimeException::class)
    fun getBust(size: String,isCircular: Boolean): String {
        var thumbnail = ""
        val result = runCatching {
            request.createRequest(ThumbnailApi::class.java, "thumbnails")
                .getBust(id, size, "Png", isCircular)
                .execute()
        }
        result.onFailure { exception ->
            if (exception is HttpException) {
                val errorCode = exception.code()
                val message = exception.message()

                throw RuntimeException("Grabbing bust of user with id ${this.id} failed with message \"$message\" and response code $errorCode")
            } else {
                throw RuntimeException("an unknown error has occurred while fetching the user's bust!\n${exception.message}")
            }
        }.onSuccess {
            thumbnail = it.body()?.data?.get(0)?.thumbnail ?: ""
        }
        return thumbnail
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