package graphics

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.awt.Point

class ShapesTest {
    private val canvas = Canvas(20u, 20u)
    private val shapes = Shapes(canvas)

    @Test
    fun drawCircle() {
        val testFile = "src/test/resources/test-${methodName()}.ppm"

        shapes.color = ColorFactory.blue(255u)
        shapes.drawCircle(Point(canvas.width.toInt() / 2, canvas.height.toInt() / 2), 7)
        shapes.color = ColorFactory.red(255u)
        shapes.drawCircle(Point(0, 0), 10)

        val testCanvas = ppmToCanvas(testFile)
        assertIterableEquals(canvas.getRawPixels(), testCanvas.getRawPixels())
    }

    @Test
    fun drawLine() {
        val testFile = "src/test/resources/test-${methodName()}.ppm"

        shapes.color = ColorFactory.blue(255u)
        shapes.drawLine(Point(10, 10), Point(10, 13))
        shapes.color = ColorFactory.red(255u)
        shapes.drawLine(Point(10, 10), Point(13, 10))
        shapes.color = ColorFactory.green(255u)
        shapes.drawLine(Point(0, 0), Point(13, 20))

        val testCanvas = ppmToCanvas(testFile)
        assertIterableEquals(canvas.getRawPixels(), testCanvas.getRawPixels())
    }

    @Test
    fun drawTriangle() {
        val testFile = "src/test/resources/test-${methodName()}.ppm"

        shapes.color = ColorFactory.green(255u)
        shapes.drawTriangle(Point(0, 0), Point(10, 0), Point(10, 19))

        shapes.color = ColorFactory.red(255u)
        shapes.drawTriangle(Point(0, 0), Point(0, 5), Point(20, 5))

        shapes.color = ColorFactory.blue(255u)
        shapes.drawTriangle(Point(10, 10), Point(3, 10), Point(20, 17))

        shapes.color = ColorFactory.red(100u)
        shapes.drawTriangle(Point(15, 15), Point(19, 15), Point(19, 0))

        val testCanvas = ppmToCanvas(testFile)
        assertIterableEquals(canvas.getRawPixels(), testCanvas.getRawPixels())
    }
}