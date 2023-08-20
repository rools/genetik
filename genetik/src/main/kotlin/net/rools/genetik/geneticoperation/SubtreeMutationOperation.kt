package net.rools.genetik.geneticoperation

import net.rools.genetik.*
import net.rools.genetik.nodegenerator.GrowNodeGenerator
import net.rools.genetik.nodegenerator.NodeGenerator
import net.rools.genetik.selector.Selector
import net.rools.genetik.selector.TournamentSelector
import net.rools.genetik.util.allNodes
import net.rools.genetik.util.depthOf
import net.rools.genetik.util.size
import net.rools.genetik.util.withNodeReplaced
import net.rools.genetik.NodeType

class SubtreeMutationOperation<T : Any>(
    private val probability: Double,
    private val selector: Selector<T> = TournamentSelector(),
    private val nodeGenerator: NodeGenerator = GrowNodeGenerator()
) : GeneticOperation<T> {

    override fun <VH> getIndividual(
        index: Int,
        individuals: List<EvaluatedIndividual<out T, VH>>,
        params: GeneticParameters<T, VH>
    ): Individual<out T, VH>? {

        if (params.random.nextDouble() > probability) return null

        val individual = selector.selectIndividual(individuals, params)
        val position = params.random.nextInt(individual.rootNode.size())
        val node = individual.rootNode.allNodes().elementAt(position)
        val newNode = nodeGenerator.generateNode(
            node.type.type,
            params.maxNodeDepth - individual.rootNode.depthOf(position),
            params
        )

        return Individual(individual.rootNode.withNodeReplaced(position, newNode) as Node<T, NodeType<T, VH>, VH>)
    }
}