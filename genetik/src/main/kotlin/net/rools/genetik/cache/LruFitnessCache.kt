package net.rools.genetik.cache

import net.rools.genetik.Node

class LruFitnessCache(maxSize: Int) : FitnessCache {
    private val fitnessValues = LruMap<Node<*, *, *>, Double>(maxSize)

    override fun read(node: Node<*, *, *>): Double? {
        return fitnessValues[node]
    }

    override fun write(node: Node<*, *, *>, fitness: Double) {
        fitnessValues[node] = fitness
    }

    private class LruMap<K, V>(
        private val maxSize: Int
    ) : LinkedHashMap<K, V>(maxSize, 1f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>): Boolean {
            return size > maxSize
        }
    }
}