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

class Shifter(rot: Int = 0) {
    private val letterMap: Map<Char, Char>

    init {
        val letterOrder = "abcdefghijklmnopqrstuvwxyz"
        val initLetterMap = mutableMapOf<Char, Char>()

        for (i in letterOrder.indices) {
            initLetterMap[letterOrder[i]] = letterOrder[(i + rot) % letterOrder.length]
        }

        letterMap = initLetterMap
    }

    fun shift(input: String): String {
        val sb = StringBuilder(input.length)
        for (c in input) {
            if (c in letterMap) {
                sb.append(letterMap[c])
            } else {
                sb.append(" ")
            }
        }
        return sb.toString()
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

fun findNorthPoleStorage(rooms: List<Room>): Int {
    rooms.forEach { room ->
        if (room.isReal()) {
            val shifter = Shifter(room.sectorId)
            val decoded = shifter.shift(room.name)

            if ("north" in decoded && "pole" in decoded && "storage " in decoded) {
                return room.sectorId
            }
        }
    }

    throw IllegalStateException("Could not find north pole storage")
}

fun main() {
    val rooms = mutableListOf<Room>()
    File(ClassLoader.getSystemResource("input_d4.txt").file).forEachLine {
        rooms.add(parseRoomString(it))
    }

    val count = sumValidRoomSectorIds(rooms)
    println("Part 1, sum of valid rooms' sector IDs: $count")

    val id = findNorthPoleStorage(rooms)
    println("Part 2, room with north pole storage: $id")
}
