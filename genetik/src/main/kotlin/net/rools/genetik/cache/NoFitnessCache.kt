package net.rools.genetik.cache

import net.rools.genetik.Node

class NoFitnessCache : FitnessCache {
    override fun read(node: Node<*, *, *>): Double? = null
    override fun write(node: Node<*, *, *>, fitness: Double) {}
}