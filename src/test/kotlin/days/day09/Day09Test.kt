package days.day09

import org.testng.Assert.*
import org.testng.annotations.Test

class Day09Test {
    @Test
    fun testExample1() {
        val decompressed = parse("ADVENT")
        assertEquals(decompressed, "ADVENT")
    }

    @Test
    fun testExample2() {
        val decompressed = parse("A(1x5)BC")
        assertEquals(decompressed, "ABBBBBC")
    }

    @Test
    fun testExample3() {
        val decompressed = parse("(3x3)XYZ")
        assertEquals(decompressed, "XYZXYZXYZ")
    }

    @Test
    fun testExample4() {
        val decompressed = parse("A(2x2)BCD(2x2)EFG")
        assertEquals(decompressed, "ABCBCDEFEFG")
    }

    @Test
    fun testExample5() {
        val decompressed = parse("(6x1)(1x3)A")
        assertEquals(decompressed, "(1x3)A")
    }

    @Test
    fun testExample6() {
        val decompressed = parse("X(8x2)(3x3)ABCY")
        assertEquals(decompressed, "X(3x3)ABC(3x3)ABCY")
    }

    @Test
    fun testExample1Part2() {
        val decompressed = parse("(3x3)XYZ", recurse = true)
        assertEquals(decompressed, "XYZXYZXYZ")
    }

    @Test
    fun testExample2Part2() {
        val decompressed = parse("X(8x2)(3x3)ABCY", recurse = true)
        assertEquals(decompressed, "XABCABCABCABCABCABCY")
    }

    @Test
    fun testExample3Part2() {
        val decompressed = parse("(27x12)(20x12)(13x14)(7x10)(1x12)A", recurse = true, returnOnlyCount = true)
        assertEquals(decompressed.toInt(), 241920)
    }

    @Test
    fun testExample4Part2() {
        val decompressed = parse("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN", recurse = true, returnOnlyCount = true)
        assertEquals(decompressed.toInt(), 445)
    }

}
