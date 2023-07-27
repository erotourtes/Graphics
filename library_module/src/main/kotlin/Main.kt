import graphics.*
import java.awt.Point


fun main() {

    val canvas = Canvas(200u, 200u)
    canvas.fill(ColorFactory.yellow)
    val shapes = Shapes(canvas)

    shapes.color = ColorFactory.green(30u)
//    shapes.drawTriangle(Point(0, 0), Point(0, 19), Point(19, 0))
    shapes.drawCircle(Point(100, 100), 1)

    CanvasSaver.saveTo(canvas, "test", "png")
//    CanvasSaver.saveToPPM(canvas, "test")
}