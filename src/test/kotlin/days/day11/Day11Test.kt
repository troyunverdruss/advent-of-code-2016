package days.day11

import org.testng.annotations.Test
import kotlin.test.assertEquals

class Day11Test {
    @Test
    fun testExample1() {
        val exampleState = State(0,
                listOf(
                        setOf(Microchip("lithium"), Microchip("hydrogen")),
                        setOf(Generator("hydrogen")),
                        setOf(Generator("lithium")),
                        setOf()

                )
        )

        val result = run(exampleState)
        assertEquals(result, 11)
    }

    @Test
    fun `test one step from final solution in example`() {
        val exampleState = State(2,
                listOf(
                        setOf(),
                        setOf(),
                        setOf(Microchip("lithium"), Microchip("hydrogen")),
                        setOf(Generator("hydrogen"), Generator("lithium"))

                )
        )

        val result = run(exampleState)
        assertEquals(result, 1)
    }
}