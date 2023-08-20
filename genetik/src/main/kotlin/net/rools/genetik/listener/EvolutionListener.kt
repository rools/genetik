package net.rools.genetik.listener

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.Individual

interface EvolutionListener<T : Any, VH> {
    fun onEvolutionStarted() {}
    fun onEvolutionEnded() {}

    fun onNewGeneration(generation: Int) {}

    fun onBeforePopulationGenerated() {}
    fun onAfterPopulationGenerated(population: List<Individual<out T, VH>>) {}

    fun onBeforePopulationEvaluated(population: List<Individual<out T, VH>>) {}
    fun onAfterPopulationEvaluated(population: List<EvaluatedIndividual<out T, VH>>) {}
}