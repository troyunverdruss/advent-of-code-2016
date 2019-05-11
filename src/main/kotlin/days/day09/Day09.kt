package days.day09

import java.io.File
import java.util.LinkedList

fun parse(input: String, recurse: Boolean = false, returnOnlyCount: Boolean = false): String {
    val queue = input.map { it }.toCollection(LinkedList())

    val decompressedString = StringBuilder()
    var decompressedStringLength = 0L

    while (queue.isNotEmpty()) {
        var c = queue.removeAt(0)

        if (c == '(') {
            // Do decompression processing
            val charCount = StringBuilder()
            val repeatCount = StringBuilder()
            val repeatChars = StringBuilder()

            var foundX = false
            c = queue.removeAt(0)
            while (c != ')') {
                if (c == 'x') {
                    // Need to know which one we're appending to
                    foundX = true
                } else {
                    // If we haven't hit the x yet, we need to tally up char count
                    // otherwise we're tallying up repeat count
                    if (!foundX) {
                        charCount.append(c)
                    } else {
                        repeatCount.append(c)
                    }
                }
                c = queue.removeAt(0)
            }

            for (i in 1..charCount.toString().toInt()) {
                c = queue.removeAt(0)
                repeatChars.append(c)
            }

            val chunk = StringBuilder()
            for (i in 1..repeatCount.toString().toInt()) {
                chunk.append(repeatChars)
            }

            if (recurse) {
                queue.addAll(0, chunk.toList())
            } else {
                if (returnOnlyCount) decompressedStringLength += chunk.length else decompressedString.append(chunk.toString())
            }

        } else {
            if (returnOnlyCount) decompressedStringLength += 1 else decompressedString.append(c)
        }
    }

    return if (returnOnlyCount) {
        decompressedStringLength.toString()
    } else {
        decompressedString.toString()
    }
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d9.txt").file).forEachLine {
        lines.add(it)
    }

    val decompressed = parse(lines[0])
    println("Part 1, decompressed length: ${decompressed.length}")

    val decompressedLength = parse(lines[0], recurse = true, returnOnlyCount = true)
    println("Part 2, decompressed recursively length: $decompressedLength")
}