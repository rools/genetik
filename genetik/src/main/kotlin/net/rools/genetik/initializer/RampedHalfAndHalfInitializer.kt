package net.rools.genetik.initializer

import net.rools.genetik.GeneticParameters
import net.rools.genetik.Individual
import net.rools.genetik.nodegenerator.FullNodeGenerator
import net.rools.genetik.nodegenerator.GrowNodeGenerator

class RampedHalfAndHalfInitializer<T : Any> : PopulationInitializer<T> {
    private val growGenerator = GrowNodeGenerator()
    private val fullGenerator = FullNodeGenerator()

    override fun <VH> initializePopulation(
        params: GeneticParameters<T, VH>
    ): List<Individual<out T, VH>> {
        return (0..<params.populationSize).map { index ->
            val generator = if (index % 2 == 0) fullGenerator else growGenerator
            val rootNode = generator.generateNode(params.rootType, params.maxNodeDepth, params)
            Individual(rootNode)
        }
    }
}