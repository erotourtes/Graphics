package graphics

class Canvas(val width: UInt, val height: UInt) {
    private val scene: Array<Color> = Array((width * height).toInt()) { Color(0xFF000000u) }

    var isMixingColors = true

    fun fill(color: Color) {
        for (i in 0..<(width * height).toInt()) scene[i] = color
    }
    fun clear() {
        fill(Color(0xFF000000u))
    }

    fun getRawPixels(): List<Color> = scene.toList()

    fun writeAt(x: Int, y: Int, color: Color) {
        if (!(0 <= x && x < width.toInt()) || !(0 <= y && y < height.toInt())) return

        val position = (y * width.toInt() + x)
        val curColor = scene[position]
        if (isMixingColors) scene[position] = color.mixOver(curColor)
        else scene[position] = color
    }

    fun isInBoundaries(vararg point: Point): Boolean =
        point.all { (x, y) -> 0 <= x && x < width.toInt() && 0 <= y && y < height.toInt() }

    fun getAt(point: Point): Color {
        if (!isInBoundaries(point)) throw IllegalArgumentException()

        val position = (point.y * width.toInt() + point.x)
        return scene[position]
    }

    fun getDimensions(): Pair<Int, Int> = Pair(width.toInt(), height.toInt())

    inline fun iterate(callback: (Point) -> Unit) {
        for (y in 0..<height.toInt()) {
            for (x in 0..<width.toInt()) {
                callback(Point(x, y))
            }
        }
    }

    fun toDimensions(x: Int, y: Int): Canvas {
        val newCanvas = Canvas(x.toUInt(), y.toUInt())

        iterate {
            val x = it.x * x / width.toInt()
            val y = it.y * y / width.toInt()

            newCanvas.writeAt(x, y, getAt(it))
        }

        return newCanvas
    }

    inner class CanvasView(from: Point, to: Point) {
        val from: Point
        val to: Point

        init {
            val (x1, y1) = from
            val (x2, y2) = to

            this.from = Point(x1.coerceAtMost(x2), y1.coerceAtMost(y2))
            this.to = Point(x1.coerceAtLeast(x2), y1.coerceAtLeast(y2))
        }

        val width get() = to.x - from.x + 1
        val height get() = to.y - from.y + 1

        private val sceneView = Array(width * height) { Color() }
            get() {
                for (y in from.y..to.y) {
                    for (x in from.x..to.x) {
                        val position = (y - from.y) * width + (x - from.x)
                        field[position] = getAt(Point(x, y))
                    }
                }

                return field
            }

        fun getRawPixels() = sceneView.toList()
    }
}