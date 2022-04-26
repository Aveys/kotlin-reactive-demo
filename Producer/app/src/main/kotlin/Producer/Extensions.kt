package Producer

import kotlin.random.Random

fun Double.randomPercent(): Double {
    val percent = Random.nextDouble(0.01, 0.2)
    return if (Random.nextBoolean()) {
        this + this * percent
    } else {
        this - this * percent
    }
}
