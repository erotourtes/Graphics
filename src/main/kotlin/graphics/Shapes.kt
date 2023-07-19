package graphics

import java.awt.Point

class Shapes(private val canvas: Canvas) {
    var color = Color(0xFF0000FFu)

    fun drawCircle(center: Point, radius: Int) {
        val cy = center.y
        val cx = center.y
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

        val k = (start.y - end.y) / (start.x - end.x).toFloat()
        val b = start.y - k * start.x

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
}