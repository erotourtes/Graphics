package graphics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Canvas(val width: UInt, val height: UInt) {
    private val scene: Array<Color> = Array((width * height).toInt()) { Color(0xFF000000u) }

    var isMixingColors = true

    fun fill(color: Color) {
        for (i in 0..<(width * height).toInt()) scene[i] = color
    }

    fun getRawPixels(): List<Color> = scene.toList()

    fun writeAt(x: Int, y: Int, color: Color) {
        if (!(0 <= x && x < width.toInt()) || !(0 <= y && y < height.toInt())) return

        val position = (y * width.toInt() + x)
        val curColor = scene[position]
        if (isMixingColors) scene[position] = color.mixOver(curColor)
        else scene[position] = color
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

    fun saveToPNG(path: String = "src/main/resources/test.png") {
        val img = BufferedImage(width.toInt(), height.toInt(), BufferedImage.TYPE_INT_RGB)
        val data = getRawPixels().map { color -> color.getValues().map { it.toByte() }}.flatten()
        img.raster.setDataElements(0, 0, width.toInt(), height.toInt(), data.toByteArray())
        val file = File(path)
        ImageIO.write(img, "PNG", file)

        TODO("fix it!")
    }
}