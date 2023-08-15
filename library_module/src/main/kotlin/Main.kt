import graphics.*
import terminal.TermPrinter
import java.lang.Thread.sleep

// To run a program use `~/.gradle/wrapper/dists/gradle-8.2.1-bin/5hap6b9n41hkg4jeh2au2pllh/gradle-8.2.1/bin/gradle ':library_module:run'`

fun animate() {
    val canvas = Canvas(100u, 30u)
    canvas.fill(ColorFactory.darkGreen)
    val shapes = Shapes(canvas)
    val terminal = TermPrinter()

    val circleRadius = 3
    var circlePoint = Point(50, 10)
    var vector = Vector(Point(1, 1))

    val fps = 30L
    val sleepTime = 1000 / fps * 15

    while (true) {
        if (
            circlePoint.x - circleRadius <= 0 ||
            circlePoint.x + circleRadius + 1 >= canvas.width.toInt()
        ) vector = Vector(Point(-vector.direction.x, vector.direction.y))

        if (
            circlePoint.y - circleRadius <= 0 ||
            circlePoint.y + circleRadius + 1 >= canvas.height.toInt()
        ) vector = Vector(Point(vector.direction.x, -vector.direction.y))

        // TODO: substitute color
        shapes.color = ColorFactory.darkGreen
        shapes.drawCircle(circlePoint, circleRadius, false)

        shapes.color = ColorFactory.yellow
        circlePoint = vector.move(circlePoint)
        shapes.drawCircle(circlePoint, circleRadius)

        terminal.printWithReset(canvas)
        sleep(sleepTime)
    }
}

fun testAntialiasing() {
    val canvas = Canvas(100u, 30u)
    canvas.fill(ColorFactory.darkGreen)
    val shapes = Shapes(canvas)

    val circleRadius = 3
    val circlePoint = Point(50, 10)
    shapes.color = ColorFactory.yellow
    shapes.drawCircle(circlePoint, circleRadius)
    CanvasSaver.saveTo(canvas, "test-again", "png")
}

fun main() {
//    animate()
    testAntialiasing()
}