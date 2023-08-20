package net.rools.genetik.geneticoperation

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters
import net.rools.genetik.Individual

class EliteOperation<T : Any>(
    private val count: Int
) : GeneticOperation<T> {

    override fun <VH> getIndividual(
        index: Int,
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): Individual<out T, VH>? {
        return if (index < count) individuals[index] else null
    }
}