package graphics

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ShapesTest {
    private val canvas = CanvasFactory.createCanvas(20, 20)
    private val shapes = Shapes(canvas)

    @Test
    fun drawCircle() {
        val testFile = "src/test/resources/test-${methodName()}.ppm"

        shapes.color = ColorFactory.blue(255u)
        shapes.drawCircle(Point(canvas.width / 2, canvas.height / 2), 7)
        shapes.color = ColorFactory.red(255u)
        shapes.drawCircle(Point(0, 0), 10)
//        CanvasSaver.saveToPPM(canvas, testFile)

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
//        CanvasSaver.saveToPPM(canvas, testFile)

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
//        CanvasSaver.saveToPPM(canvas, testFile)

        val testCanvas = ppmToCanvas(testFile)
        assertIterableEquals(canvas.getRawPixels(), testCanvas.getRawPixels())
    }

    @Test
    fun opacity() {
        val testFile = "src/test/resources/test-${methodName()}.ppm"

        shapes.color = ColorFactory.yellow.builder().alpha(100u).build()
        shapes.drawTriangle(Point(0, 0), Point(0, 19), Point(19, 0))
        shapes.color = ColorFactory.lightGreen.builder().alpha(100u).build()
        shapes.drawCircle(Point(9, 9), 5)
        shapes.color = ColorFactory.darkGreen.builder().alpha(50u).build()
        shapes.drawRec(Point(0, 0), Point(9, 19))
//        CanvasSaver.saveToPPM(canvas, testFile)

        val testCanvas = ppmToCanvas(testFile)
        assertIterableEquals(canvas.getRawPixels(), testCanvas.getRawPixels())
    }
}