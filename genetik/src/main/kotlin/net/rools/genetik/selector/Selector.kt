package net.rools.genetik.selector

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters

interface Selector<T : Any> {
    fun <VH> selectIndividual(
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): EvaluatedIndividual<out T, VH>
}