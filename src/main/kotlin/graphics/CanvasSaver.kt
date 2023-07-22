package graphics

import java.io.File

class CanvasSaver() {
    fun saveToPPM(scene: Array<Color>, width: Int, height: Int, path: String = "src/main/resources/test.ppm") {
        val file = File(path)

        file.bufferedWriter().use {
            it.write("P3\n${width} ${height}\n255\n")

            for (color in scene) {
                it.appendLine(color.toString())
            }
        }
    }

//    fun saveToPNG(path: String = "src/main/resources/test.png") {
//        val width = canvas.width.toInt()
//        val height = canvas.height.toInt()
//
//        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
//        val data = canvas.getRawPixels().map { color -> color.getValues().map { it.toByte() } }.flatten()
//        img.raster.setDataElements(0, 0, width, height, data.toByteArray())
//        val file = File(path)
//        ImageIO.write(img, "PNG", file)
//
//        TODO("fix it!")
//    }
}
