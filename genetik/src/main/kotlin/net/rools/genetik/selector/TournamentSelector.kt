package net.rools.genetik.selector

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters
import net.rools.genetik.OptimizationType

class TournamentSelector<T : Any>(
    private val tournamentSize: Int = 5
) : Selector<T> {

    init {
        if (tournamentSize < 1) throw IllegalStateException("Tournament size must be at least 1")
    }

    override fun <VH> selectIndividual(
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): EvaluatedIndividual<out T, VH> {
        var best = individuals.random(params.random)

        repeat(tournamentSize - 1) {
            val individual = individuals.random(params.random)
            when (params.optimizationType) {
                OptimizationType.MAXIMIZE -> if (individual.fitness > best.fitness) best = individual
                OptimizationType.MINIMIZE -> if (individual.fitness < best.fitness) best = individual
            }
        }

        return best
    }
}