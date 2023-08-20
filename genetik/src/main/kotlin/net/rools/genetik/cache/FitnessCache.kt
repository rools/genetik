package net.rools.genetik.cache

import net.rools.genetik.Node

interface FitnessCache {
    fun read(node: Node<*, *, *>): Double?
    fun write(node: Node<*, *, *>, fitness: Double)
}