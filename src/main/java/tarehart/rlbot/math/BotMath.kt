package tarehart.rlbot.math


object BotMath {

    const val PI = Math.PI.toFloat()

    fun nonZeroSignum(value: Float) : Int {
        return if (value < 0) -1 else 1
    }
}
