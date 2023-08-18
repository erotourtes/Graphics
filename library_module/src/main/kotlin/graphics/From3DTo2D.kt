package graphics

import kotlin.math.abs
import kotlin.math.sqrt

class From3DTo2D(private val canvas: Canvas) {
    private val screenDistance = 1

    fun convertTo2D(p: Vector3D): Point {
        val screen_p_y = p.y / p.z * screenDistance
        val screen_p_x = p.x / p.z * screenDistance

        return translateCartesianToScreenCoordinates(Vector3D(screen_p_x, screen_p_y, 0.0))
    }

    private fun translateCartesianToScreenCoordinates(p: Vector3D): Point {
//        val p = p.normalize()
        val screenX = (p.x + 1) / 2 * canvas.width
        val screenY = (1-(p.y + 1) / 2) * canvas.height

        return Point(screenX.toInt(), screenY.toInt())
    }
}