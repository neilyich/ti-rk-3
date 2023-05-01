package agentsprovider

import Agent
import Player
import utils.RandomUtils

class InfluencedAgentsProvider(
    private val players: List<Player>,
    private val agents: List<Agent>,
    private val noInfluenceProbability: Double,
) : InitialAgentsProvider {
    override fun agents(count: Int): List<Agent> {
        agents.indices.forEach { influenceAgentOrNot(it) }
        return agents
    }

    private fun influenceAgentOrNot(index: Int) {
        if (agentHasInfluence()) {
            influenceAgent(randomPlayer(), index)
        }
    }

    private fun agentHasInfluence() = RandomUtils.instance.nextDouble() > noInfluenceProbability

    private fun randomPlayer() = players.random(RandomUtils.instance)

    private fun influenceAgent(player: Player, index: Int) {
        agents[index].opinion = player.opinion
        player.agents.add(agents[index])
    }
}