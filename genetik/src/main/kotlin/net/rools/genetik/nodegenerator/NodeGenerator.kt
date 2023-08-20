package net.rools.genetik.nodegenerator

import net.rools.genetik.GeneticParameters
import net.rools.genetik.Node
import net.rools.genetik.NodeType
import kotlin.reflect.KClass

interface NodeGenerator {
    fun <T : Any, VH> generateNode(
        type: KClass<T>,
        maxDepth: Int,
        params: GeneticParameters<*, VH>
    ): Node<out T, out NodeType<T, in VH>, in VH>
}