package day04

import java.io.File

data class Room(val name: String, val sectorId: Int, val checksum: String) {
    fun isReal(): Boolean {
        val computedChecksum = name.filter { it != '-' }
                .groupingBy { it }
                .eachCount()
                .toList()
                .sortedWith(compareBy({ -it.second }, { it.first }))
                .toMap()
                .keys
                .joinToString(separator = "")
                .substring(0..4)
//        println(computedChecksum)
        return computedChecksum == checksum
    }
}

fun parseRoomString(input: String): Room {
    val matchResult = Regex("(.*-)(\\d+)\\[(.*)]").matchEntire(input)

    return Room(
            matchResult?.groups?.get(1)?.value.toString(),
            matchResult?.groups?.get(2)?.value?.toInt() ?: 0,
            matchResult?.groups?.get(3)?.value.toString()
    )
}

fun sumValidRoomSectorIds(rooms: List<Room>): Int {
    var sum = 0

    rooms.forEach { room ->
        if (room.isReal()) sum += room.sectorId
    }

    return sum
}

fun main() {
    val rooms = mutableListOf<Room>()
    File(ClassLoader.getSystemResource("input_d4.txt").file).forEachLine {
        rooms.add(parseRoomString(it))
    }

    val count = sumValidRoomSectorIds(rooms)
    println("Part 1, sum of valid rooms' sector IDs: $count")
}
