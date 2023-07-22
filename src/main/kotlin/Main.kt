import graphics.Canvas
import graphics.Color
import graphics.ColorFactory
import graphics.Shapes
import java.awt.Point


fun main() {
    val canvas = Canvas(20u, 20u)
    canvas.fill(Color())
    val shapes = Shapes(canvas)
    for (i in 0..10) {
        shapes.drawLine(Point(2 * i, 0), Point(2 * i, 20))
        shapes.drawLine(Point(0, 2 * i), Point(20, 2 * i))
    }

    shapes.color = ColorFactory.green(30u)
    shapes.drawTriangle(Point(0, 0), Point(0, 19), Point(19, 0))

    val view = canvas.CanvasView(Point(5, 10), Point(19, 19))
    view.saveToPPM()
    canvas.saveToPPM()
}