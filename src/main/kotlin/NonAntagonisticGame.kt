import org.ejml.simple.SimpleMatrix

class NonAntagonisticGame(
    private val cf: (Double) -> Double,
    private val cs: (Double) -> Double,
    private val hf: (Double) -> Double,
    private val hs: (Double) -> Double,
    private val players: List<Player>,
    private val trustMatrix: SimpleMatrix,
    private val agents: List<Agent>,
    private val e: Double,
) {
    private val initialAgents = agents.map { it.copy() }
    private val confrontation = InformationConfrontation(trustMatrix, agents, e)
    private val resultTrustMatrix = confrontation.perform()
    private val rf = players[0].agents.map { it.index }.sumOf { resultTrustMatrix[0, it] }
    private val rs = players[1].agents.map { it.index }.sumOf { resultTrustMatrix[0, it] }
    private val influencedAgents = players.flatMap { it.agents }.toSet()
    private val independentAgents = agents.filter { agent -> agent !in influencedAgents }
    private val x0 = independentAgents.map { it.index }.sumOf { resultTrustMatrix[0, it] * initialAgents[it].opinion }

    private fun ff(u: Double, v: Double) = hf(x(u, v)) - cf(u)
    private fun fs(u: Double, v: Double) = hs(x(u, v)) - cs(v)

    private fun x(u: Double, v: Double) = u*rf + v*rs + x0


}