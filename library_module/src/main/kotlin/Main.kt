import graphics.*
import terminal.TermPrinter
import java.lang.Thread.sleep
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// `z Graphics && set PATH ~/Documents/.jdks/AmazonCorretto19.0.2/bin/ /bin/`
// To run a program use `~/.gradle/wrapper/dists/gradle-8.2.1-bin/5hap6b9n41hkg4jeh2au2pllh/gradle-8.2.1/bin/gradle ':library_module:run'`
// All in one `z Graphics && set PATH ~/Documents/.jdks/AmazonCorretto19.0.2/bin/ /bin/ && ~/.gradle/wrapper/dists/gradle-8.2.1-bin/5hap6b9n41hkg4jeh2au2pllh/gradle-8.2.1/bin/gradle ':library_module:run'`

inline fun setInterval(ms: Long, crossinline action: (Int) -> Unit): Thread {
    var delta = 0
    val thread = Thread {
        while (true) {
            action(delta)
            delta++
            sleep(ms)
        }
    }

    thread.start()
    return thread
}

fun animateCircle() {
    val canvas = CanvasFactory.createCanvas(100, 30)
    canvas.fill(ColorFactory.darkGreen)
    val shapes = Shapes(canvas)
    val terminal = TermPrinter()

    val circleRadius = 3
    var circlePoint = Point(50, 10)
    var vector = Vector(Point(1, 1))

    val fps = 30L
    val sleepTime = 1000 / fps * 15

    setInterval(sleepTime) {
        if (
            circlePoint.x - circleRadius <= 0 ||
            circlePoint.x + circleRadius + 1 >= canvas.width
        ) vector = Vector(Point(-vector.direction.x, vector.direction.y))

        if (
            circlePoint.y - circleRadius <= 0 ||
            circlePoint.y + circleRadius + 1 >= canvas.height
        ) vector = Vector(Point(vector.direction.x, -vector.direction.y))

        // TODO: substitute color
        shapes.color = ColorFactory.darkGreen
        shapes.drawCircle(circlePoint, circleRadius, false)

        shapes.color = ColorFactory.yellow
        circlePoint = vector.move(circlePoint)
        shapes.drawCircle(circlePoint, circleRadius)

        terminal.printWithReset(canvas)
    }
}

fun animateSquishing() {
    val maxWidth = 25
    val maxHeight = 25
    val squishFactor = 22

    val c = CanvasFactory.createCanvas(150, 100)
    val emoji = CanvasSaver.readCanvasFrom("emojy")

    val terminal = TermPrinter()

    val fps = 30L
    val sleepTime = 1000 / fps * 15

    var prevView: Canvas? = null

    setInterval(sleepTime) {
        val factor = sin(it.toDouble())

        val curWidth = (maxWidth - factor * squishFactor).toInt()
        val curHeight = (maxHeight + factor * squishFactor).toInt()
        val view = CanvasFactory.createView(
            Point(c.width / 2 - curWidth / 2, c.height - curHeight),
            curWidth,
            curHeight,
            c
        )

        prevView?.fill(ColorFactory.blank)
        view.fitToDimensions(emoji)
        prevView = view

        terminal.printWithReset(c)
    }
}

fun animate3D() {
    val c = CanvasFactory.createCanvas(100, 30)
    c.fill(ColorFactory.blank)
    val shapes3D = Shapes3D(c)
    val terminal = TermPrinter()

    setInterval(1000 / 60 * 15) {
        val z = 1.5
        c.clear()
        shapes3D.clearZBuffer()
        // rotation matrix is
        // cos(a) -sin(a)
        // sin(a) cos(a)

        val p1 =
            Vector3D(
                cos(it.toDouble()) * 0.5,
                -0.3,
                z + 0.5 * sin(it.toDouble())
            )
        val p2 =
            Vector3D(
                cos(it.toDouble() + PI) * 0.5,
                -0.3,
                z + 0.5 * sin(it.toDouble() + PI)
            )
        val p3 = (Vector3D(0.0, 0.5, z))

        shapes3D.drawTriangle(p1, p2, p3)

        terminal.printWithReset(c)
    }
}

fun intersection3D() {
    val c = CanvasFactory.createCanvas(800, 600)
    c.fill(ColorFactory.blank)
    val shapes3D = Shapes3D(c)

    val z = 1.5
    val it = 5

    val p1 =
        Vector3D(
            cos(it.toDouble()) * 0.5,
            -0.3,
            z + 0.5 * sin(it.toDouble())
        )
    val p2 =
        Vector3D(
            cos(it.toDouble() + PI) * 0.5,
            -0.3,
            z + 0.5 * sin(it.toDouble() + PI)
        )
    val p3 = (Vector3D(0.0, 0.5, z))


    val p12 =
        Vector3D(
            cos(it.toDouble() + PI / 2) * 0.5,
            -0.3,
            z + 0.5 * sin(it.toDouble() + PI / 2)
        )
    val p22 =
        Vector3D(
            cos(it.toDouble() + PI + PI / 2) * 0.5,
            -0.3,
            z + 0.5 * sin(it.toDouble() + PI + PI / 2)
        )
    val p32 = (Vector3D(0.0, 0.5, z))

    shapes3D.drawTriangle(p1, p2, p3)
    shapes3D.drawTriangle(p12, p22, p32, ColorFactory.purple)

    CanvasSaver.saveTo(c, "intersection")
}

fun main() {
//    animateCircle()
//    animateSquishing()
    animate3D()

//    intersection3D()
}
