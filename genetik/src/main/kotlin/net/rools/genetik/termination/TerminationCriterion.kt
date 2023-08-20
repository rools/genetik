package net.rools.genetik.termination

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.OptimizationType

interface TerminationCriterion<T : Any, VH> {
    fun shouldTerminate(
        optimizationType: OptimizationType,
        generation: Int,
        population: List<EvaluatedIndividual<out T, VH>>
    ): Boolean
}