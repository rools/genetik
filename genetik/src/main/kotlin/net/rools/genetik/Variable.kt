package net.rools.genetik

import kotlin.random.Random
import kotlin.reflect.KClass

abstract class VariableType<T : Any, VH>(type: KClass<T>, val name: String) : NodeType<T, VH>(type, emptyList()) {
    override fun createNode(random: Random, children: List<Node<*, *, in VH>>): VariableNode<T, VH> {
        return VariableNode(this, children)
    }

    abstract fun evaluate(variableHolder: VH): T
}

class VariableNode<T : Any, VH> : Node<T, VariableType<T, VH>, VH> {
    constructor(type: VariableType<T, VH>, children: List<Node<*, *, in VH>>) : super(type, children)
    constructor(node: VariableNode<T, VH>, children: List<Node<*, *, in VH>>) : super(node.type, children)

    override val name: String get() = type.name

    override fun clone(children: List<Node<*, *, in VH>>): VariableNode<T, VH> {
        return VariableNode(this, children)
    }

    override fun evaluate(variables: VH): T {
        return type.evaluate(variables)
    }

    override fun equals(other: Any?): Boolean {
        return other is VariableNode<*, *>
                && other.type.name == type.name
                && other.children == children
    }

    override fun hashCode(): Int {
        return 31 * type.name.hashCode() + children.hashCode()
    }
}

inline fun <reified T : Any, VH : Any> variable(
    name: String,
    crossinline operation: (VH) -> T
): NodeType<T, VH> = object : VariableType<T, VH>(T::class, name) {
    override fun evaluate(variableHolder: VH): T = operation(variableHolder)
}
