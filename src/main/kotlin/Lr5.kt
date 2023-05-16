import agentsprovider.InfluencedAgentsProvider
import agentsprovider.RandomAgentsProvider
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import trustmatrixprovider.TrustMatrix
import utils.FormattedPrinter
import utils.RandomUtils
import java.io.File

object Lr5 {

    private val mapper = ObjectMapper().apply {
        registerModule(kotlinModule())
        enable(JsonParser.Feature.ALLOW_COMMENTS)
    }

    private val task = mapper.readValue(File("lr5.json"), Lr5Task::class.java).also {
        RandomUtils.setSeed(it.seed)
        FormattedPrinter.setScale(it.formatting.scale)
    }

    private val initialAgents = RandomAgentsProvider(
        opinionsRange = 0 until 1
    ).agents(task.agentsCount)

    private val trustMatrix = TrustMatrix.createRandom(task.agentsCount).also {
        FormattedPrinter.print("Случайно сгенерированная матрица доверия", it)
    }

    fun run() {
        val players = (0 until 2).map { index ->
            Player(index + 1, 0.0)
        }
        val influencedAgents = InfluencedAgentsProvider(
            players = players,
            agents = copyInitialAgents(),
            noInfluenceProbability = task.noInfluenceProbability
        ).agents(task.agentsCount)
        for (p in players) {
            println("Агенты игрока ${p.id} (с мнением ${FormattedPrinter.roundDouble(p.opinion)}): ${p.agents.map { it.id }}")
        }
        val game = NonAntagonisticGame(
            a = task.coefs.a,
            b = task.coefs.b,
            c = task.coefs.c,
            d = task.coefs.d,
            gf = task.coefs.gf,
            gs = task.coefs.gs,
            players = players,
            trustMatrix = trustMatrix,
            agents = influencedAgents,
            e = task.maxEpsilon,
        )
//        val game = NonAntagonisticGame(
//            a = 3.0,
//            b = 4.0,
//            c = 4.0,
//            d = 3.0,
//            gf = 1.0,
//            gs = 2.0,
//            players = players,
//            trustMatrix = trustMatrix,
//            agents = influencedAgents,
//            e = task.maxEpsilon,
//        )
        game.solve()
    }

    private fun copyInitialAgents() = initialAgents.map { it.copy() }
}

fun main() {
    Lr5.run()
}
