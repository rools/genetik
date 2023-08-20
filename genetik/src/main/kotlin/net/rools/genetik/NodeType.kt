package net.rools.genetik

import kotlin.random.Random
import kotlin.reflect.KClass

abstract class NodeType<T : Any, VH>(
    val type: KClass<T>,
    val childTypes: List<KClass<*>>
) {
    abstract fun createNode(random: Random, children: List<Node<*, *, in VH>>): Node<out T, out NodeType<T, VH>, VH>
}