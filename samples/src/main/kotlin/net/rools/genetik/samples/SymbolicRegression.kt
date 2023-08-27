package net.rools.genetik.samples

import net.rools.genetik.*
import net.rools.genetik.geneticoperation.EliteOperation
import net.rools.genetik.geneticoperation.ReproductionOperation
import net.rools.genetik.geneticoperation.SubtreeCrossoverOperation
import net.rools.genetik.geneticoperation.SubtreeMutationOperation
import net.rools.genetik.initializer.RampedHalfAndHalfInitializer
import net.rools.genetik.listener.LoggingEvolutionListener
import net.rools.genetik.solution.BestSoFarSolutionDesignation
import net.rools.genetik.termination.GenerationTerminationCriterion
import net.rools.genetik.util.toStringInfix
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
            EliteOperation(10),
            SubtreeCrossoverOperation(probability = 0.8),
            SubtreeMutationOperation(probability = 0.25),
            ReproductionOperation(probability = 1.0)
        ),
        terminationCriterion = GenerationTerminationCriterion(100),
        solutionDesignation = BestSoFarSolutionDesignation(),
        random = Random(seed = 1),
        populationSize = 5_000,
        maxNodeDepth = 4,
        optimizationType = OptimizationType.MINIMIZE,
        parsimonyCoefficient = 0.1,
    )

    val runner = GeneticRunner(
        params = params,
        evolutionListeners = listOf(
            LoggingEvolutionListener(),
            ChartingEvolutionListener()
        ),
    )

    val solution = runner.evolve()

    println("Expression: ${solution.rootNode.toStringInfix()}")
}

private class MeanSquaredErrorEvaluator : FitnessEvaluator<Int, Int> {
    private fun targetExpression(x: Int) = 4 * x * x * x + 7 * x * x - 3 * x + 5

    private val dataset: List<Pair<Int, Int>> = (-50..50).map { x ->
        x to targetExpression(x)
    }

    override fun evaluate(individual: Individual<out Int, Int>): Double {
        return dataset.sumOf { (x, expectedY) ->
            val evaluatedY = individual.rootNode.evaluate(x)
            (expectedY - evaluatedY).toDouble().pow(2)
        } / dataset.size
    }
}