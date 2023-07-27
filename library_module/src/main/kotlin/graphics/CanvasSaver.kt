package graphics

import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

object CanvasSaver {

    fun saveToPPM(canvas: Canvas, fullPath: String) {
        val last = fullPath.lastIndexOf('/')

        val (width, height) = canvas.getDimensions()
        saveToPPM(canvas.getRawPixels(), width, height, fullPath.substring(last + 1), fullPath.substring(0, last + 1))
    }

    fun saveToPPM(view: Canvas.CanvasView, fullPath: String) {
        val last = fullPath.lastIndexOf('/')

        val (width, height) = Pair(view.width, view.height)
        saveToPPM(view.getRawPixels(), width, height, fullPath.substring(last + 1), fullPath.substring(0, last + 1))
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

        img.setRGB(0, 0, width, height, data.toIntArray(), 0, height)

        val file = File("$path$name.${formatName.lowercase(Locale.getDefault())}")
        ImageIO.write(img, formatName.uppercase(Locale.getDefault()), file)
    }
}