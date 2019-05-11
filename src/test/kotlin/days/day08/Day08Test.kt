package days.day08

import days.day08.Point
import days.day08.Screen
import days.day08.debugPrint
import org.testng.Assert.*
import org.testng.annotations.Test

class Day08Test {
    @Test
    fun testExample1() {
        val screen = Screen(7, 3)
        assertEquals(screen.countOfEnabledPixels(), 0)

        // rect 3x2
        screen.rect(3, 2)
        debugPrint(screen)
        assertEquals(screen.countOfEnabledPixels(), 6)
        println()

        // rotate column x=1 by 1
        screen.rotateColumn(1, 1)
        debugPrint(screen)
        assertEquals(screen.countOfEnabledPixels(), 6)
        assertFalse(screen.pixels[Point(1, 0)]!!.on)
        assertTrue(screen.pixels[Point(1, 1)]!!.on)
        assertTrue(screen.pixels[Point(1, 2)]!!.on)
        println()

        // rotate row y=0 by 4
        screen.rotateRow(0, 4)
        debugPrint(screen)
        assertEquals(screen.countOfEnabledPixels(), 6)
        assertFalse(screen.pixels[Point(0, 0)]!!.on)
        assertFalse(screen.pixels[Point(1, 0)]!!.on)
        assertFalse(screen.pixels[Point(2, 0)]!!.on)
        assertFalse(screen.pixels[Point(3, 0)]!!.on)
        assertTrue(screen.pixels[Point(4, 0)]!!.on)
        assertFalse(screen.pixels[Point(5, 0)]!!.on)
        assertTrue(screen.pixels[Point(6, 0)]!!.on)
        println()

        // rotate column x=1 by 1
        screen.rotateColumn(1, 1)
        debugPrint(screen)
        assertEquals(screen.countOfEnabledPixels(), 6)
        assertTrue(screen.pixels[Point(1, 0)]!!.on)
        assertFalse(screen.pixels[Point(1, 1)]!!.on)
        assertTrue(screen.pixels[Point(1, 2)]!!.on)
        println()
    }
}