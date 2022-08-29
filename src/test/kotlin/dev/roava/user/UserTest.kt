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

package user

import dev.roava.user.User
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UserTest {
    private val testUser = User(3838771115)

    @Test
    fun testCreateUserRejectInvalid() {
        val executable: () -> Unit = {
            User(20000000000)
        }

        assertThrows(RuntimeException::class.java, executable)
    }

    @Test
    fun testCreateUserWithName() {
        assertEquals(testUser.id, User("TestRoavaCoverage").id)
    }

    @Test
    fun testDisplayName() {
        assertEquals(testUser.displayName, "Roava")
    }

    @Test
    fun testName() {
        assertEquals(testUser.name, "TestRoavaCoverage")
    }

    @Test
    fun testId() {
        assertEquals(testUser.id, 3838771115)
    }

    @Test
    fun testFriends() {
        val friends = testUser.getFriends()

        assertEquals(friends[0].id, 72242614.toLong())

    }

    @Test
    fun getGroups() {
        val groups = testUser.getGroups()

        assertEquals(groups[0].id, 15771240)
    }

    @Test
    fun isInGroup() {
        assertTrue(testUser.isInGroup(15771240))
    }
}