package net.rools.genetik.nodegenerator

import net.rools.genetik.NodeType
import kotlin.reflect.full.isSubclassOf

internal class TerminalDistanceCalculator {
    fun calculateTerminalDistances(nodeTypes: List<NodeType<*, *>>): MutableMap<NodeType<*, *>, Int> {
        val remainingTypes = nodeTypes.toMutableSet()
        val distances = mutableMapOf<NodeType<*, *>, Int>()

        var distance = 0

        while (remainingTypes.isNotEmpty()) {
            val nodesAtDistance = remainingTypes.filter { remainingType ->
                remainingType.childTypes.all { childType ->
                    distances.any { (nodeType, _) -> nodeType.type.isSubclassOf(childType) }
                }
            }

            if (nodesAtDistance.isEmpty()) {
                throw IllegalStateException("Node type ${remainingTypes.first()} has no path leading to a terminal")
            }

            remainingTypes -= nodesAtDistance.toSet()
            nodesAtDistance.forEach { distances[it] = distance }

            distance++
        }

        return distances
    }
}