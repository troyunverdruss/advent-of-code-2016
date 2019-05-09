package day04

import org.testng.Assert.*
import org.testng.annotations.Test

class Day04Test {

    @Test
    fun testExample1() {
        val room = parseRoomString("aaaaa-bbb-z-y-x-123[abxyz]")
        assertEquals(room.name, "aaaaa-bbb-z-y-x-")
        assertEquals(room.sectorId, 123)
        assertEquals(room.checksum, "abxyz")
        assertTrue(room.isReal())
    }

    @Test
    fun testExample2() {
        val room = parseRoomString("a-b-c-d-e-f-g-h-987[abcde]")
        assertEquals(room.name, "a-b-c-d-e-f-g-h-")
        assertEquals(room.sectorId, 987)
        assertEquals(room.checksum, "abcde")
        assertTrue(room.isReal())
    }

    @Test
    fun testExample3() {
        val room = parseRoomString("not-a-real-room-404[oarel]")
        assertEquals(room.name, "not-a-real-room-")
        assertEquals(room.sectorId, 404)
        assertEquals(room.checksum, "oarel")
        assertTrue(room.isReal())
    }

    @Test
    fun testExample4() {
        val room = parseRoomString("totally-real-room-200[decoy]")
        assertEquals(room.name, "totally-real-room-")
        assertEquals(room.sectorId, 200)
        assertEquals(room.checksum, "decoy")
        assertFalse(room.isReal())
    }
}