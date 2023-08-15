import graphics.*
import terminal.TermPrinter
import java.lang.Thread.sleep

// To run a program use `~/.gradle/wrapper/dists/gradle-8.2.1-bin/5hap6b9n41hkg4jeh2au2pllh/gradle-8.2.1/bin/gradle ':library_module:run'`

fun main() {
    val canvas = Canvas(100u, 30u)
    canvas.isMixingColors = false
    canvas.fill(ColorFactory.darkGreen)
    val shapes = Shapes(canvas)
    val terminal = TermPrinter()

    val circleRadius = 15
    var circlePoint = Point(50, 10)
    var vector = Vector(Point(1, 1))

    val fps = 30L
    val sleepTime = 1000 / fps * 30

    while (true) {
        if (!(circlePoint.x in 0..canvas.width.toInt() && circlePoint.y in 0..canvas.height.toInt())) {
            vector = Vector(Point(-vector.direction.x, -vector.direction.y))
        }

        // TODO: substitute color
        shapes.color = ColorFactory.darkGreen
        shapes.drawCircle(circlePoint, circleRadius)

        shapes.color = ColorFactory.yellow
        circlePoint = vector.move(circlePoint)
        shapes.drawCircle(circlePoint, circleRadius)

        terminal.printWithReset(canvas)

        sleep(sleepTime)
    }
}