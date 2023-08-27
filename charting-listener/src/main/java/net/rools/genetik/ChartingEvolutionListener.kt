package net.rools.genetik

import net.rools.genetik.listener.EvolutionListener
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.style.markers.SeriesMarkers

class ChartingEvolutionListener<T : Any, VH> : EvolutionListener<T, VH> {
    private val chart = XYChartBuilder()
        .xAxisTitle("Generation")
        .yAxisTitle("Fitness")
        .build()

    private val swingWrapper = SwingWrapper(chart).apply {
        setTitle("Genetik")
    }

    private val generationValues = mutableListOf<Int>()
    private val fitnessValues = mutableListOf<Double>()

    init {
        chart.styler.isYAxisLogarithmic = true

        swingWrapper.displayChart()
    }

    override fun onNewGeneration(generation: Int) {
        generationValues += generation
    }

    override fun onAfterPopulationEvaluated(population: List<EvaluatedIndividual<out T, VH>>) {
        fitnessValues += population[0].fitness

        if (fitnessValues.size == 1) {
            chart.addSeries("Best fitness", generationValues, fitnessValues).apply {
                marker = SeriesMarkers.NONE
            }
        } else {
            chart.updateXYSeries("Best fitness", generationValues, fitnessValues, null)
        }

        swingWrapper.repaintChart()
    }
}