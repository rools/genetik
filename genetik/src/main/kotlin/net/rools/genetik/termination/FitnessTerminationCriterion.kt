package net.rools.genetik.termination

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.OptimizationType

class FitnessTerminationCriterion<T : Any, VH>(
    private val fitnessThreshold: Double
) : TerminationCriterion<T, VH> {

    override fun shouldTerminate(
        optimizationType: OptimizationType,
        generation: Int,
        population: List<EvaluatedIndividual<out T, VH>>
    ): Boolean {
        return when (optimizationType) {
            OptimizationType.MAXIMIZE -> population[0].fitness >= fitnessThreshold
            OptimizationType.MINIMIZE -> population[0].fitness <= fitnessThreshold
        }
    }
}