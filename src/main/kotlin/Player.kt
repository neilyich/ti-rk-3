data class Player(
    val id: Int,
    val opinion: Double,
) {
    val agents: MutableList<Agent> = mutableListOf()
}