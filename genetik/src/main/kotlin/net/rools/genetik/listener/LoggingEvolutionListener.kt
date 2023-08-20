package net.rools.genetik.listener

import net.rools.genetik.EvaluatedIndividual
import net.rools.genetik.util.toStringTree

class LoggingEvolutionListener<T : Any, VH> : EvolutionListener<T, VH> {

    override fun onNewGeneration(generation: Int) {
        println("Generation $generation")
    }

    override fun onAfterPopulationEvaluated(population: List<EvaluatedIndividual<out T, VH>>) {
        println("Fitness: best=${population[0].fitness} worst=${population.last().fitness}")
        println(population[0].rootNode.toStringTree())
    }
}