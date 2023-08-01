package graphics

fun Int.range(value: Int) = if (this < value) this..value else value..this

fun linePrams(start: Point, end: Point): Pair<Float, Float> {
    val k = (start.y - end.y) / (start.x - end.x).toFloat()
    val b = start.y - k * start.x

    return Pair(k, b)
}

data class Point(val x: Int, val y: Int)