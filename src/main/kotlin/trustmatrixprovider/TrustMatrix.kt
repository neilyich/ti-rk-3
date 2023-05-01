package trustmatrixprovider

import org.ejml.simple.SimpleMatrix
import utils.RandomUtils

object TrustMatrix {
    fun createRandom(size: Int): SimpleMatrix {
        return SimpleMatrix((0 until size).map { randomRow(size) }.toTypedArray())
    }

    private fun randomRow(size: Int): DoubleArray {
        val trusts = List(size) { RandomUtils.instance.nextDouble() }
        val sum = trusts.sum()
        return trusts.map { it / sum }.toDoubleArray()

    }
}