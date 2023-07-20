package graphics

import java.io.File

class Canvas(val width: UInt, val height: UInt) {
    private val scene: Array<Color> = Array((width * height).toInt()) { Color(0xFF000000u) }

    fun fill(color: Color) {
        for (i in 0 until (width * height).toInt()) {
            scene[i] = color
        }
    }

    fun getRawPixels(): List<Color> {
        return scene.toList()
    }

    fun writeAt(x: Int, y: Int, color: Color) {
        if (!(0 <= x && x < width.toInt()) || !(0 <= y && y < height.toInt())) return

        scene[(y * width.toInt() + x)] = color
    }

    fun saveToPPM(path: String = "src/main/resources/test.ppm") {
        val file = File(path)

        file.bufferedWriter().use {
            it.write("P3\n$width $height\n255\n")

            for (color in scene) {
                it.appendLine(color.toString())
            }
        }
    }
}