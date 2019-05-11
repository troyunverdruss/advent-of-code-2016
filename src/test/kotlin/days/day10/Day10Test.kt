package days.day10

import org.testng.Assert.*
import org.testng.annotations.Test

class Day10Test {
    @Test
    fun testExample() {
        val input = """
            value 5 goes to bot 2
            bot 2 gives low to bot 1 and high to bot 0
            value 3 goes to bot 1
            bot 1 gives low to output 1 and high to bot 0
            bot 0 gives low to output 2 and high to output 0
            value 2 goes to bot 2
        """.trimIndent()

        val processResult = processInput(input.lines())

        val out0 = processResult.outputBins.first { it.id == 0 }
        val out1 = processResult.outputBins.first { it.id == 1 }
        val out2 = processResult.outputBins.first { it.id == 2 }

        assertEquals(out0.inputs, listOf(5))
        assertEquals(out1.inputs, listOf(2))
        assertEquals(out2.inputs, listOf(3))

        val bot = processResult.bots.first { it.outputs.contains(setOf(2, 5)) }
        assertEquals(bot.id, 2)

    }
}

