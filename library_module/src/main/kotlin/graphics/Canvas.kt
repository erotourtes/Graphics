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
        val (topLeft, bottomRight) = sortPoints(from, to)

        if (parentCanvas != null && (!parentCanvas.isInBoundaries(topLeft) || !parentCanvas.isInBoundaries(bottomRight)))
            throw IllegalArgumentException("View is out of parent canvas boundaries $topLeft $bottomRight \n$parentCanvas")

        val parentOffsetFrom = parentCanvas?.from ?: Point(0, 0)
        val parentOffsetTo = parentCanvas?.to ?: Point(bottomRight.x - topLeft.x + 1, bottomRight.y - topLeft.y + 1)

        this.from = Point(parentOffsetFrom.x + topLeft.x, parentOffsetFrom.y + topLeft.y)
        this.to = Point(parentOffsetTo.x + bottomRight.x, parentOffsetTo.y + bottomRight.y)
    }

    init {
        width = to.x - from.x + 1
        height = to.y - from.y + 1
    }

    init {
        scene = parentCanvas?.scene ?: Array(width * height) { Color(0xFF000000u) }
        stride = parentCanvas?.stride ?: width
    }

    fun getAt(local: Point): Color {
        if (!isInBoundaries(local)) throw IllegalArgumentException()

        val index = globalIndexFrom(local)
        return scene[index]
    }

    fun writeAt(local: Point, color: Color) {
        if (!isInBoundaries(local)) return

        val index = globalIndexFrom(local)

        if (index >= scene.size) throw Error("SHOULD NEVER HAPPEN: index ($index) is out of range (${scene.size})\n$this")

        val curColor = scene[index]
        if (isMixingColors) scene[index] = color.mixOver(curColor)
        else scene[index] = color
    }

    fun setRawPixels(colors: List<Color>) {
        if (colors.size != width * height) throw IllegalArgumentException()

        iterate { writeAt(it, colors[globalIndexFrom(it)]) }
    }

    fun fill(color: Color) = iterate { writeAt(it, color) }

    fun clear() = fill(ColorFactory.blank)

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

    inline fun iterateWithEndRowIndicator(callback: (Point, Boolean) -> Unit) {
        for (y in 0..<height)
            for (x in 0..<width)
                callback(Point(x, y), x == width - 1)
    }

    fun fitToDimensions(from: Point, to: Point, src: Canvas) {
        val dstView = CanvasFactory.createView(from, to, this)
        dstView.iterate {
            val x = it.x * src.width / dstView.width
            val y = it.y * src.height / dstView.height

            dstView.writeAt(it, src.getAt(Point(x, y)))
        }
    }

    fun fitToDimensions(width: Int, height: Int, src: Canvas) =
        fitToDimensions(Point(0, 0), Point(width - 1, height - 1), src)

    fun fitToDimensions(src: Canvas) =
        fitToDimensions(Point(0, 0), Point(width - 1, height - 1), src)

    private fun globalIndexFrom(local: Point): Int {
        val xPos = from.x + local.x
        val yPos = from.y + local.y

        return (yPos * stride + xPos)
    }

    fun pointFromGlobalIndex(index: Int): Point {
        val xPos = index % stride
        val yPos = index / stride

        return Point(xPos, yPos)
    }

    private fun isInBoundaries(vararg point: Point): Boolean =
        point.all { (x, y) -> (x in 0..<width) && (y in 0..<height) }

    override fun toString(): String {
        return """
            Canvas(
                width=$width, 
                height=$height, 
                isMixingColors=$isMixingColors, 
                isView=$isView, 
                stride=$stride, 
                from=$from, 
                to=$to, 
                stride=$stride
            )
        """
    }
}