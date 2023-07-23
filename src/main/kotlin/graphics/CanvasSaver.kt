package graphics

import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

object CanvasSaver {
    fun saveToPPM(canvas: Canvas, name: String, path: String = "src/main/resources/") {
        val (width, height) = canvas.getDimensions()
        val scene = canvas.getRawPixels()

        val file = File("$path$name.ppm")

        file.bufferedWriter().use {
            it.write("P3\n${width} ${height}\n255\n")

            for (color in scene) {
                it.appendLine(color.toString())
            }
        }
    }

    fun saveTo(canvas: Canvas, name: String, formatName: String, path: String = "src/main/resources/") {
        val (width, height) = canvas.getDimensions()
        val scene = canvas.getRawPixels()

        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val data = scene.map { color -> color.toNumber().toInt() }

        img.setRGB(0, 0, width, height, data.toIntArray(), 0, height)

        val file = File("$path$name.${formatName.lowercase(Locale.getDefault())}")
        ImageIO.write(img, formatName.uppercase(Locale.getDefault()), file)
    }
}
