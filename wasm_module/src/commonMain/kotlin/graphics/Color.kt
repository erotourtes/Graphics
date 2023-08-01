package graphics

class Color(color: UInt = 0xFF000000u) {
    // color 0xAARRGGBB
    val alpha = color shr 8 * 3 and 0xFFu
    val red = color shr 8 * 2 and 0xFFu
    val green = color shr 8 * 1 and 0xFFu
    val blue = color shr 8 * 0 and 0xFFu

    override fun toString(): String {
        return "$red $green $blue"
    }

    fun builder() = Builder().alpha(alpha).red(red).green(green).blue(blue)

    fun mixOver(b: Color): Color {
        val builder = Builder()
        builder.alpha(alpha)

        val alpha = this.alpha.toFloat() / 255f
        val red = (this.red.toFloat() * alpha + (1 - alpha) * b.red.toFloat()).toUInt()
        val green = (this.green.toFloat() * alpha + (1 - alpha) * b.green.toFloat()).toUInt()
        val blue = (this.blue.toFloat() * alpha + (1 - alpha) * b.blue.toFloat()).toUInt()
        builder.red(red).green(green).blue(blue)

        return builder.build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return try {
            other as Color
            this.red == other.red && this.green == other.green && this.blue == other.blue
        } catch (e: Exception) {
            false
        }
    }

    fun getValues() = listOf(alpha, red, green, blue)

    fun toNumber() = buildNumber(getValues())

    companion object {
        fun builder() = Builder()

         fun buildNumber(channels: List<UInt>): UInt {
            var color = 0x00000000u
            for (i in 0 until 3)
                color = color or channels[i] shl 8
            return color or channels[3]
        }
    }

    class Builder {
        private val channels = Array(4) { 0x00u }

        init {
            channels[0] = 0xFFu
        }

        fun alpha(value: UInt): Builder = apply { channels[0] = value }
        fun red(value: UInt): Builder = apply { channels[1] = value }
        fun green(value: UInt): Builder = apply { channels[2] = value }
        fun blue(value: UInt): Builder = apply { channels[3] = value }

        fun build() = Color(buildNumber(channels.asList()))
    }
}

object ColorFactory {
    fun red(value: UInt = 255u) = Color.builder().red(value).build()
    fun green(value: UInt = 255u) = Color.builder().green(value).build()
    fun blue(value: UInt = 255u) = Color.builder().blue(value).build()

    val yellow = Color(0xFFF7B32Bu)
    val lightGreen = Color(0xFF89CE94u)
    val darkGreen = Color(0xFF297373u)
    val purple = Color(0xFF7D5BA6u)
    val blank = Color()
}