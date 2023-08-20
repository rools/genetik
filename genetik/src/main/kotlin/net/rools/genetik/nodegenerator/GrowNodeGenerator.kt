package net.rools.genetik.nodegenerator

import net.rools.genetik.GeneticParameters
import net.rools.genetik.Node
import net.rools.genetik.NodeType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

open class GrowNodeGenerator : NodeGenerator {
    private var terminalDistances = mapOf<NodeType<*, *>, Int>()

    override fun <T : Any, VH> generateNode(
        type: KClass<T>,
        maxDepth: Int,
        params: GeneticParameters<*, VH>
    ): Node<out T, out NodeType<T, in VH>, in VH> {
        if (terminalDistances.isEmpty()) {
            terminalDistances = TerminalDistanceCalculator().calculateTerminalDistances(params.nodeTypes)
        }

        val nodeType = params.nodeTypes.filter { it.type.isSubclassOf(type) }
            .filter { terminalDistances.getValue(it) <= maxDepth }
            .randomOrNull(params.random) as NodeType<T, VH>?
            ?: throw IllegalStateException("No node types provided matching the type $type")

        val children: List<Node<out Any, out NodeType<out Any, in VH>, in VH>> = nodeType.childTypes.map { childType ->
            generateNode(childType, maxDepth - 1, params)
        }

        return nodeType.createNode(params.random, children)
    }
}