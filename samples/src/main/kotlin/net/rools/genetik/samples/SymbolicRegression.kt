package net.rools.genetik.samples

import net.rools.genetik.*
import net.rools.genetik.cache.NoFitnessCache
import net.rools.genetik.geneticoperation.EliteOperation
import net.rools.genetik.geneticoperation.ReproductionOperation
import net.rools.genetik.geneticoperation.SubtreeCrossoverOperation
import net.rools.genetik.geneticoperation.SubtreeMutationOperation
import net.rools.genetik.initializer.RampedHalfAndHalfInitializer
import net.rools.genetik.listener.LoggingEvolutionListener
import net.rools.genetik.termination.GenerationTerminationCriterion
import kotlin.math.pow
import kotlin.random.Random

fun main() {
    val nodeTypes = listOf<NodeType<*, in VariableHolder>>(
        function("+") { a: Number, b: Double -> a.toFloat() + b },
        function("-") { a: Double, b: Double -> a - b },
        function("^") { a: Double, b: Double -> a.pow(b) },
        function("*") { a: Double, b: Double -> a * b },
        function("/") { a: Double, b: Double -> a / b },

        variable("x") { v: VariableHolder -> v.x },

        ephemeralConstant(
            mutator = { value, factor -> value * factor },
            generator = { random -> random.nextInt(-10, 10).toDouble() }
        )
    )

    val params = GeneticParameters(
        nodeTypes = nodeTypes,
        rootType = Double::class,
        fitnessEvaluator = SymbolicRegressionEvaluator(),
        populationInitializer = RampedHalfAndHalfInitializer(),
        geneticOperations = listOf(
            EliteOperation(count = 10),
            SubtreeCrossoverOperation(probability = 0.8),
            SubtreeMutationOperation(probability = 0.25),
            ReproductionOperation(probability = 1.0)
        ),
        terminationCriterion = GenerationTerminationCriterion(100),
        random = Random(1),
        populationSize = 100_000,
        maxNodeDepth = 4,
        optimizationType = OptimizationType.MINIMIZE,
        parsimonyCoefficient = 0.1,
    )

    val runner = GeneticRunner(
        params = params,
        evolutionListeners = listOf(
            LoggingEvolutionListener()
        ),
        fitnessCache = NoFitnessCache()
    )

    runner.evolve()
}

private class SymbolicRegressionEvaluator : FitnessEvaluator<Double, VariableHolder> {
    private val values = mutableListOf<Pair<Double, Double>>()

    init {
        fun expression(x: Double) = x.pow(3) / 5 + 2 * x - 7

        var x = -5.0
        while (x < 5.0) {
            values += x to expression(x)
            x += 0.1
        }
    }

    override fun evaluate(individual: Individual<out Double, VariableHolder>): Double {
        val variables = VariableHolder(0.0)

        val mse = values.sumOf { (x, targetY) ->
            variables.x = x
            val y = individual.rootNode.evaluate(variables)
            (targetY - y).pow(2)
        }

        if (mse.isNaN()) return Double.POSITIVE_INFINITY

        return mse
    }
}

private data class VariableHolder(
    var x: Double
)