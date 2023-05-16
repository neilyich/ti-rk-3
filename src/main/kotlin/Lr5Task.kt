data class Lr5Task(
    val seed: Int,
    val agentsCount: Int,
    val coefs: Coefs,
    val maxEpsilon: Double,
    val noInfluenceProbability: Double,
    val formatting: Formatting,
) {
    data class Coefs(
        val a: Double,
        val b: Double,
        val c: Double,
        val d: Double,
        val gf: Double,
        val gs: Double,
    )

    data class Formatting(
        val scale: Int
    )
}