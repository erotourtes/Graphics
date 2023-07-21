package graphics

import java.awt.Point

class Shapes(private val canvas: Canvas) {
    var color = Color(0xFF0000FFu)

    fun drawCircle(center: Point, radius: Int) {
        val cy = center.y
        val cx = center.x
        for (y in cy - radius..cy + radius) {
            for (x in cx - radius..cx + radius) {
                val dx = x - cx
                val dy = y - cy
                if (dx * dx + dy * dy <= radius * radius)
                    canvas.writeAt(x, y, color)
            }
        }
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