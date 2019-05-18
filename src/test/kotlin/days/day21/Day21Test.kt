package days.day21

import org.testng.Assert.*
import org.testng.annotations.Test

class Day21Test {
    @Test
    fun `test rotate right`() {
        val result = RotateRightInstruction(1).process("abcd".toMutableList())
        assertEquals(result, "dabc".toMutableList())
    }

    @Test
    fun `test rotate left`() {
        val result = RotateLeftInstruction(1).process("abcd".toMutableList())
        assertEquals(result, "bcda".toMutableList())
    }

    @Test
    fun `test example 1`() {
        val input = """
            swap position 4 with position 0
            swap letter d with letter b
            reverse positions 0 through 4
            rotate left 1 step
            move position 1 to position 4
            move position 3 to position 0
            rotate based on position of letter b
            rotate based on position of letter d
        """.trimIndent()

        val scrambler = Scrambler(input.lines())
        val result = scrambler.scramble("abcde")
        assertEquals(result, "decab")
    }

    @Test
    fun `test reverse swap position`() {
        val instruction = SwapPositionInstruction(1, 3)
        val start = "abcde"

        val forward = instruction.process(start.toMutableList())
        val reverse = instruction.reverse(forward.toMutableList())

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse swap letter position`() {
        val instruction = SwapLetterInstruction('b', 'd')
        val start = "abcde"

        val forward = instruction.process(start.toMutableList())
        val reverse = instruction.reverse(forward.toMutableList())

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse rotate right`() {
        val instruction = RotateRightInstruction(1)
        val start = "abcde"

        val forward = instruction.process(start.toMutableList())
        val reverse = instruction.reverse(forward.toMutableList())

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse rotate left`() {
        val instruction = RotateLeftInstruction(1)
        val start = "abcde"
        println(start)

        val forward = instruction.process(start.toMutableList())
        val reverse = instruction.reverse(forward.toMutableList())

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse rotate by letter less than index 4 that ends up less than 4 after rotation`() {
        val instruction = RotateByLetterInstruction('a')
        val start = "abcdefgh"
        println(start)

        val forward = instruction.process(start.toMutableList())
        println(forward.joinToString(""))
        val reverse = instruction.reverse(forward.toMutableList())
        println(reverse.joinToString(""))

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse rotate by letter less than index 4 that ends up higher than 4 after rotation`() {
        val instruction = RotateByLetterInstruction('b')
        val start = "abcdefgh"
        println(start)

        val forward = instruction.process(start.toMutableList())
        println(forward.joinToString(""))
        val reverse = instruction.reverse(forward.toMutableList())
        println(reverse.joinToString(""))

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse rotate by letter more than index 4 that ends up less than 4`() {
        val instruction = RotateByLetterInstruction('e')
        val start = "abcdefgh"
        println(start)

        val forward = instruction.process(start.toMutableList())
        println(forward.joinToString(""))
        val reverse = instruction.reverse(forward.toMutableList())
        println(reverse.joinToString(""))

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse rotate by letter more than index 4 that ends up more than 4`() {
        val instruction = RotateByLetterInstruction('f')
        val start = "abcdefghijklmnopqrstuv"
        println(start)

        val forward = instruction.process(start.toMutableList())
        println(forward.joinToString(""))
        val reverse = instruction.reverse(forward.toMutableList())
        println(reverse.joinToString(""))

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse reverse`() {
        val instruction = ReverseInstruction(1,3)
        val start = "abcde"

        val forward = instruction.process(start.toMutableList())
        val reverse = instruction.reverse(forward.toMutableList())

        assertEquals(reverse, start.toMutableList())
    }

    @Test
    fun `test reverse move`() {
        val instruction = MoveInstruction(1, 3)
        val start = "abcde"

        val forward = instruction.process(start.toMutableList())
        val reverse = instruction.reverse(forward.toMutableList())

        assertEquals(reverse, start.toMutableList())
    }

}