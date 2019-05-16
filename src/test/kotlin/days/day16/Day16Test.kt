package days.day16

import org.testng.Assert.*
import org.testng.annotations.Test

class Day16Test {
    @Test
    fun `verify grow`() {
        assertEquals(growData("1", 3), "100")
        assertEquals(growData("0", 3), "001")
        assertEquals(growData("11111", 11), "11111000000")
        assertEquals(growData("111100001010", 25), "1111000010100101011110000")
    }

    @Test
    fun `verify checksum`() {
        val data = "110010110100"
        val checksum = checksum(data)
        assertEquals(checksum, "100")
    }

    @Test
    fun `verify example 1`() {
        val data = growData("10000", 20)
        val checksum = checksum(data)
        assertEquals(checksum, "01100")
    }
}