package es.uma.informatica.misia.ae.simpleea;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

public class CsvWriter {
    public static final String SUMMARY_HEADER = "algorithm,instance,populationSize,bitFlipProb,crossoverProb,initialTemperature,alpha,maxFunctionEvaluations,randomSeed,bestFitness,functionEvaluations,bestSolutionEvaluations,timeMs";
    public static final String TRACE_HEADER =
        "algorithm,instance,populationSize,bitFlipProb,crossoverProb,initialTemperature,alpha,maxFunctionEvaluations,randomSeed,evaluation,bestFitness";

    private CsvWriter() {
    }

    public static void writeSummary(PrintWriter out, String algorithmName, int instance, Map<String, Double> parameters, RunResult result, long timeMs) {
        double populationSize = parameters.getOrDefault(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM, Double.NaN);
        double bitFlipProb = parameters.getOrDefault(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM, Double.NaN);
        double crossoverProb = parameters.getOrDefault(SinglePointCrossover.CROSSOVER_PROBABILITY_PARAM, Double.NaN);
        double initialTemperature = parameters.getOrDefault(SimulatedAnnealing.INITIAL_TEMPERATURE_PARAM, Double.NaN);
        double alpha = parameters.getOrDefault(SimulatedAnnealing.ALPHA_PARAM, Double.NaN);
        long randomSeed = parameters.get(EvolutionaryAlgorithm.RANDOM_SEED_PARAM).longValue();
        int maxFunctionEvaluations = parameters.get(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM).intValue();

        out.println(String.format(Locale.US, "%s,%d,%s,%s,%s,%s,%s,%d,%d,%s,%d,%d,%d",
                algorithmName, instance,
                populationSize, bitFlipProb, crossoverProb, initialTemperature, alpha,
                maxFunctionEvaluations, randomSeed,
                result.getBestSolution().getFitness(), result.getFunctionEvaluations(), result.getBestSolutionEvaluations(),
                timeMs));
    }

    public static void writeTrace(PrintWriter out, String algorithmName, int instance, Map<String, Double> parameters, RunResult result) {
        double populationSize = parameters.getOrDefault(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM, Double.NaN);
        double bitFlipProb = parameters.getOrDefault(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM, Double.NaN);
        double crossoverProb = parameters.getOrDefault(SinglePointCrossover.CROSSOVER_PROBABILITY_PARAM, Double.NaN);
        double initialTemperature = parameters.getOrDefault(SimulatedAnnealing.INITIAL_TEMPERATURE_PARAM, Double.NaN);
        double alpha = parameters.getOrDefault(SimulatedAnnealing.ALPHA_PARAM, Double.NaN);
        int maxFunctionEvaluations = parameters.get(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM).intValue();
        long randomSeed = parameters.get(EvolutionaryAlgorithm.RANDOM_SEED_PARAM).longValue();

        Trace trace = result.getTrace();
        for (int i = 0; i < trace.size(); i++) {
            out.println(String.format(Locale.US, "%s,%d,%s,%s,%s,%s,%s,%d,%d,%d,%s",
                    algorithmName, instance,
                    populationSize, bitFlipProb, crossoverProb, initialTemperature, alpha,
                    maxFunctionEvaluations, randomSeed,
                    trace.getEvaluation(i), trace.getBestFitness(i)));
        }
    }

}
