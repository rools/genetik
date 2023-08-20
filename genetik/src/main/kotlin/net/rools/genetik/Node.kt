package net.rools.genetik

abstract class Node<T : Any, NT : NodeType<T, VH>, VH>(
    val type: NT,
    val children: List<Node<*, *, in VH>>
) {
    abstract val name: String
    abstract fun clone(children: List<Node<*, *, in VH>>): Node<T, NT, VH>
    abstract fun evaluate(variables: VH): T
}