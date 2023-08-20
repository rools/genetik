package net.rools.genetik.geneticoperation

import net.rools.genetik.*
import net.rools.genetik.selector.Selector
import net.rools.genetik.selector.TournamentSelector
import net.rools.genetik.util.allNodes
import net.rools.genetik.util.withNodeReplaced
import net.rools.genetik.NodeType
import kotlin.random.Random
import kotlin.random.asJavaRandom

class ConstantMutationOperation<T : Any>(
    private val probability: Double,
    private val selector: Selector<T> = TournamentSelector(),
    private val factorGenerator: (random: Random) -> Double = { it.asJavaRandom().nextGaussian() * 0.01 + 1 }
) : GeneticOperation<T> {

    override fun <VH> getIndividual(
        index: Int,
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): Individual<out T, VH>? {

        if (params.random.nextDouble() > probability) return null

        val individual = selector.selectIndividual(individuals, params)

        val (position, node) = individual.rootNode
            .allNodes()
            .mapIndexedNotNull { i, node -> if (node is EphemeralConstantNode<*>) i to node else null }
            .toList()
            .randomOrNull(params.random)
            ?: return individual

        val factor = factorGenerator(params.random)

        val newRoot = individual.rootNode.withNodeReplaced(position, node.mutated(factor))

        return Individual(newRoot as Node<T, NodeType<T, VH>, VH>)
    }
}