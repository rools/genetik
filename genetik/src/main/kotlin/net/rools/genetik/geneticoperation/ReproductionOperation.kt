package net.rools.genetik.geneticoperation

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.GeneticParameters
import net.rools.genetik.Individual
import net.rools.genetik.selector.Selector
import net.rools.genetik.selector.TournamentSelector

class ReproductionOperation<T : Any>(
    private val probability: Double,
    private val selector: Selector<T> = TournamentSelector()
) : GeneticOperation<T> {

    override fun <VH> getIndividual(
        index: Int,
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): Individual<out T, VH>? {
        if (params.random.nextDouble() > probability) return null
        return selector.selectIndividual(individuals, params)
    }
}