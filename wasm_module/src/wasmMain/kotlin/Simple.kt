import kotlinx.browser.document
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

const val WIDTH = 500
const val HEIGHT = 500

@JsFun("x => x")
external fun stringToDynamic(x: String): Dynamic

fun main() {
    val canvas = document.querySelector("canvas") ?: throw NoSuchElementException()
    canvas as HTMLCanvasElement
    canvas.setAttribute("width", "$WIDTH")
    canvas.setAttribute("height", "$HEIGHT")


    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    ctx.fillStyle = stringToDynamic("green")
    ctx.fillRect(10.0, 10.0, 100.0, 100.0)
}