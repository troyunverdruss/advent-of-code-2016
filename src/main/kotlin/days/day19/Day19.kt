package days.day19

import java.util.LinkedList

data class Elf(val id: Int, val gifts: Int = 1)

fun run1(input: Int): Int {
    val queue = LinkedList<Elf>()

    for (i in 1..input) {
        queue.add(Elf(i))
    }

    while (queue.size > 1) {
        val thief = queue.poll()
        val sucker = queue.poll()
        thief.gifts + sucker.gifts
        queue.add(thief)
    }

    val lastElf = queue.first
    return lastElf.id
}

fun run2(input: Int): Int {
    val front = LinkedList<Elf>()
    val back = LinkedList<Elf>()

    for (i in 1..input / 2 + 1) {
        front.add(Elf(i))
    }
    for (i in (input / 2 + 2)..input) {
        back.add(Elf(i))
    }

    while (front.size + back.size > 1) {
        rebalance(front, back)
        val thief = front.poll()
        val sucker = when {
            front.size == back.size -> front.pollLast()
            front.size > back.size -> front.pollLast()
            else -> back.pollFirst()
        }

//        println("${thief.id} -> ${sucker.id}")
        thief.gifts + sucker.gifts
        back.add(thief)
    }

    return (front + back).first().id
}

private fun rebalance(front: LinkedList<Elf>, back: LinkedList<Elf>) {
    while (!isBalancedEnough(front.size, back.size)) {
        front.add(back.poll())
    }
}

private fun isBalancedEnough(frontSize: Int, backSize: Int): Boolean {
    return if (frontSize == backSize) {
        true
    } else frontSize == backSize + 1
}

fun main() {
    val input = 3014603
    val lastElfId = run1(input)
    println("Part 1, last elf ID: $lastElfId")

    val lastElfId2 = run2(input)
    println("Part 2, last elf ID: $lastElfId2")
}