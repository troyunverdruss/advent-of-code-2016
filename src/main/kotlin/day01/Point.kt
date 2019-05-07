package day01

data class Point(val x: Int = 0, val y: Int = 0) {
    fun move(direction: Direction, distance: Int): Point {
        return when (direction) {
            Direction.NORTH -> Point(this.x, this.y + distance)
            Direction.SOUTH -> Point(this.x, this.y - distance)
            Direction.WEST -> Point(this.x - distance, this.y)
            Direction.EAST -> Point(this.x + distance, this.y)
        }
    }
}