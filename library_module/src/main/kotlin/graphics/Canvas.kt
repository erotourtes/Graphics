package graphics

interface Savable {
    fun getRawPixels(): List<Color>
    fun getDimensions(): Pair<Int, Int>
}

object CanvasFactory {
    fun createView(from: Point, to: Point, parentCanvas: Canvas) = Canvas(from, to, parentCanvas)
    fun createView(start: Point, width: Int, height: Int, parentCanvas: Canvas) =
        createView(start, Point(start.x + width - 1, start.y + height - 1), parentCanvas)

    fun createCanvas(width: Int, height: Int) =
        Canvas(Point(0, 0), Point(width - 1, height - 1), null)
}

class Canvas(from: Point, to: Point, parentCanvas: Canvas?) : Savable {
    val width: Int
    val height: Int
    var isMixingColors = true
    val isView: Boolean = parentCanvas != null
    private val stride: Int
    private val from: Point
    private val to: Point
    private val scene: Array<Color>

    init {
        val (x1, y1) = from
        val (x2, y2) = to

        this.from = Point(x1.coerceAtMost(x2), y1.coerceAtMost(y2))
        this.to = Point(x1.coerceAtLeast(x2), y1.coerceAtLeast(y2))
    }

    init {
        width = to.x - from.x + 1
        height = to.y - from.y + 1
    }

    init {
        scene = parentCanvas?.scene ?: Array(width * height) { Color(0xFF000000u) }
        stride = parentCanvas?.width ?: width
    }

    fun getAt(local: Point): Color {
        if (!isInBoundaries(local)) throw IllegalArgumentException()

        val index = globalIndexFrom(local)
        return scene[index]
    }

    fun writeAt(local: Point, color: Color) {
        if (!isInBoundaries(local)) return

        val index = globalIndexFrom(local)

        val curColor = scene[index]
        if (isMixingColors) scene[index] = color.mixOver(curColor)
        else scene[index] = color
    }

    fun setRawPixels(colors: List<Color>) {
        if (colors.size != width * height) throw IllegalArgumentException()

        iterate { writeAt(it, colors[globalIndexFrom(it)]) }
    }

    fun fill(color: Color) = iterate { writeAt(it, color) }

    fun clear() = fill(Color(0xFF000000u))

    override fun getRawPixels(): List<Color> {
        if (isView) {
            val viewScene = MutableList(0) { ColorFactory.blank }
            iterate { viewScene.add(getAt(it)) }
            return viewScene
        }

        return scene.toList()
    }

    override fun getDimensions(): Pair<Int, Int> = Pair(width, height)

    inline fun iterate(callback: (Point) -> Unit) {
        for (y in 0..<height)
            for (x in 0..<width)
                callback(Point(x, y))
    }

    private fun globalIndexFrom(local: Point): Int {
        val xPos = from.x + local.x
        val yPos = from.y + local.y

        return (yPos * stride + xPos)
    }

    private fun isInBoundaries(vararg point: Point): Boolean =
        point.all { (x, y) -> (x in 0..<width) && (y in 0..<height) }
}


//class Canvas(val width: Int, val height: Int) {
//    fun toDimensions(x: Int, y: Int): Canvas {
//        val newCanvas = Canvas(x, y)
//
//        iterate {
//            val x = it.x * x / width
//            val y = it.y * y / height
//
//            newCanvas.writeAt(x, y, getAt(it))
//        }
//
//        return newCanvas
//    }
//}