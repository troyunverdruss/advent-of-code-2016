package days.day11

import org.testng.Assert.*
import org.testng.annotations.Test

class StateTest {
    @Test
    fun `detects valid end state`() {
        val state = State(3, listOf(
                setOf(),
                setOf(),
                setOf(),
                setOf(Generator("a"), Microchip("a"))))

        assertTrue(state.isEndState())
        assertTrue(state.isValid())
    }

    @Test
    fun `detects not end state`() {
        val state = State(3, listOf(
                setOf(Generator("a")),
                setOf(),
                setOf(),
                setOf(Microchip("a"))))

        assertFalse(state.isEndState())
        assertTrue(state.isValid())
    }

    @Test
    fun `detects invalid floor`() {
        val state = State(3, listOf(
                setOf(),
                setOf(Microchip("a")),
                setOf(Generator("b")),
                setOf(Generator("a"), Microchip("b"))))

        assertFalse(state.isValid())
    }

    @Test
    fun `detects valid floors`() {
        val state = State(3, listOf(
                setOf(),
                setOf(Generator("a"), Microchip("a"), Generator("b")),
                setOf(),
                setOf(Microchip("b"))))

        assertTrue(state.isValid())
    }

    @Test
    fun `simple computes next states when on bottom`() {
        val state = State(0, listOf(
                setOf(Generator("a")),
                setOf(),
                setOf(),
                setOf()))

        val nextStates = state.nextStates()
        assertEquals(nextStates.size, 1)
        assertTrue(nextStates.contains(
                State(1, listOf(
                        setOf(),
                        setOf(Generator("a")),
                        setOf(),
                        setOf()))
        ))
    }

    @Test
    fun `simple computes next states when on top`() {
        val state = State(3, listOf(
                setOf(),
                setOf(),
                setOf(),
                setOf(Generator("a"))))

        val nextStates = state.nextStates()
        assertEquals(nextStates.size, 1)
        assertTrue(nextStates.contains(
                State(2, listOf(
                        setOf(),
                        setOf(),
                        setOf(Generator("a")),
                        setOf()))
        ))
    }

    @Test
    fun `simple computes next states goes both ways`() {
        val state = State(1, listOf(
                setOf(),
                setOf(Generator("a")),
                setOf(),
                setOf()))

        val nextStates = state.nextStates()
        assertEquals(nextStates.size, 2)
        assertTrue(nextStates.contains(
                State(2, listOf(
                        setOf(),
                        setOf(),
                        setOf(Generator("a")),
                        setOf()))
        ))
        assertTrue(nextStates.contains(
                State(0, listOf(
                        setOf(Generator("a")),
                        setOf(),
                        setOf(),
                        setOf()))
        ))
    }

    @Test
    fun `computes next states with 3 items`() {
        val state = State(0, listOf(
                setOf(Generator("a"), Microchip("a"), Generator("b")),
                setOf(),
                setOf(),
                setOf()))

        val nextStates = state.nextStates()
        assertEquals(nextStates.size, 6)
        // All the individual moves
        assertTrue(nextStates.contains(
                State(1, listOf(
                        setOf(Microchip("a"), Generator("b")),
                        setOf(Generator("a")),
                        setOf(),
                        setOf()))
        ))
        assertTrue(nextStates.contains(
                State(1, listOf(
                        setOf(Generator("a"), Generator("b")),
                        setOf(Microchip("a")),
                        setOf(),
                        setOf()))
        ))
        assertTrue(nextStates.contains(
                State(1, listOf(
                        setOf(Generator("a"), Microchip("a")),
                        setOf(Generator("b")),
                        setOf(),
                        setOf()))
        ))

        // All the pair moves
        assertTrue(nextStates.contains(
                State(1, listOf(
                        setOf(Generator("b")),
                        setOf(Generator("a"), Microchip("a")),
                        setOf(),
                        setOf()))
        ))
        assertTrue(nextStates.contains(
                State(1, listOf(
                        setOf(Microchip("a")),
                        setOf(Generator("a"), Generator("b")),
                        setOf(),
                        setOf()))
        ))
        assertTrue(nextStates.contains(
                State(1, listOf(
                        setOf(Generator("a")),
                        setOf(Microchip("a"), Generator("b")),
                        setOf(),
                        setOf()))
        ))
    }
}