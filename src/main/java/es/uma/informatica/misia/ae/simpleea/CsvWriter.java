package es.uma.informatica.misia.ae.simpleea;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

public class CsvWriter {
    public static final String SUMMARY_HEADER = "algorithm,instance,populationSize,bitFlipProb,crossoverProb,maxFunctionEvaluations,randomSeed,bestFitness,functionEvaluations,bestSolutionEvaluations";
    public static final String TRACE_HEADER =
        "algorithm,instance,populationSize,bitFlipProb,crossoverProb,maxFunctionEvaluations,randomSeed, evaluation,bestFitness";

    private CsvWriter() {
    }

    public static void writeSummary(PrintWriter out, String algorithmName, int instance, Map<String, Double> parameters, RunResult result) {
        int populationSize = parameters.get(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM).intValue();
        double bitFlipProb = parameters.get(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM);
        double crossoverProb = parameters.get(SinglePointCrossover.CROSSOVER_PROBABILITY_PARAM);
        long randomSeed = parameters.get(EvolutionaryAlgorithm.RANDOM_SEED_PARAM).longValue();
        int maxFunctionEvaluations = parameters.get(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM).intValue();

        out.println(String.format(Locale.US, "%s,%d,%d,%s,%s,%d,%d,%s,%d,%d",
                algorithmName, instance, populationSize, bitFlipProb, crossoverProb,
                maxFunctionEvaluations, randomSeed,
                result.getBestSolution().getFitness(), result.getFunctionEvaluations(),
                result.getBestSolutionEvaluations()));
    }

    public static void writeTrace(PrintWriter out, String algorithmName, int instance, Map<String, Double> parameters, RunResult result) {
        int populationSize = parameters.get(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM).intValue();
        double bitFlipProb = parameters.get(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM);
        double crossoverProb = parameters.get(SinglePointCrossover.CROSSOVER_PROBABILITY_PARAM);
        int maxFunctionEvaluations = parameters.get(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM).intValue();
        long randomSeed = parameters.get(EvolutionaryAlgorithm.RANDOM_SEED_PARAM).longValue();

        Trace trace = result.getTrace();
        for (int i = 0; i < trace.size(); i++) {
            out.println(String.format(Locale.US, "%s,%d,%d,%s,%s,%d,%d,%d,%s",
                    algorithmName, instance, populationSize, bitFlipProb, crossoverProb,
                    maxFunctionEvaluations, randomSeed,
                    trace.getEvaluation(i), trace.getBestFitness(i)));
        }
    }

}
