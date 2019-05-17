package days.day20

import org.testng.Assert.*
import org.testng.annotations.Test

class Day20Test {
    @Test
    fun `part 2 test with example data`() {
        val input = """
            5-8
            0-2
            4-7
        """.trimIndent()

        val ranges = parseRanges(input.lines())
        val totalOpen = run2(ranges, 9)

        assertEquals(totalOpen, 2)
    }

    @Test
    fun `part 2, more complicated test`() {
        val input = """
            0-25
            20-50
            60-90
            65-80
            99-100
        """.trimIndent()

        val ranges = parseRanges(input.lines())
        val totalOpen = run2(ranges, 100)


        assertEquals(totalOpen, 17)
    }
}