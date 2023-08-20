package net.rools.genetik

open class Individual<T : Any, VH>(
    val rootNode: Node<out T, out NodeType<out T, in VH>, in VH>
)