package net.rools.genetik.solution

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters
import net.rools.genetik.OptimizationType
import net.rools.genetik.util.isBetterThan

class BestSoFarSolutionDesignation<T : Any, VH> : SolutionDesignation<T, VH> {
    private var solution: EvaluatedIndividual<out T, VH>? = null

    override fun getSolution(
        population: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): EvaluatedIndividual<out T, VH> {
        val best = when (params.optimizationType) {
            OptimizationType.MAXIMIZE -> population.maxBy { it.fitness }
            OptimizationType.MINIMIZE -> population.minBy { it.fitness }
        }

        val currentSolution = solution

        return if (currentSolution == null || best.isBetterThan(currentSolution, params.optimizationType)) {
            solution = best
            best
        } else {
            currentSolution
        }
    }
}