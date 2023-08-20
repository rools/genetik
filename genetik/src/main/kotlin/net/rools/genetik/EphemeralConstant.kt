package net.rools.genetik

import kotlin.random.Random
import kotlin.reflect.KClass

abstract class EphemeralConstantType<T : Any>(type: KClass<T>) : NodeType<T, Any>(type, emptyList()) {
    override fun createNode(random: Random, children: List<Node<*, *, in Any>>): EphemeralConstantNode<T> {
        return EphemeralConstantNode(this, random, children)
    }

    abstract fun generateValue(random: Random): T

    abstract fun mutated(value: T, factor: Double): T
}

class EphemeralConstantNode<T : Any> : Node<T, EphemeralConstantType<T>, Any> {
    private val value: T

    constructor(
        type: EphemeralConstantType<T>,
        random: Random,
        children: List<Node<*, *, in Any>>
    ) : super(type, children) {
        value = type.generateValue(random)
    }

    constructor(
        node: EphemeralConstantNode<T>,
        children: List<Node<*, *, in Any>>
    ) : super(node.type, children) {
        value = node.value
    }

    constructor(
        type: EphemeralConstantType<T>,
        children: List<Node<*, *, in Any>>,
        value: T
    ) : super(type, children) {
        this.value = value
    }

    override val name: String get() = value.toString()

    override fun clone(children: List<Node<*, *, in Any>>): EphemeralConstantNode<T> {
        return EphemeralConstantNode(this, children)
    }

    override fun evaluate(variables: Any): T {
        return value
    }

    override fun equals(other: Any?): Boolean {
        return other is EphemeralConstantNode<*>
                && other.value == value
                && other.children == children
    }

    override fun hashCode(): Int {
        return 31 * value.hashCode() + children.hashCode()
    }

    fun mutated(factor: Double): EphemeralConstantNode<T> {
        return EphemeralConstantNode(type, children, type.mutated(value, factor))
    }
}

inline fun <reified T : Any> ephemeralConstant(
    crossinline mutator: (value: T, factor: Double) -> T,
    crossinline generator: (random: Random) -> T
): EphemeralConstantType<T> {

    return object : EphemeralConstantType<T>(T::class) {
        override fun generateValue(random: Random): T = generator(random)
        override fun mutated(value: T, factor: Double): T = mutator(value, factor)
    }
}