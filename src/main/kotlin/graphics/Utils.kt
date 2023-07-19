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

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        val channels = Array(4) { 0x00u }

        init {
            channels[0] = 0xFFu
        }

        fun alpha(value: UInt): Builder = apply { channels[0] = value }
        fun red(value: UInt): Builder = apply { channels[1] = value }
        fun green(value: UInt): Builder = apply { channels[2] = value }
        fun blue(value: UInt): Builder = apply { channels[3] = value }

        fun build(): UInt {
            var color = 0x00000000u
            for (i in 0 until 3)
                color = color or channels[i] shl 8
            return color or channels[3]
        }
    }
}

fun Int.range(value: Int) = if (this < value) this..value else value..this