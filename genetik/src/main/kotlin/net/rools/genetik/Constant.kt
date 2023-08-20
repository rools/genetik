package net.rools.genetik

import kotlin.random.Random
import kotlin.reflect.KClass

class ConstantType<T : Any>(type: KClass<T>, val name: String, val value: T) : NodeType<T, Any>(type, emptyList()) {
    override fun createNode(random: Random, children: List<Node<*, *, in Any>>): ConstantNode<T> {
        return ConstantNode(this, children)
    }
}

class ConstantNode<T : Any> : Node<T, ConstantType<T>, Any> {
    constructor(type: ConstantType<T>, children: List<Node<*, *, in Any>>) : super(type, children)
    constructor(node: ConstantNode<T>, children: List<Node<*, *, in Any>>) : super(node.type, children)

    override val name: String get() = type.name

    override fun clone(children: List<Node<*, *, in Any>>): ConstantNode<T> {
        return ConstantNode(this, children)
    }

    override fun evaluate(variables: Any): T {
        return type.value
    }
}

inline fun <reified T : Any> constant(name: String, value: T): ConstantType<T> {
    return ConstantType(T::class, name, value)
}