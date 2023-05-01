package utils

import kotlin.random.Random

object RandomUtils {
    private var random = Random(42)

    val instance: Random
        get() = random

    fun setSeed(seed: Int) {
        random = Random(seed)
    }
}