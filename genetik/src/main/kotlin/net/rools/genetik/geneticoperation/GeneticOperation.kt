package net.rools.genetik.geneticoperation

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters
import net.rools.genetik.Individual

interface GeneticOperation<T : Any> {
    fun <VH> getIndividual(
        index: Int,
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): Individual<out T, VH>?
}