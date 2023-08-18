package terminal

import graphics.Canvas
import graphics.Color


class TermPrinter {
    private val chars = ".,-~:;=!*#$@"

    fun printWithReset(canvas: Canvas) {
        print("\u001b[H\u001b[2J")

        print(canvas)
    }

    fun print(canvas: Canvas) {
        canvas.iterateWithEndRowIndicator { it, isEndOfRow ->
            colorToChar(canvas.getAt(it)).let(::print)

            if (isEndOfRow) println()
        }
    }


    // TODO: it doesn't use alpha channel
    private fun colorToChar(color: Color): Char {
        if (color.alpha == 0u) return ' '

        val redPerceivedBr = 0.299
        val greenPerceivedBr = 0.587
        val bluePerceivedBr = 0.114

        val greyIntensity =
            (color.red.toFloat() * redPerceivedBr +
                    color.green.toFloat() * greenPerceivedBr +
                    color.blue.toFloat() * bluePerceivedBr) * (color.alpha.toDouble() / 255.0)

        val index = (greyIntensity * (chars.length - 1) / 255)
        val char = chars[index.toInt()];

        return char
    }
}