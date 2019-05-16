package days.day14

import org.testng.Assert.*
import org.testng.annotations.Test

class Day14Test {
    @Test
    fun `verify example 1`() {
        val input = "abc"

        val keys = part1(input, ::getHash)
        assertEquals(keys[0].index, 39)
        println(keys[0].hash)
        assertEquals(keys[1].index, 92)
        println(keys[1].hash)
        assertEquals(keys[63].index, 22728)
    }

    @Test
    fun `verify part 2 hashing algorithm`() {
        val input = "abc"

        val hash = getPart2Hash(input, 0)

        println(hash)
        assertTrue(hash.startsWith("a107ff"))
    }
}