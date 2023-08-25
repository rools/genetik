package net.rools.genetik

import net.rools.genetik.geneticoperation.GeneticOperation
import net.rools.genetik.initializer.PopulationInitializer
import net.rools.genetik.solution.SolutionDesignation
import net.rools.genetik.termination.TerminationCriterion
import kotlin.random.Random
import kotlin.reflect.KClass

data class GeneticParameters<T : Any, VH>(
    val nodeTypes: List<NodeType<*, in VH>>,
    val rootType: KClass<T>,
    val fitnessEvaluator: FitnessEvaluator<T, VH>,
    val populationInitializer: PopulationInitializer<T>,
    val geneticOperations: List<GeneticOperation<T>>,
    val terminationCriterion: TerminationCriterion<T, VH>,
    val solutionDesignation: SolutionDesignation<T, VH>,
    val random: Random,
    val populationSize: Int,
    val maxNodeDepth: Int,
    val optimizationType: OptimizationType,
    val parsimonyCoefficient: Double,
)