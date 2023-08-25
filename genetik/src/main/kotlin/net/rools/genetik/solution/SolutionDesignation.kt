package net.rools.genetik.solution

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters

interface SolutionDesignation<T : Any, VH> {
    fun getSolution(
        population: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): EvaluatedIndividual<out T, VH>
}