import org.ejml.simple.SimpleMatrix

class InformationConfrontation(
    private val trustMatrix: SimpleMatrix,
    private val agents: List<Agent>,
    private val e: Double,
) {

    var iterations = 0
        private set

    private var currentTrustMatrix = trustMatrix

    fun perform(): SimpleMatrix {
        while (!nextIterationNeeded()) {
            iterate()
        }
        return currentTrustMatrix
    }

    private fun iterate() {
        val x0 = SimpleMatrix(agents.map { it.opinion }.toDoubleArray())
        val x1 = trustMatrix.mult(x0)
        agents.forEachIndexed { index, agent -> agent.opinion = x1[index, 0] }
        iterations++
        currentTrustMatrix = currentTrustMatrix.mult(trustMatrix)
    }

    private fun nextIterationNeeded(): Boolean {
        for (col in 0 until currentTrustMatrix.numCols) {
            val column = currentTrustMatrix.getColumn(col)
            val max = column.elementMax()
            val min = column.elementMin()
            if (max - min > e) {
                return false
            }
        }
        return true
    }

}