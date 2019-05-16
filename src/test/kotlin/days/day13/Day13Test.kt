package days.day13

import org.testng.Assert.*
import org.testng.annotations.Test

class Day13Test {
    @Test
    fun `verify example 1`() {
        val shortestDistance = part1(10, Point(7, 4))
        assertEquals(shortestDistance, 11)
    }
}