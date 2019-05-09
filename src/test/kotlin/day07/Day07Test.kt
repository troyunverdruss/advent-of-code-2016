package day07

import org.testng.Assert.*
import org.testng.annotations.Test

class Day07Test {
    @Test
    fun testParseFirstExample() {
        val input = "abba[mnop]qrst"
        val address = parseString(input)

        assertEquals(listOf("abba", "qrst"), address.outsideBrackets)
        assertEquals(listOf("mnop"), address.insideBrackets)
    }

    @Test
    fun testStartingInsideBrackets() {
        val input = "[abc]abba[mnop]qrst"
        val address = parseString(input)

        assertEquals(listOf("abba", "qrst"), address.outsideBrackets)
        assertEquals(listOf("abc","mnop"), address.insideBrackets)
    }

    @Test
    fun testPart1Example1() {
        val input = "abba[mnop]qrst"
        val address = parseString(input)

        assertTrue(address.supportsTls())
    }

    @Test
    fun testPart1Example2() {
        val input = "abcd[bddb]xyyx"
        val address = parseString(input)

        assertFalse(address.supportsTls())
    }

    @Test
    fun testPart1Example3() {
        val input = "aaaa[qwer]tyui"
        val address = parseString(input)

        assertFalse(address.supportsTls())
    }

    @Test
    fun testPart1Example4() {
        val input = "ioxxoj[asdfgh]zxcvbn"
        val address = parseString(input)

        assertTrue(address.supportsTls())
    }

    @Test
    fun testPart2Example1() {
        val input = "aba[bab]xyz"
        val address = parseString(input)

        assertTrue(address.supportsSsl())
    }

    @Test
    fun testPart2Example2() {
        val input = "xyx[xyx]xyx"
        val address = parseString(input)

        assertFalse(address.supportsSsl())
    }

    @Test
    fun testPart2Example3() {
        val input = "aaa[kek]eke"
        val address = parseString(input)

        assertTrue(address.supportsSsl())
    }

    @Test
    fun testPart2Example4() {
        val input = "zazbz[bzb]cdb"
        val address = parseString(input)

        assertTrue(address.supportsSsl())
    }
}