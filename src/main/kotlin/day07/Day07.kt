package day07

import java.io.File
import java.lang.IllegalArgumentException

data class Address(val insideBrackets: List<String>, val outsideBrackets: List<String>) {
    fun supportsTls(): Boolean {
        var outside = false
        var inside = false

        for (string in outsideBrackets) {
            if (contains4CharPalindrome(string)) {
                outside = true
                break
            }
        }

        for (string in insideBrackets) {
            if (contains4CharPalindrome(string)) {
                inside = true
                break
            }
        }

        return outside && !inside
    }

    private fun contains4CharPalindrome(string: String): Boolean {
        string.forEachIndexed { index, char ->
            if (index + 3 <= string.lastIndex
                    && char == string[index + 3]
                    && char != string[index + 1]
                    && string[index + 1] == string[index + 2]) {
                return true
            }
        }
        return false
    }

    fun supportsSsl(): Boolean {
        val insidePalindromes = insideBrackets.map { get3CharPalindromes(it) }.flatten()
        val outsidePalindromes = outsideBrackets.map { get3CharPalindromes(it) }.flatten()

        for (string in insidePalindromes) {
            if (invert3CharPalindrom(string) in outsidePalindromes) {
                return true
            }
        }
        return false
    }

    private fun get3CharPalindromes(string: String): List<String> {
        val palindromes = mutableListOf<String>()
        string.forEachIndexed { index, char ->
            if (index + 2 <= string.lastIndex
                    && char == string[index + 2]
                    && char != string[index + 1]) {
                palindromes.add(string.substring(index, index + 3))
            }
        }

        return palindromes
    }

    private fun invert3CharPalindrom(string: String): String {
        if (string.length != 3) {
            throw IllegalArgumentException("String must be 3 chars long, instead got: $string")
        }
        if (string[0] != string[2] || string[0] == string[1]) {
            throw IllegalArgumentException("Not a proper 3-char palindrome: $string")
        }

        return "${string[1]}${string[0]}${string[1]}"
    }

}

fun parseString(input: String): Address {
    var inside = false

    val insideList = mutableListOf<String>()
    val outsideList = mutableListOf<String>()

    val sb = StringBuilder()
    input.forEach { char ->
        val brackets = listOf('[', ']')
        if (char in brackets) {
            appendString(inside, insideList, sb, outsideList)
            inside = !inside
            sb.clear()
        } else {
            sb.append(char)
        }
    }

    // And grab the final one
    appendString(inside, insideList, sb, outsideList)

    return Address(insideList, outsideList)
}

private fun appendString(inside: Boolean, insideList: MutableList<String>, sb: StringBuilder, outsideList: MutableList<String>) {
    if (sb.isNotEmpty()) {
        if (inside) {
            insideList.add(sb.toString())
        } else {
            outsideList.add(sb.toString())
        }
    }
}

fun main() {
    val addresses = mutableListOf<Address>()
    File(ClassLoader.getSystemResource("input_d7.txt").file).forEachLine {
        addresses.add(parseString(it))
    }

    val supportsTlsCount = addresses.filter { it.supportsTls() }.count()

    println("Part 1, $supportsTlsCount support TLS")

    val supportsSslCount = addresses.filter { it.supportsSsl() }.count()

    println("Part 2, $supportsSslCount support SSL")

}