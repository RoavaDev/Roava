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

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GroupTest {
    private val testGroup = Group(15771240)

    @Test
    fun testGroupRejectInvalid() {
        val executable: () -> Unit = {
            Group(100000000)
        }

        assertThrows(RuntimeException::class.java, executable)
    }

    @Test
    fun testGetRoles() {
        val roles = testGroup.getRoles()

        assertEquals(roles[1].name, "Member")
    }

    @Test
    fun testGetRoleByName() {
        val role = testGroup.getRole("Member")

        assertEquals(role.id, 88561132)
    }

    @Test
    fun testGetRoleByNameReject() {
        val executable: () -> Unit = {
            testGroup.getRole("Test")
        }

        assertThrows(RuntimeException::class.java, executable)
    }

    @Test
    fun testGetRoleById() {
        val role = testGroup.getRole(88561132)

        assertEquals(role.name, "Member")
    }

    @Test
    fun testGetRoleByIdRejectGroup() {
        val executable: () -> Unit = {
            testGroup.getRole(260)
        }

        assertThrows(RuntimeException::class.java, executable)
    }

    @Test
    fun testGetRoleByIdRejectInvalid() {
        val executable: () -> Unit = {
            testGroup.getRole(9090909)
        }

        assertThrows(RuntimeException::class.java, executable)
    }

    @Test
    fun testGetRoleByRank() {
        val role = testGroup.getRole(1)

        assertEquals(role.id, 88561132)
    }

    @Test
    fun testRoleByRankRejectInvalid() {
        val executable: () -> Unit = {
            testGroup.getRole(5)
        }

        assertThrows(RuntimeException::class.java, executable)
    }

    @Test
    fun testGetUserRank() {
        val role = testGroup.getUserRank(3838771115)

        assertEquals(role.id, 88561132)
    }

    @Test
    fun testGetUserRankRejectInvalid() {
        val executable: () -> Unit = {
            testGroup.getUserRank(1)
        }

        assertThrows(RuntimeException::class.java, executable)
    }
}