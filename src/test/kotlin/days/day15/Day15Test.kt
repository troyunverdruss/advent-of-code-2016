package days.day15

import org.testng.Assert.*
import org.testng.annotations.Test

class Day15Test {
    @Test
    fun `verify example 1`() {
        val input = """
            Disc #1 has 5 positions; at time=0, it is at position 4.
            Disc #2 has 2 positions; at time=0, it is at position 1.
        """.trimIndent()

        val discs = parseInput(input.lines())

        assertEquals(discs[0].slotAt(0), 0)
        assertEquals(discs[1].slotAt(0), 1)

        val time = run(discs)
        assertEquals(time, 5)
    }
}