# Genetik

A genetic programming implementation written in Kotlin.

## Sample

Below is a symbolic regression sample that will attempt to evolve mathematical expressions using genetik. The target expression in this sample is $`f(x) = 7*x*x - 3*x + 5`$.

The first step is to specify the function and terminal set for the problem. This sample specifies three basic arithmetic expressions, a variable *x* and an *ephemeral random constant* ranging from 1 to 10:

```kotlin
val nodeTypes = listOf<NodeType<*, in Int>>(
    function("+") { a: Int, b: Int -> a + b },
    function("-") { a: Int, b: Int -> a - b },
    function("*") { a: Int, b: Int -> a * b },

    variable("x") { x: Int -> x },

    ephemeralConstant { random -> random.nextInt(1, 10) }
)
```

Then a `FitnessEvaluator` is defined which generates a dataset from the target expression. Candidate expressions will be evaluated over this dataset, and the mean squared error will be used as our fitness function:

```kotlin
class MeanSquaredErrorEvaluator : FitnessEvaluator<Int, Int> {
    private fun targetExpression(x: Int) =  7 * x * x - 3 * x + 5

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
```

The last preparatory step is to define the remaining parameters for the run, most of which should be self-explanatory:

```kotlin
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
    solutionDesignation = BestSoFarSolutionDesignation(),
    random = Random(seed = 1),
    populationSize = 100_000,
    maxNodeDepth = 4,
    optimizationType = OptimizationType.MINIMIZE,
    parsimonyCoefficient = 0.1,
)
```

Everything needed to evolve some expressions is now set up. The parameters is passed to a `GeneticRunner`, and the evolution is started:

```kotlin
val runner = GeneticRunner(params)
val solution = runner.evolve()

println("Expression: ${solution.rootNode.toStringInfix()}")
```

If all goes well, the solution expression should be equivalent to (but not necessarily the same as) the target expression:

```
Expression: (5 + (((7 * x) - 3) * x))
```

## License

    Copyright 2023 Robert Olsson

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.