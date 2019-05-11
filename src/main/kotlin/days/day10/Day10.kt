package days.day10

import java.io.File
import java.util.LinkedList
import kotlin.IllegalStateException

interface Receiver {
    fun takeIn(value: Int)
}

data class Bot(val inputs: MutableList<Int> = mutableListOf(), var low: Receiver? = null, var high: Receiver? = null, val outputs: MutableList<Set<Int>> = mutableListOf(), val id: Int) : Receiver {
    override fun takeIn(value: Int) {
        inputs.add(value)
        if (inputs.size == 2) {
            val min = inputs.min() ?: throw IllegalStateException("No min")
            val max = inputs.max() ?: throw IllegalStateException("No max")
            inputs.clear()

            low?.takeIn(min) ?: throw IllegalStateException("No low target")
            high?.takeIn(max) ?: throw IllegalStateException("No high target")

            outputs.add(setOf(min, max))
        }
    }
}

data class OutputBin(val inputs: MutableList<Int> = mutableListOf(), val id: Int) : Receiver {
    override fun takeIn(value: Int) {
        inputs.add(value)
    }
}


data class ProcessResult(val bots: List<Bot>, val outputBins: List<OutputBin>)

data class ValuePair(val value: Int, val targetBot: Int)

enum class ReceiverType {
    BOT, OUTPUT_BIN;

    companion object {
        fun from(string: String): ReceiverType {
            return when (string) {
                "bot" -> BOT
                "output" -> OUTPUT_BIN
                else -> throw IllegalArgumentException("Unknown string type: $string")
            }
        }
    }
}

fun processInput(lines: List<String>): ProcessResult {
    val valueQueue = LinkedList<ValuePair>()
    val bots = mutableMapOf<Int, Bot>()
    val outputBins = mutableMapOf<Int, OutputBin>()

    lines.forEach { line ->
        Regex("value (\\d+) goes to bot (\\d+)").matchEntire(line)?.let { matchResult ->
            val value = matchResult.groups[1]?.value?.toInt() ?: throw IllegalStateException("Couldn't parse value")
            val botId = matchResult.groups[2]?.value?.toInt() ?: throw IllegalStateException("Couldn't parse bot ID")

            // Going to chain react all the values after we've made sure to create all the objects needed
            valueQueue.add(ValuePair(value, botId))

            createReceiverIfAbsent(bots, outputBins, botId, ReceiverType.BOT)
        }

        Regex("bot (\\d+) gives low to (.+) (\\d+) and high to (.+) (\\d+)").matchEntire(line)?.let { matchResult ->
            val botId = matchResult.groups[1]?.value?.toInt() ?: throw IllegalStateException("Couldn't parse bot id")
            val lowTypeString = matchResult.groups[2]?.value ?: throw IllegalStateException("Couldn't parse low type")
            val lowId = matchResult.groups[3]?.value?.toInt() ?: throw IllegalStateException("Couldn't parse low id")
            val highTypeString = matchResult.groups[4]?.value ?: throw IllegalStateException("Couldn't parse high type")
            val highId = matchResult.groups[5]?.value?.toInt() ?: throw IllegalStateException("Couldn't parse high id")

            val lowType = ReceiverType.from(lowTypeString)
            val highType = ReceiverType.from(highTypeString)

            createReceiverIfAbsent(bots, outputBins, botId, ReceiverType.BOT)
            createReceiverIfAbsent(bots, outputBins, lowId, lowType)
            createReceiverIfAbsent(bots, outputBins, highId, highType)

            when (lowType) {
                ReceiverType.BOT -> bots[botId]?.low = bots[lowId]
                ReceiverType.OUTPUT_BIN -> bots[botId]?.low = outputBins[lowId]
            }
            when (highType) {
                ReceiverType.BOT -> bots[botId]?.high = bots[highId]
                ReceiverType.OUTPUT_BIN -> bots[botId]?.high = outputBins[highId]
            }
        }
    }

    for (v in valueQueue) {
        bots[v.targetBot]?.takeIn(v.value)
    }

    return ProcessResult(bots.values.toList(), outputBins.values.toList())
}

private fun createReceiverIfAbsent(bots: MutableMap<Int, Bot>, outputBins: MutableMap<Int, OutputBin>, receiverId: Int, type: ReceiverType) {
    when (type) {
        ReceiverType.BOT -> {
            if (!bots.containsKey(receiverId)) {
                val bot = Bot(id = receiverId)
                bots[receiverId] = bot
            }
        }
        ReceiverType.OUTPUT_BIN -> {
            if (!outputBins.containsKey(receiverId)) {
                val outputBin = OutputBin(id = receiverId)
                outputBins[receiverId] = outputBin
            }
        }
    }
}

fun main() {
    val lines = mutableListOf<String>()
    File(ClassLoader.getSystemResource("input_d10.txt").file).forEachLine {
        lines.add(it)
    }

    val processResult = processInput(lines)

    val bot = processResult.bots.first { it.outputs.contains(setOf(17, 61)) }
    println("Part 1, the bot comparing (17, 61) has ID: ${bot.id}")

    val product = processResult.outputBins
            .filter { it.id in listOf(0, 1, 2) }
            .flatMap { it.inputs }
            .fold(1, {total, next -> total * next})

    println("Part 2, the product of values in bins 0, 1, and 2 is: $product")
}