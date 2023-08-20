package net.rools.genetik.util

import net.rools.genetik.Node

fun Node<*, *, *>.toStringInfix(): String = when (children.size) {
    0 -> name
    2 -> "(${children[0].toStringInfix()} $name ${children[1].toStringInfix()})"
    else -> "$name(${children.joinToString { it.toStringInfix() }})"
}

fun Node<*, *, *>.toStringTree(): String {
    fun Node<*, *, *>.toStringTreeInternal(): List<String> {
        return listOf(name) +
                children.withIndex().flatMap { (childIndex, node) ->
                    node.toStringTreeInternal().mapIndexed { index, string ->
                        val char = when (childIndex) {
                            children.lastIndex -> if (index == 0) "└" else " "
                            else -> if (index == 0) "├" else "│"
                        }
                        "$char $string"
                    }
                }
    }
    return toStringTreeInternal().joinToString("\n")
}

fun <VH> Node<*, *, in VH>.allNodes(): Sequence<Node<*, *, in VH>> = sequence {
    suspend fun SequenceScope<Node<*, *, in VH>>.allNodes(node: Node<*, *, in VH>) {
        yield(node)
        node.children.forEach { allNodes(it) }
    }
    allNodes(this@allNodes)
}

fun Node<*, *, *>.maxDepth(): Int {
    fun Node<*, *, *>.maxDepth(currentDepth: Int): Int {
        return children.maxOfOrNull { it.maxDepth(currentDepth + 1) } ?: currentDepth
    }
    return maxDepth(0)
}

fun Node<*, *, *>.size(): Int {
    return 1 + children.sumOf { it.size() }
}

fun Node<*, *, *>.depthOf(position: Int): Int {
    var currentPosition = 0

    fun Node<*, *, *>.depthOf(position: Int, currentDepth: Int): Int? {
        if (currentPosition++ == position) return currentDepth
        return children.firstNotNullOfOrNull { it.depthOf(position, currentDepth + 1) }
    }

    return depthOf(position, 0) ?: throw IllegalStateException("Node is unreachable from this")
}

fun Node<*, *, *>.isTerminal(): Boolean = children.isEmpty()

fun <VH> Node<*, *, VH>.withNodeReplaced(
    position: Int,
    newNode: Node<*, *, *>
): Node<*, *, VH> {
    var currentPosition = 0

    fun <VH> Node<*, *, VH>.withNodeReplacedInternal(
        position: Int,
        newNode: Node<*, *, *>
    ): Node<*, *, VH> {
        if (currentPosition++ == position) return newNode as Node<*, *, VH>
        return clone(children.map { it.withNodeReplacedInternal(position, newNode) })
    }

    return withNodeReplacedInternal(position, newNode)
}