package net.rools.genetik

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import net.rools.genetik.cache.FitnessCache
import net.rools.genetik.geneticoperation.GeneticOperation
import net.rools.genetik.listener.EvolutionListener
import net.rools.genetik.util.maxDepth
import net.rools.genetik.util.size
import kotlin.reflect.full.isSubclassOf

class GeneticRunner<T : Any, VH>(
    private val params: GeneticParameters<T, VH>,
    private val evolutionListeners: List<EvolutionListener<T, VH>>,
    private val fitnessCache: FitnessCache? = null,
) {

    private val parsimonySign: Float = when (params.optimizationType) {
        OptimizationType.MAXIMIZE -> -1f
        OptimizationType.MINIMIZE -> 1f
    }

    init {
        if (params.geneticOperations.isEmpty()) {
            throw IllegalArgumentException("At least one genetic operation is required")
        }
    }

    fun evolve(): EvaluatedIndividual<out T, VH> {
        evolutionListeners.forEach { listener ->
            listener.onEvolutionStarted()
            listener.onNewGeneration(0)
            listener.onBeforePopulationGenerated()
        }

        var population = params.populationInitializer.initializePopulation(params)

        evolutionListeners.forEach { it.onBeforePopulationGenerated() }

        var generation = 0

        var solution: EvaluatedIndividual<out T, VH>

        while (true) {
            evolutionListeners.forEach { it.onBeforePopulationEvaluated(population) }

            val evaluatedPopulation = evaluatePopulation(population)

            evolutionListeners.forEach { it.onAfterPopulationEvaluated(evaluatedPopulation) }

            generation++

            solution = params.solutionDesignation.getSolution(evaluatedPopulation, params)

            if (params.terminationCriterion.shouldTerminate(params.optimizationType, generation, evaluatedPopulation)) {
                break
            }

            evolutionListeners.forEach { listener ->
                listener.onBeforePopulationGenerated()
                listener.onNewGeneration(generation)
            }

            population = generateNextGeneration(evaluatedPopulation.sortedByFitness())

            evolutionListeners.forEach { it.onAfterPopulationGenerated(population) }
        }

        evolutionListeners.forEach { it.onEvolutionEnded() }

        return solution
    }

    private fun evaluatePopulation(population: List<Individual<out T, VH>>): List<EvaluatedIndividual<out T, VH>> {
        val fitnessValues = mutableMapOf<Node<out T, out NodeType<out T, in VH>, in VH>, Double>()
        val individualsToEvaluate = mutableListOf<Individual<out T, VH>>()

        population.distinct().forEach { individual ->
            when (val cachedFitness = fitnessCache?.read(individual.rootNode)) {
                null -> individualsToEvaluate += individual
                else -> fitnessValues[individual.rootNode] = cachedFitness
            }
        }

        val evaluations = individualsToEvaluate.associateParallel { individual ->
            individual.rootNode to evaluate(individual)
        }
        fitnessValues += evaluations

        fitnessCache?.apply {
            evaluations.forEach { (node, fitness) -> write(node, fitness) }
        }

        return population
            .map { EvaluatedIndividual(it.rootNode, fitnessValues.getValue(it.rootNode)) }
            .sortedByFitness()
    }

    private fun evaluate(individual: Individual<out T, VH>): Double {
        return params.fitnessEvaluator.evaluate(individual) +
                parsimonySign * params.parsimonyCoefficient * individual.rootNode.size()
    }

    private fun generateNextGeneration(population: List<EvaluatedIndividual<out T, VH>>): List<Individual<out T, VH>> {
        val nextPopulation = mutableListOf<Individual<out T, VH>>()

        while (nextPopulation.size < params.populationSize) {
            val individual = params.geneticOperations
                .firstNotNullOfOrNull { operation ->
                    operation.getIndividual(nextPopulation.size, population, params)?.also { individual ->
                        verifyIndividual(individual, operation)
                    }
                } ?: throw IllegalStateException("The last operation must always return a non-null individual")


            nextPopulation += individual
        }

        return nextPopulation
    }

    private fun <T : Any> verifyIndividual(individual: Individual<out T, VH>, operation: GeneticOperation<T>) {
        check(individual.rootNode.maxDepth() <= params.maxNodeDepth) {
            "${operation::class.qualifiedName} generated an individual with the max depth " +
                    "${individual.rootNode.maxDepth()}, but the params specify a max depth of ${params.maxNodeDepth}"
        }

        check(individual.rootNode.type.type.isSubclassOf(params.rootType)) {
            "${operation::class.qualifiedName} generated an individual with a root node of type " +
                    "${individual.rootNode.type.type.qualifiedName}, but the params specifies the " +
                    "type ${params.rootType.qualifiedName}"
        }
    }

    private fun List<EvaluatedIndividual<out T, VH>>.sortedByFitness(): List<EvaluatedIndividual<out T, VH>> {
        return when (params.optimizationType) {
            OptimizationType.MAXIMIZE -> sortedByDescending { it.fitness }
            OptimizationType.MINIMIZE -> sortedBy { it.fitness }
        }
    }
}

private fun <T, K, V> Iterable<T>.associateParallel(transform: (T) -> Pair<K, V>): Map<K, V> {
    return runBlocking {
        map { async(Dispatchers.Default) { transform(it) } }.awaitAll().toMap()
    }
}