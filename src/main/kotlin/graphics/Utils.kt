package graphics

import java.awt.Point
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class Color(color: UInt = 0xFF000000u) {
    // color 0xAARRGGBB
    val alpha = color shr 8 * 3 and 0xFFu
    val red = color shr 8 * 2 and 0xFFu
    val green = color shr 8 * 1 and 0xFFu
    val blue = color shr 8 * 0 and 0xFFu


    override fun toString(): String {
        return "$red $green $blue"
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Color

        return buildNumber(this.getValues()) == buildNumber(other.getValues())
    }

    fun getValues() = listOf(alpha, red, green, blue)

    companion object {
        fun builder() = Builder()

        private fun buildNumber(channels: List<UInt>): UInt {
            var color = 0x00000000u
            for (i in 0 until 3)
                color = color or channels[i] shl 8
            return color or channels[3]
        }
    }

    class Builder {
        private val channels = Array(4) { 0x00u }

        init {
            channels[0] = 0xFFu
        }

        fun alpha(value: UInt): Builder = apply { channels[0] = value }
        fun red(value: UInt): Builder = apply { channels[1] = value }
        fun green(value: UInt): Builder = apply { channels[2] = value }
        fun blue(value: UInt): Builder = apply { channels[3] = value }

        fun build() = Color(buildNumber(channels.asList()))
    }
}

object ColorFactory {
    fun red(value: UInt = 255u) = Color.builder().red(value).build()
    fun green(value: UInt = 255u) = Color.builder().green(value).build()
    fun blue(value: UInt = 255u) = Color.builder().blue(value).build()
}

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