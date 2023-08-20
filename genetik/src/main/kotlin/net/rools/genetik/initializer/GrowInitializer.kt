package net.rools.genetik.initializer

import net.rools.genetik.GeneticParameters
import net.rools.genetik.Individual
import net.rools.genetik.nodegenerator.GrowNodeGenerator

class GrowInitializer<T : Any> : PopulationInitializer<T> {
    private val growGenerator = GrowNodeGenerator()

    override fun <VH> initializePopulation(
        params: GeneticParameters<T, VH>
    ): List<Individual<out T, VH>> {
        return (0..<params.populationSize).map {
            val rootNode = growGenerator.generateNode(params.rootType, params.maxNodeDepth, params)
            Individual(rootNode)
        }
    }
}