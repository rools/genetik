package net.rools.genetik.nodegenerator

import net.rools.genetik.GeneticParameters
import net.rools.genetik.Node
import net.rools.genetik.NodeType
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class FullNodeGenerator : NodeGenerator {
    private var terminalDistances = mapOf<NodeType<*, *>, Int>()

    override fun <T : Any, VH> generateNode(
        type: KClass<T>,
        maxDepth: Int,
        params: GeneticParameters<*, VH>
    ): Node<out T, out NodeType<T, in VH>, in VH> {

        val nodeType = if (maxDepth == 0) {
            params.nodeTypes.filter { it.childTypes.isEmpty() }
                .filter { it.type.isSubclassOf(type) }
                .randomOrNull(params.random)
                ?: throw IllegalStateException("No terminals provided of type $type")
        } else {
            if (terminalDistances.isEmpty()) {
                terminalDistances = TerminalDistanceCalculator().calculateTerminalDistances(params.nodeTypes)
            }

            params.nodeTypes.filter { it.childTypes.isNotEmpty() }
                .filter { it.type.isSubclassOf(type) }
                .filter { terminalDistances.getValue(it) <= maxDepth }
                .randomOrNull(params.random)
                ?: throw IllegalStateException("No functions provided of type $type")
        } as NodeType<T, VH>

        val children: List<Node<out Any, out NodeType<out Any, in VH>, in VH>> = nodeType.childTypes.map { childType ->
            generateNode(childType, maxDepth - 1, params)
        }

        return nodeType.createNode(params.random, children)
    }
}