import graphics.Canvas
import graphics.Color
import graphics.Shapes
import java.awt.Point


fun main() {
    val canvas = Canvas(300u, 200u)
    canvas.fill(Color())
    val shapes = Shapes(canvas)
    shapes.drawCircle(Point(0, 0), 50)
    shapes.drawLine(Point(100, 100), Point(105, 150))
    shapes.color = Color(0xFFFF000Fu)
    shapes.drawLine(Point(150, 105), Point(100, 100))
    shapes.drawLine(Point(150, 105), Point(150, 150))
    shapes.drawLine(Point(150, 105), Point(100, 105))
    canvas.saveToPPM()
}