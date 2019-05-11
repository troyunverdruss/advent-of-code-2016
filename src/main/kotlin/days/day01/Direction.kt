package days.day01

enum class Direction {
    NORTH, SOUTH, WEST, EAST;

    fun turn(dir: Turn): Direction {
        return when (dir) {
            Turn.LEFT -> when (this) {
                NORTH -> WEST
                SOUTH -> EAST
                WEST -> SOUTH
                EAST -> NORTH
            }
            Turn.RIGHT -> when (this) {
                NORTH -> EAST
                SOUTH -> WEST
                WEST -> NORTH
                EAST -> SOUTH
            }
        }
    }
}