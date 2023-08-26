package net.rools.genetik.util

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.OptimizationType

fun <T : Any, VH> EvaluatedIndividual<out T, VH>.isBetterThan(
    individual: EvaluatedIndividual<out T, VH>,
    optimizationType: OptimizationType
): Boolean {
    return when (optimizationType) {
        OptimizationType.MAXIMIZE -> fitness > individual.fitness
        OptimizationType.MINIMIZE -> fitness < individual.fitness
    }
}