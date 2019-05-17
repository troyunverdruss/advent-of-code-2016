package days.day19

import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class Day19Test {
    @Test
    fun `verify example 2`() {
        val result = run2(5)
        assertEquals(result, 2)
    }

    @Test
    fun `stealing from 10`() {
        val result = run2(9)
        assertEquals(result, 9)
    }
}