package graphics

import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class ImageReader {
    fun readImage(): BufferedImage {
        val file = File("src/main/resources/test-again.png")
        return ImageIO.read(file)
    }

    fun placeImageOn(view: Canvas, fileName: String, path: String = "src/main/resources/") {
        val image = ImageIO.read(File("$path$fileName"))
        val width = image.width
        val height = image.height
        val pixels = image.getRGB(0, 0, width, height, null, 0, width)
        val colors = pixels.map { Color(it.toUInt()) }
        view.setRawPixels(colors)
    }
}