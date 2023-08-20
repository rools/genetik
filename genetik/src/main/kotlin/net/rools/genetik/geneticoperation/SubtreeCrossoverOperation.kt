package net.rools.genetik.geneticoperation

import net.rools.genetik.*
import net.rools.genetik.selector.Selector
import net.rools.genetik.selector.TournamentSelector
import net.rools.genetik.util.allNodes
import net.rools.genetik.util.isTerminal
import net.rools.genetik.util.maxDepth
import net.rools.genetik.util.withNodeReplaced
import net.rools.genetik.NodeType

class SubtreeCrossoverOperation<T : Any>(
    private val probability: Double,
    private val selector: Selector<T> = TournamentSelector(),
    private val terminalProbability: Double = 0.1,
) : GeneticOperation<T> {

    init {
        if (terminalProbability <= 0.0 || terminalProbability > 1.0) {
            throw IllegalArgumentException("Terminal probability must be between 0.0 and 1.0")
        }
    }

    override fun <VH> getIndividual(
        index: Int,
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): Individual<out T, VH>? {

        if (params.random.nextDouble() > probability) return null

        while (true) {
            val parent1 = selector.selectIndividual(individuals, params)
            val parent2 = selector.selectIndividual(individuals, params)

            var pickTerminal = params.random.nextDouble() <= terminalProbability
            val (position1, node1) = parent1.rootNode
                .allNodes()
                .withIndex()
                .filter { it.value.isTerminal() == pickTerminal }
                .toList()
                .randomOrNull(params.random)
                ?: continue

            pickTerminal = params.random.nextDouble() <= terminalProbability
            val node2 = parent2.rootNode
                .allNodes()
                .filter { it.type.type == node1.type.type && it.isTerminal() == pickTerminal }
                .toList()
                .randomOrNull(params.random)
                ?: continue

            val newRoot = parent1.rootNode.withNodeReplaced(position1, node2) as Node<T, NodeType<T, VH>, VH>
            if (newRoot.maxDepth() > params.maxNodeDepth) continue
            return Individual(newRoot)
        }
    }
}