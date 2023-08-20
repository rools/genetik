package net.rools.genetik

class EvaluatedIndividual<T : Any, VH>(
    rootNode: Node<out T, out NodeType<out T, in VH>, in VH>,
    val fitness: Double
) : Individual<T, VH>(rootNode)