package net.rools.genetik.selector

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters

class RandomSelector<T : Any> : Selector<T> {
    override fun <VH> selectIndividual(
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): EvaluatedIndividual<out T, VH> {
        return individuals.random(params.random)
    }
}