package days.day16

fun growData(seed: String, targetLength: Int): String {
    var data = seed
    while (data.length < targetLength) {
        data = data + "0" + data.reversed()
                .replace('0', 'x')
                .replace('1', '0')
                .replace('x', '1')
    }
    return data.substring(0 until targetLength)
}

fun checksum(data: String): String {
    var compressedData = compressData(data)
    while (compressedData.length % 2 == 0) {
        compressedData = compressData(compressedData)
    }
    return compressedData
}

private fun compressData(data: String):String {
    val sb = StringBuilder()
    for (i in 0..data.lastIndex step 2) {
        if (data[i] == data[i + 1]) {
            sb.append(1)
        } else {
            sb.append(0)
        }
    }
    return sb.toString()
}

fun main() {
    val input = "11110010111001001"

    val checksum1 = run(input, 272)
    println("Part 1, checksum for input $input grown to length 272: $checksum1")

    val checksum2 = run(input, 35651584)
    println("Part 2, checksum for input $input grown to length 35651584: $checksum2")
}

private fun run(input: String, length: Int): String {
    val data = growData(input, length)
    val checksum = checksum(data)
    return checksum
}