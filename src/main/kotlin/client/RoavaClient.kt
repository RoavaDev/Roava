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

package client

import api.ProfileApi
import user.User

/**
 * A class which represents an authenticated Client. Required for requests such as [group.Group.rankUser].
 */
class RoavaClient {
    private val cookie: String
    internal val request: RoavaRequest

    val name: String
    val id: Long

    /**
     * Creates a new [RoavaClient] object with a provided Cookie
     *
     * <b>Sample</b>
     *
     * Java
     * ```
     * // This represents a client
     * Client client = new Client("Cookie");
     * ```
     *
     * @param[cookie] The .ROBLOSECURITY cookie.
     * @throws[RuntimeException]
     */
    @Throws(RuntimeException::class)
    constructor(cookie: String) {
        // Validation check
        if (!cookie.startsWith("_|WARNING:-")) {
            throw RuntimeException("Your cookie is not set properly! Please make sure that you include the entirety of the string, including the _|WARNING:-")
        }

        request = RoavaRequest(cookie)
        this.cookie = cookie

        try {
            // Make a request to get the current Client's information
            val profileData = request.createRequest(ProfileApi::class.java)
                .getProfileInfo()
                .execute()
                .body()

            name = profileData?.username!!
            id = profileData.id!!
        } catch(exception: Exception) {
            throw RuntimeException("Error occurred while authenticating! Your cookie might have been invalidated.")
        }
    }

    /**
     * Method to get the [RoavaClient] represented as a [User]
     *
     * @throws[RuntimeException]
     * @return[User]
     */
    @Throws(RuntimeException::class)
    fun getUser(): User {
        return User(id)
    }
}