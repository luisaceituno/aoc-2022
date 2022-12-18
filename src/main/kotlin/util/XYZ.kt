package util

data class XYZ(val x: Long, val y: Long, val z: Long) {
    /**
     * Assuming this object represents a 1x1x1 cube, returns the positions of all cubes sharing a face with this one
     */
    fun getFaceAdjacent(): Set<XYZ> {
        return hashSetOf(move(x = 1), move(x = -1), move(y = 1), move(y = -1), move(z = 1), move(z = -1))
    }

    /**
     * Assuming this object represents a 1x1x1 cube, returns the positions of all cubes sharing a corner with this one
     */
    fun getCornerAdjacent(): Set<XYZ> {
        return hashSetOf(
            move(1, 1, 1), move(1, 1, -1), move(1, -1, 1), move(1, -1, -1),
            move(-1, 1, 1), move(-1, 1, -1), move(-1, -1, 1), move(-1, -1, -1)
        )
    }

    /**
     * Assuming this object represents a 1x1x1 cube, returns the positions of all cubes sharing an edge with this one
     */
    fun getEdgeAdjacent(): Set<XYZ> {
        return hashSetOf(
            move(x = 1, y = 1), move(x = 1, y = -1), move(x = 1, z = 1), move(x = 1, z = -1),
            move(x = -1, y = 1), move(x = -1, y = -1), move(x = -1, z = 1), move(x = -1, z = -1),
            move(y = 1, z = 1), move(y = 1, z = -1), move(y = -1, z = 1), move(y = -1, z = -1)
        )
    }

    fun getAllAdjacent(): Set<XYZ> {
        return getFaceAdjacent() + getCornerAdjacent() + getEdgeAdjacent()
    }

    fun move(x: Long = 0, y: Long = 0, z: Long = 0): XYZ {
        return XYZ(this.x + x, this.y + y, this.z + z)
    }
}