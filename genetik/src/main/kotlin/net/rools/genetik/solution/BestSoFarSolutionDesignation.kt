package net.rools.genetik.solution

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters
import net.rools.genetik.OptimizationType

class BestSoFarSolutionDesignation<T : Any, VH> : SolutionDesignation<T, VH> {
    private var bestSoFarIndividual: EvaluatedIndividual<out T, VH>? = null

    override fun getSolution(
        population: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): EvaluatedIndividual<out T, VH> {
        val bestIndividual = when (params.optimizationType) {
            OptimizationType.MAXIMIZE -> population.maxBy { it.fitness }
            OptimizationType.MINIMIZE -> population.minBy { it.fitness }
        }

        val currentBestSoFar = bestSoFarIndividual

        return if (currentBestSoFar == null || bestIndividual.fitness > currentBestSoFar.fitness) {
            bestSoFarIndividual = bestIndividual
            bestIndividual
        } else {
            currentBestSoFar
        }
    }
}