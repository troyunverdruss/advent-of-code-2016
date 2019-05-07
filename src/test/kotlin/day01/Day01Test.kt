package day01

import org.testng.Assert.*
import org.testng.annotations.Test

class Day01Test {
    @Test
    fun testTurning() {
        assertEquals(Direction.NORTH.turn(Turn.LEFT), Direction.WEST)
        assertEquals(Direction.NORTH.turn(Turn.RIGHT), Direction.EAST)
    }

    @Test
    fun testExample1() {
        val moves = listOf("R2", "L3")
        val finalPoint = processDirections(moves)
        assertEquals(finalPoint, Point(2, 3))
        assertEquals(manhattanDistance(Point(), finalPoint), 5)
    }

    @Test
    fun testExample2() {
        val moves = listOf("R2", "R2", "R2")
        val finalPoint = processDirections(moves)
        assertEquals(finalPoint, Point(0, -2))
        assertEquals(manhattanDistance(Point(), finalPoint), 2)
    }

    @Test
    fun testExample3() {
        val moves = listOf("R5", "L5", "R5", "R3")
        val finalPoint = processDirections(moves)
        assertEquals(manhattanDistance(Point(), finalPoint), 12)
    }

    @Test
    fun testPart2Example() {
        val moves = listOf("R8", "R4", "R4", "R8")
        val finalPoint = processDirections(moves, true)
        assertEquals(finalPoint, Point(4, 0))
        assertEquals(manhattanDistance(Point(), finalPoint), 4)
    }
}
