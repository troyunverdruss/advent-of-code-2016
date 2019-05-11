package days.day05

import java.math.BigInteger
import java.security.MessageDigest

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun main() {

    val seed = "ffykfhsq"
    val part1Password = StringBuilder()
    val part2Password = Array<String?>(8) { null }

    var i = 0
    while (part1Password.length < 8 || part2Password.filterNotNull().size < 8) {
        val hash = (seed + i).md5()
        if (hash.substring(0, 5) == "00000") {
            println("$i: $hash")

            if (part1Password.length < 8) {
                part1Password.append(hash[5])
            }

            val index = hash[5].toString().toIntOrNull()
            val value = hash[6]

            index?.let {
                if (index in 0..7 && part2Password[index] == null) {
                    part2Password[index] = value.toString()
                }
            }
        }
        i += 1
    }

    println("Part 1 password: $part1Password")
    println("Part 2 password: ${part2Password.joinToString("")}")
}
