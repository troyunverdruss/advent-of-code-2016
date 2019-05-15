package days.day12

import org.testng.Assert.*
import org.testng.annotations.Test

class ProcessorTest {
    @Test
    fun testExample1() {
        val input = """
            cpy 41 a
            inc a
            inc a
            dec a
            jnz a 2
            dec a
        """.trimIndent()

        val processor = Processor(input.lines())
        processor.run()
        assertEquals(processor.registers["a"], 42)
    }

    @Test
    fun `test jump forward`() {
        val input = """
            cpy 1 a
            jnz a 2
            inc a
            dec a
        """.trimIndent()

        val processor = Processor(input.lines())
        processor.run()
        assertEquals(processor.registers["a"], 0)
    }

    @Test
    fun `test jump backward`() {
        val input = """
            cpy 1 a
            inc a
            jnz a -1

        """.trimIndent()

        val processor = Processor(input.lines())
        processor.run()
        assertEquals(processor.registers["a"], 0)
    }

}