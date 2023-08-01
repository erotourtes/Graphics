import graphics.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.khronos.webgl.Uint8ClampedArray
import org.khronos.webgl.set
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.ImageData
import kotlin.random.Random

const val WIDTH = 100
const val HEIGHT = 100

@JsFun("x => x")
external fun stringToJsAny(x: String): Dynamic

fun init(): HTMLCanvasElement {
    val canvas = document.querySelector("canvas") ?: throw NoSuchElementException()
    canvas as HTMLCanvasElement
    canvas.setAttribute("width", "$WIDTH")
    canvas.setAttribute("height", "$HEIGHT")

//    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
//    ctx.fillStyle = stringToJsAny("black")
//    ctx.fillRect(10.0, 10.0, 100.0, 100.0)

    return canvas
}


fun animate(canvas: HTMLCanvasElement, shapes: Shapes) {
    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D

    val color = if (Random.nextBoolean()) 0xFF000000u else 0xFFFFFFFFu
    shapes.color = Color(color)

    shapes.drawCircle(Point(25, 25), Random.nextInt(10))
    putPixelsTo(ctx, shapes.canvas)

    window.requestAnimationFrame { animate(canvas, shapes) }
}

fun putPixelsTo(ctx: CanvasRenderingContext2D, from: Canvas) {
    val arrOfPoints =
        from.getRawPixels().map { color ->
            listOf(color.red, color.green, color.blue, color.alpha).map { it.toUByte() }
        }.flatten().toTypedArray()

    val img = ImageData(WIDTH, HEIGHT)
    for (i in arrOfPoints.indices) {
        TODO("KOTLIN BUG: SHOULD BE UBYTE INSTEAD OF BYTE")
//        img.data[i] = arrOfPoints[i]
        img.data[i] = if (arrOfPoints[i] > 0u) 127 else 0
    }
    ctx.putImageData(img, 0.0, 0.0)
}

fun main() {
    val shapes = Shapes(Canvas(WIDTH.toUInt(), HEIGHT.toUInt()))
    val canvas = init()

    shapes.color = ColorFactory.purple
    shapes.drawTriangle(Point(0, 0),Point(15, 0), Point(0, 15))
    putPixelsTo(canvas.getContext("2d") as CanvasRenderingContext2D, shapes.canvas)

//    animate(canvas, shapes)
}