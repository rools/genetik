package net.rools.genetik.termination

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.OptimizationType

class GenerationTerminationCriterion<T : Any, VH>(
    private val generationCount: Int
) : TerminationCriterion<T, VH> {

    override fun shouldTerminate(
        optimizationType: OptimizationType,
        generation: Int,
        population: List<EvaluatedIndividual<out T, VH>>
    ): Boolean {
        return generation >= generationCount
    }
}