package net.rools.genetik.initializer

import net.rools.genetik.GeneticParameters
import net.rools.genetik.Individual

interface PopulationInitializer<T : Any> {
    fun <VH> initializePopulation(
        params: GeneticParameters<T, VH>
    ): List<Individual<out T, VH>>
}