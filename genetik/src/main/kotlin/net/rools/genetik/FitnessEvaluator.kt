package net.rools.genetik

interface FitnessEvaluator<T : Any, VH> {
    fun evaluate(individual: Individual<out T, VH>): Double
}