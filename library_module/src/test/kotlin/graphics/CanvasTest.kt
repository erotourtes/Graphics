package graphics

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CanvasTest {
    private val canvas = Canvas(20u, 20u)
    private val shapes = Shapes(canvas)

    @Test
    fun saveToPPM() {
        val testFile = "src/test/resources/test-canvas-view-${methodName()}.ppm"


        shapes.color = ColorFactory.red(200u)
        shapes.drawTriangle(Point(0, 0), Point(0, 19), Point(19, 0))

        shapes.color = ColorFactory.green(200u)
        shapes.drawCircle(Point(5, 5), 1)
        shapes.drawCircle(Point(8, 10), 1)

        val view = canvas.CanvasView(Point(5, 5), Point(9, 11))
//        CanvasSaver.saveToPPM(view, testFile)

        val testCanvas = ppmToCanvas(testFile)
        assertIterableEquals(view.getRawPixels(), testCanvas.getRawPixels())
    }
}