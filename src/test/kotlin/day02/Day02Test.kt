package day02

import org.testng.Assert.*
import org.testng.annotations.Test

class Day02Test {
    @Test
    fun testExample1() {
        val lines = mutableListOf(
                "ULL",
                "RRDDD",
                "LURDL",
                "UUUUD"
        )

        val result = processLines(lines)
        assertEquals(result, "1985")
    }

}