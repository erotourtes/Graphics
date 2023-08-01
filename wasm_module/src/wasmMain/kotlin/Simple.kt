import graphics.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.random.Random

const val WIDTH = 500
const val HEIGHT = 500

@JsFun("x => x")
external fun stringToJsAny(x: String): JsAny

fun init(): HTMLCanvasElement {
    val canvas = document.querySelector("canvas") ?: throw NoSuchElementException()
    canvas as HTMLCanvasElement
    canvas.setAttribute("width", "$WIDTH")
    canvas.setAttribute("height", "$HEIGHT")

    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    ctx.fillStyle = stringToJsAny("black")
    ctx.fillRect(10.0, 10.0, 100.0, 100.0)

    return canvas
}


fun animate(canvas: HTMLCanvasElement) {
    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    val color = if (Random.nextBoolean()) "black" else "white"
    ctx.fillStyle = stringToJsAny(color)

    window.requestAnimationFrame { animate(canvas) }
}

fun main() {
    val shapes = Shapes(Canvas(WIDTH.toUInt(), HEIGHT.toUInt()))
    val canvas = init()

    animate(canvas)

}