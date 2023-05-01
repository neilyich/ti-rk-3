package agentsprovider

import Agent

interface InitialAgentsProvider {
    fun agents(count: Int): List<Agent>
}