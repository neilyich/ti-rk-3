package agentsprovider

import Agent
import utils.RandomUtils
import kotlin.random.nextInt

class RandomAgentsProvider(
    private val opinionsRange: IntRange,
) : InitialAgentsProvider {
    override fun agents(count: Int): List<Agent> {
        return List(count) { index ->
            Agent(
                id = index + 1,
                index = index,
                opinion = RandomUtils.instance.nextInt(opinionsRange).toDouble()
            )
        }
    }
}