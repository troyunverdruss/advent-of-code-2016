package days.day14

import md5
import java.util.LinkedList
import kotlin.reflect.KFunction2

data class Details(val char: Char, val index: Int, val hash: String)

fun part1(input: String, hashMethod: KFunction2<String, Int, String>): List<Details> {
    val orderedDetails = linkedMapOf<Int, Details>()
    val detailsLookup = mutableMapOf<Char, MutableList<Details>>()
    val keys = mutableListOf<Details>()

    var i = 0
    while (keys.size < 64) {
        val hash = hashMethod(input, i)
        val triple = findFirstTriple(hash)

        pruneMaps(i, orderedDetails, detailsLookup)

        val fives = findQuintuples(hash)
        for (five in fives) {
            val toRemove = mutableListOf<Details>()
            detailsLookup[five]?.let { list ->
                for (details in list) {
                    keys.add(details)
                    toRemove.add(details)
                }
            }

            for (details in toRemove) {
                orderedDetails.remove(details.index)
                detailsLookup[details.char]?.remove(details)
            }
        }

        triple?.let { char ->
            val details = Details(char, i, hash)
            orderedDetails[i] = details

            initKey(char, detailsLookup)
            detailsLookup[char]?.add(details)
        }

        i += 1
    }

    return keys.sortedBy { it.index }.toList()
}

fun getHash(input: String, i: Int) = (input + i).md5()

fun getPart2Hash(input: String, i: Int): String {
    var hash = getHash(input, i)
    for (i in 0 until 2016) {
        hash = hash.md5()
    }
    return hash
}

private fun initKey(char: Char, detailsLookup: MutableMap<Char, MutableList<Details>>) {
    if (char !in detailsLookup) {
        detailsLookup[char] = LinkedList<Details>()
    }
}

private fun pruneMaps(index: Int, orderedDetails: LinkedHashMap<Int, Details>, detailsLookup: MutableMap<Char, MutableList<Details>>) {
    val toRemove = mutableListOf<Details>()
    for (entry in orderedDetails) {
        if (entry.key < index - 1000) {
            toRemove.add(entry.value)
        } else {
            break
        }
    }

    for (details in toRemove) {
        orderedDetails.remove(details.index)
        detailsLookup[details.char]?.remove(details)
    }
}

fun findFirstTriple(hash: String): Char? {

    hash.forEachIndexed { index, char ->
        if (index + 2 <= hash.lastIndex) {
            if (char == hash[index + 1] && char == hash[index + 2]) {
                return char
            }
        }
    }

    // No triple found
    return null
}

fun findQuintuples(hash: String): List<Char> {
    val found = mutableListOf<Char>()
    hash.forEachIndexed { index, char ->
        if (index + 4 <= hash.lastIndex) {
            if (char == hash[index + 1] && char == hash[index + 2] && char == hash[index + 3] && char == hash[index + 4]) {
                // Only add if it hasn't been found already
                if (char !in found) {
                    found.add(char)
                }
            }
        }
    }

    return found
}

fun main() {
    val input = "cuanljph"

    val keys1 = part1(input, ::getHash)
    println("Part 1, last index: ${keys1[63].index}")

    val keys2 = part1(input, ::getPart2Hash)
    println("Part 2, last index: ${keys2[63].index}")
}

