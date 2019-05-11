package days.day01

import java.lang.IllegalArgumentException

enum class Turn {
    LEFT, RIGHT;

    companion object {
        fun from(s: String) : Turn {
            return when (s.toUpperCase()) {
                "L" -> LEFT
                "R" -> RIGHT
                else -> throw IllegalArgumentException("Unknown direction: {}".format(s))
            }
        }
    }
}



