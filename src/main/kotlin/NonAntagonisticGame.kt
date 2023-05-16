import org.ejml.simple.SimpleMatrix
import utils.FormattedPrinter

class NonAntagonisticGame(
    private val a: Double,
    private val b: Double,
    private val c: Double,
    private val d: Double,
    private val gf: Double,
    private val gs: Double,
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

    private fun x(u: Double, v: Double) = u*rf + v*rs + x0

    fun solve(): Pair<Double, Double> {
        FormattedPrinter.print("Результирующая матрица", resultTrustMatrix)
        FormattedPrinter.print("rf", rf)
        FormattedPrinter.print("rs", rs)
        val a00 = gf + 2*b*rf*rf
        val a01 = 2*b*rs*rf
        val a10 = 2*d*rs*rf
        val a11 = gs + 2*d*rs*rs
        val b0 = a*rf
        val b1 = c*rs
        val A = SimpleMatrix(arrayOf(
            doubleArrayOf(a00, a01),
            doubleArrayOf(a10, a11)
        ))
        val b = SimpleMatrix(doubleArrayOf(b0, b1))
        val x = A.solve(b)
        val u0 = (rf - 2*rf*rs*rs) / (4*rf*rf + 2*rs*rs + 1)
        val u1 = (rs + 2*rs*rf*rf) / (4*rf*rf + 2*rs*rs + 1)
        println("Test")
        println("u0 = ${u0}")
        println("u1 = ${u1}")
        val u = x[0]
        val v = x[1]
        FormattedPrinter.print("u*", u)
        FormattedPrinter.print("v*", v)
        val firstWin = Ff(u, v)
        val secondWin = Fs(u, v)
        FormattedPrinter.print("Целевая функция игрока 1", firstWin)
        FormattedPrinter.print("Целевая функция игрока 2", secondWin)
        if (firstWin > secondWin) {
            println("Победитель - игрок 1")
        } else {
            println("Победитель - игрок 2")
        }
        return u to v
    }

    fun Ff(u: Double, v: Double): Double {
        val x = x(u, v)
        return a*x - b*x*x - u*u/2 * gf
    }

    private fun Fs(u: Double, v: Double): Double {
        val x = x(u, v)
        return c*x - d*x*x - v*v/2 * gs
    }

}