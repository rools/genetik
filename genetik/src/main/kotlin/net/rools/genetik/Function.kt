package net.rools.genetik

import kotlin.random.Random
import kotlin.reflect.KClass

abstract class FunctionType<T : Any>(
    type: KClass<T>,
    childTypes: List<KClass<*>>,
    val name: String
) : NodeType<T, Any>(type, childTypes) {

    override fun createNode(
        random: Random,
        children: List<Node<*, *, in Any>>
    ): Node<out T, out NodeType<T, Any>, Any> {
        return FunctionNode(this, children)
    }


    abstract fun evaluate(args: List<Any>): T

    // TODO?
    // fun isValid(): Boolean
}

class FunctionNode<T : Any> : Node<T, FunctionType<T>, Any> {
    constructor(type: FunctionType<T>, children: List<Node<*, *, in Any>>) : super(type, children)
    constructor(node: FunctionNode<T>, children: List<Node<*, *, in Any>>) : super(node.type, children)

    override val name: String get() = type.name

    override fun clone(children: List<Node<*, *, in Any>>): FunctionNode<T> {
        return FunctionNode(this, children)
    }

    override fun evaluate(variables: Any): T {
        return type.evaluate(children.map { it.evaluate(variables) })
    }

    override fun equals(other: Any?): Boolean {
        return other is FunctionNode<*>
                && other.type == type
                && other.children == children
    }

    override fun hashCode(): Int {
        return 31 * type.hashCode() + children.hashCode()
    }
}

inline fun <reified T : Any, reified O1> function(
    name: String, noinline operation: (O1) -> T
) = object : FunctionType<T>(
    T::class,
    listOf(O1::class),
    name
) {
    override fun evaluate(args: List<Any>): T = operation(
        args[0] as O1
    )
}

inline fun <reified T : Any, reified O1, reified O2> function(
    name: String, noinline operation: (O1, O2) -> T
) = object : FunctionType<T>(
    T::class,
    listOf(O1::class, O2::class),
    name
) {
    override fun evaluate(args: List<Any>): T = operation(
        args[0] as O1,
        args[1] as O2
    )
}

inline fun <reified T : Any, reified O1, reified O2, reified O3> function(
    name: String, noinline operation: (O1, O2, O3) -> T
) = object : FunctionType<T>(
    T::class,
    listOf(O1::class, O2::class, O3::class),
    name
) {
    override fun evaluate(args: List<Any>): T = operation(
        args[0] as O1,
        args[1] as O2,
        args[2] as O3
    )
}

inline fun <reified T : Any, reified O1, reified O2, reified O3, reified O4> function(
    name: String, noinline operation: (O1, O2, O3, O4) -> T
) = object : FunctionType<T>(
    T::class,
    listOf(O1::class, O2::class, O3::class, O4::class),
    name
) {
    override fun evaluate(args: List<Any>): T = operation(
        args[0] as O1,
        args[1] as O2,
        args[2] as O3,
        args[3] as O4
    )
}