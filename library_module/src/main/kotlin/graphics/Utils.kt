package graphics

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun Int.range(value: Int) = if (this < value) this..value else value..this


fun linePrams(start: Point, end: Point): Pair<Float, Float> {
    val k = (start.y - end.y) / (start.x - end.x).toFloat()
    val b = start.y - k * start.x

    return Pair(k, b)
}

fun ppmToCanvas(filePath: String): Canvas {
    val file = File(filePath)
    if(!file.isFile) throw NoSuchFileException(file)

    val canvas: Canvas

    BufferedReader(FileReader(file)).use { reader ->
        reader.readLine() // format
        val (width, height) = reader.readLine().split(" ").map { it.toUInt() }
        canvas = Canvas(width, height)
        reader.readLine() // number of colors

        var line: String?
        var counter = 0
        while (reader.readLine().also { line = it } != null) {
            val (red, green, blue) = line!!.split(" ").map { it.toUInt() }
            val color = Color.builder().red(red).green(green).blue(blue).build()
            val y = counter / width.toInt()
            val x = counter - y * width.toInt()
            canvas.writeAt(x, y, color)
            counter++
        }

        return canvas
    }
}
fun methodName() = Thread.currentThread().stackTrace[2].methodName

data class Point(val x: Int, val y: Int)