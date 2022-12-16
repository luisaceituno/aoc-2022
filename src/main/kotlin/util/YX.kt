package util

import kotlin.math.abs

class YX(val y: Long, val x: Long) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as YX

        if (y != other.y) return false
        if (x != other.x) return false

        return true
    }

    override fun hashCode(): Int {
        var result = y.hashCode()
        result = 31 * result + x.hashCode()
        return result
    }

    override fun toString(): String {
        return "[y=$y, x=$x]"
    }

    fun manhattanDistanceTo(other: YX): Long {
        return abs(other.y - this.y) + abs(other.x - this.x)
    }

    fun move(up: Long = 0, right: Long = 0, down: Long = 0, left: Long = 0): YX {
        return YX(x - left + right, y - up + down)
    }
}