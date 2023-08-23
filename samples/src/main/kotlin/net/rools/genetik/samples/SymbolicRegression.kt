package net.rools.genetik.samples

import net.rools.genetik.*
import net.rools.genetik.geneticoperation.ReproductionOperation
import net.rools.genetik.geneticoperation.SubtreeCrossoverOperation
import net.rools.genetik.geneticoperation.SubtreeMutationOperation
import net.rools.genetik.initializer.RampedHalfAndHalfInitializer
import net.rools.genetik.listener.LoggingEvolutionListener
import net.rools.genetik.termination.GenerationTerminationCriterion
import kotlin.math.pow
import kotlin.random.Random

fun main() {
    val nodeTypes = listOf<NodeType<*, in Int>>(
        function("+") { a: Int, b: Int -> a + b },
        function("-") { a: Int, b: Int -> a - b },
        function("*") { a: Int, b: Int -> a * b },

        variable("x") { x: Int -> x },

        ephemeralConstant { random -> random.nextInt(1, 10) }
    )

    val params = GeneticParameters(
        nodeTypes = nodeTypes,
        rootType = Int::class,
        fitnessEvaluator = MeanSquaredErrorEvaluator(),
        populationInitializer = RampedHalfAndHalfInitializer(),
        geneticOperations = listOf(
            SubtreeCrossoverOperation(probability = 0.8),
            SubtreeMutationOperation(probability = 0.25),
            ReproductionOperation(probability = 1.0)
        ),
        terminationCriterion = GenerationTerminationCriterion(100),
        random = Random(seed = 1),
        populationSize = 100_000,
        maxNodeDepth = 4,
        optimizationType = OptimizationType.MINIMIZE,
        parsimonyCoefficient = 0.1,
    )

    val runner = GeneticRunner(
        params = params,
        evolutionListeners = listOf(LoggingEvolutionListener()),
    )

    runner.evolve()
}

private class MeanSquaredErrorEvaluator : FitnessEvaluator<Int, Int> {
    private fun targetExpression(x: Int) = (7 * x - 3) * x + 5

    private val dataset: List<Pair<Int, Int>> = (-10..10).map { x ->
        x to targetExpression(x)
    }

    override fun evaluate(individual: Individual<out Int, Int>): Double {
        return dataset.sumOf { (x, expectedY) ->
            val evaluatedY = individual.rootNode.evaluate(x)
            (expectedY - evaluatedY).toDouble().pow(2)
        } / dataset.size
    }
}