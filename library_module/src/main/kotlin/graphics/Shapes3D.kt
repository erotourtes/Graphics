package graphics

class Shapes3D(private val canvas: Canvas) {
    private val from3DTo2D = From3DTo2D(canvas)
    private val zBuffer = Array(canvas.width * canvas.height) { Pair(Double.MAX_VALUE, ColorFactory.blank) }

    fun drawTriangle(p1: Vector3D, p2: Vector3D, p3: Vector3D, color: Color = ColorFactory.yellow) {
        val points = arrayOf(p1, p2, p3).apply { this.sortBy { it.y } }
        drawHalfOfTriangle(points[0], points[1], points[2], color)
        drawHalfOfTriangle(points[2], points[1], points[0], color)
    }

    private fun drawHalfOfTriangle(
        p1: Vector3D,
        p2: Vector3D,
        p3: Vector3D,
        color: Color
    ) {
        val first = from3DTo2D.convertTo2D(p1)
        val second = from3DTo2D.convertTo2D(p2)
        val third = from3DTo2D.convertTo2D(p3)

        val (k1, b1) = linePrams(first, second)
        val (k2, b2) = linePrams(first, third)

        for (y in first.y.range(second.y)) {
            val startX = if (first.y == second.y || first.x == second.x) first.x else ((y - b1) / k1).toInt()
            val endX = if (first.y == third.y || first.x == third.x) first.x else ((y - b2) / k2).toInt()

            if (first.y == second.y && startX == endX) break

            for (x in startX.range(endX)) {
                val (u1, u2, u3) = barycentricCoordinates(first, second, third, Point(x, y))

                val z = p1.z * u1 + p2.z * u2 + p3.z * u3
                val prevZ = zBuffer[x + y * canvas.width].first

                if (z < prevZ) {
                    zBuffer[x + y * canvas.width] = Pair(z, color)
                    canvas.writeAt(Point(x, y), color)
                }

                // the Tsodings code, however it seems to be wrong
//                val z = 1 / p1.z * u1 + 1 / p2.z * u2 + 1 / p3.z * u3 // inverted for interpolation
//
//                val prevZ = zBuffer[x + y * canvas.width].first
//                val curZ = 1 / z
//
//                if (curZ < prevZ) {
//                    zBuffer[x + y * canvas.width] = Pair(curZ, color)
//                    canvas.writeAt(Point(x, y), color)
//                }
            }
        }
    }

    fun clearZBuffer() = zBuffer.forEachIndexed { i, _ -> zBuffer[i] = Pair(Double.MAX_VALUE, ColorFactory.blank) }

    private fun barycentricCoordinates(
        p1: Point,
        p2: Point,
        p3: Point,
        point: Point
    ): Triple<Double, Double, Double> {
        val (x1, y1) = p1
        val (x2, y2) = p2
        val (x3, y3) = p3
        val (x, y) = point

        val det = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3)
        val u1 = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / det.toDouble()
        val u2 = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / det.toDouble()
        val u3 = 1.0 - u1 - u2

        return Triple(u1, u2, u3)
    }
}