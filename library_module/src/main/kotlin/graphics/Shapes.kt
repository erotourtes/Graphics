package graphics

import kotlin.math.pow

class Shapes(private val canvas: Canvas) {
    var color = Color(0xFF0000FFu)

    fun drawCircle(center: Point, radius: Int) {
        val cy = center.y
        val cx = center.x
        for (y in cy - radius - 1..cy + radius) {
            for (x in cx - radius - 1..cx + radius) {
                val dx = x - cx
                val dy = y - cy

                val innerCircleRadius = (radius - 2).coerceAtLeast(0)
                if (dx * dx + dy * dy <= innerCircleRadius*innerCircleRadius) { // inner should be filled
                    canvas.writeAt(x, y, color)
                } else if (dx * dx + dy * dy <= (radius + 2) * (radius + 2)) { // on the edge should be calculated
                    val antialiasFraction =
                        antialias(dx, dy, 2) { xf, yf ->
                            xf * xf + yf * yf <= (radius.toDouble() + 0.5).pow(2) // top of the pixel is in (0,0) of subpixel
                        }

                    val newAlpha = color.alpha.toDouble() * antialiasFraction
                    canvas.writeAt(x, y, color.builder().alpha(newAlpha.toUInt()).build())
                }
            }
        }
    }

    private inline fun antialias(x: Int, y: Int, precision: Int, formula: (Double, Double) -> Boolean): Double {
        var count = 0
        val gaps = precision - 1

        for (subY in 0..<precision) {
            for (subX in 0..<precision) {
                val partOfPixelX = subX / gaps.toDouble()
                val partOfPixelY = subY / gaps.toDouble()
                val higherX = x.toFloat() + partOfPixelX
                val higherY = y.toFloat() + partOfPixelY
                if (formula(higherX, higherY)) count++
            }
        }

        return count / precision.toDouble().pow(2)
    }

    fun drawLine(start: Point, end: Point) {
        val isVertical = start.x == end.x
        if (isVertical) {
            for (y in start.y.range(end.y))
                canvas.writeAt(start.x, y, color)
            return
        }

        val (k, b) = linePrams(start, end)

        for (x in start.x.range(end.x)) {
            val firstY = k * x + b
            val lastY = k * (x + 1) + b

            var y = firstY.toInt()
            while (y <= lastY) {
                canvas.writeAt(x, y, color)
                y++
            }
        }
    }

    fun drawRec(p1: Point, p2: Point) {
        for (y in p1.y.range(p2.y)) {
            for (x in p1.x.range(p2.x)) {
                canvas.writeAt(x, y, color)
            }
        }
    }

    fun drawTriangle(p0: Point, p1: Point, p2: Point) {
        val points = arrayOf(p0, p1, p2).apply { this.sortBy { it.y } }
        drawHalfOfTriangle(points[0], points[1], points[2])
        drawHalfOfTriangle(points[2], points[1], points[0])
    }

    private fun drawHalfOfTriangle(p0: Point, p1: Point, p2: Point) {
        val (k1, b1) = linePrams(p0, p1)
        val (k2, b2) = linePrams(p0, p2)

        for (y in p0.y.range(p1.y)) {
            val startX = if (p0.y == p1.y || p0.x == p1.x) p0.x else ((y - b1) / k1).toInt()
            val endX = if (p0.y == p2.y || p0.x == p2.x) p0.x else ((y - b2) / k2).toInt()

            if (p0.y == p1.y && startX == endX) return

            for (x in startX.range(endX)) canvas.writeAt(x, y, color)
        }
    }
}