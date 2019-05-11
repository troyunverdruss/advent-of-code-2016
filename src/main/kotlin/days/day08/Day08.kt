package days.day08

import java.io.File
import java.lang.IllegalStateException

data class Point(val x: Int, val y: Int)
data class Pixel(var on: Boolean = false)

class Screen(val width: Int, val height: Int) {
    val pixels = mutableMapOf<Point, Pixel>()

    init {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = Pixel()
                pixels[Point(x, y)] = pixel
            }
        }
    }

    fun rect(a: Int, b: Int) {
        for (x in 0 until a) {
            for (y in 0 until b) {
                pixels[Point(x, y)]?.on = true
            }
        }
    }

    fun rotateRow(a: Int, b: Int) {
        // Get a row of pixels
        val row = mutableListOf<Pixel>()
        for (x in 0 until width) {
            val pixel = pixels[Point(x, a)] ?: throw IllegalStateException("No pixel found at ($x, $a)")
            row.add(pixel)
        }

        // Now let's insert them back into the screen at the right spots
        row.forEachIndexed { index, pixel ->
            val xIndex = (index + b) % width
            pixels[Point(xIndex, a)] = pixel
        }
    }

    fun rotateColumn(a: Int, b: Int) {
        // Get a column of pixels
        val column = mutableListOf<Pixel>()
        for (y in 0 until height) {
            val pixel = pixels[Point(a, y)] ?: throw IllegalStateException("No pixel found at ($a, $y)")
            column.add(pixel)
        }

        // Now let's insert them back into the screen at the right spots
        column.forEachIndexed { index, pixel ->
            val yIndex = (index + b) % height
            pixels[Point(a, yIndex)] = pixel
        }
    }

    fun countOfEnabledPixels(): Int {
        return pixels.filter { it.value.on }.count()
    }
}

fun debugPrint(screen: Screen) {
    for (y in 0 until screen.height) {
        for (x in 0 until screen.width) {
            print(if (screen.pixels[Point(x, y)]!!.on) "#" else " ")
        }
        println()
    }
}

fun part1(lines: List<String>): Int {
    val screen = Screen(50, 6)

    lines.forEach { line ->
        Regex("rect (\\d+)x(\\d+)").matchEntire(line)?.let { matchResult ->
            screen.rect(matchResult.groups[1]?.value?.toInt()!!, matchResult.groups[2]?.value?.toInt()!!)
        }

        Regex("rotate row y=(\\d+) by (\\d+)").matchEntire(line)?.let { matchResult ->
            screen.rotateRow(matchResult.groups[1]?.value?.toInt()!!, matchResult.groups[2]?.value?.toInt()!!)
        }

        Regex("rotate column x=(\\d+) by (\\d+)").matchEntire(line)?.let { matchResult ->
            screen.rotateColumn(matchResult.groups[1]?.value?.toInt()!!, matchResult.groups[2]?.value?.toInt()!!)
        }
    }

    debugPrint(screen)

    return screen.countOfEnabledPixels()
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d8.txt").file).forEachLine {
        lines.add(it)
    }

    val part1Count = part1(lines)
    println("Part 2 ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
    println("Part 1, total enabled pixels: $part1Count")
}