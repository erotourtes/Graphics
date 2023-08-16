package graphics

import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

object CanvasSaver {

    fun saveToPPM(canvas: Savable, fullPath: String) {
        val last = fullPath.lastIndexOf('/')

        val (width, height) = canvas.getDimensions()
        saveToPPM(canvas.getRawPixels(), width, height, fullPath.substring(last + 1), fullPath.substring(0, last + 1))
    }

    fun saveToPPM(scene: List<Color>, width: Int, height: Int, name: String, path: String = "src/main/resources/") {
        val file = File("$path${if (name.endsWith(".ppm")) name else "$name.ppm"}")

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

        img.setRGB(0, 0, width, height, data.toIntArray(), 0, width)

        val file = File("$path$name.${formatName.lowercase(Locale.getDefault())}")
        ImageIO.write(img, formatName.uppercase(Locale.getDefault()), file)
    }

    fun readCanvasFrom(name: String, formatName: String = "png", path: String = "src/main/resources/"): Canvas {
        val file = File("$path$name.$formatName")
        val image = ImageIO.read(file)
        val rawPixels = image.getRGB(0, 0, image.width, image.height, null, 0, image.width)

        val canvas = CanvasFactory.createCanvas(image.width, image.height)
        for (i in rawPixels.indices) {
            val color = Color(rawPixels[i].toUInt())
            canvas.writeAt(canvas.pointFromGlobalIndex(i), color)
        }

        return canvas
    }
}
