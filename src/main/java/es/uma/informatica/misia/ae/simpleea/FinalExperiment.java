package es.uma.informatica.misia.ae.simpleea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class FinalExperiment {
    private static final int POPULATION_SIZE = 100;
    private static final double BIT_FLIP_PROBABILITY = 0.12;
    private static final double CROSSOVER_PROBABILITY = 1.0;

    private static final double INITIAL_TEMPERATURE = 200.0;
    private static final double ALPHA = 0.99999;

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length < 5) {
            System.err.println("Invalid number of arguments");
            System.err.println(
                    "Arguments: <filename> <instance> <max evaluations> <number of seeds> <output directory> [initial random seed]");
            return;
        }

        String filename = args[0];
        int instance = Integer.parseInt(args[1]);
        int maxEvaluations = Integer.parseInt(args[2]);
        int numSeeds = Integer.parseInt(args[3]);
        String outputDir = args[4];
        int initialSeed = args.length > 5 ? Integer.parseInt(args[5]) : 1;

        Knapsack problem = new Knapsack(filename, instance);
        new File(outputDir).mkdirs();
        PrintWriter summaryOut = new PrintWriter(new File(outputDir, "final_summary.csv"));
        PrintWriter traceOut = new PrintWriter(new File(outputDir, "final_trace.csv"));
        summaryOut.println(CsvWriter.SUMMARY_HEADER);
        traceOut.println(CsvWriter.TRACE_HEADER);

        for (int i = 0; i < numSeeds; i++) {
            Map<String, Double> parametersSA = buildParametersSA(
                    maxEvaluations, INITIAL_TEMPERATURE, ALPHA, initialSeed + i, problem.getOptimum());
            SimulatedAnnealing sa = new SimulatedAnnealing(parametersSA, problem);
            long start = System.nanoTime();
            RunResult result = sa.run();
            long timeMs = (System.nanoTime() - start) / 1_000_000;

            CsvWriter.writeSummary(summaryOut, "SA", instance, parametersSA, result, timeMs);
            CsvWriter.writeTrace(traceOut, "SA", instance, parametersSA, result);
            summaryOut.flush();
            traceOut.flush();

            Map<String, Double> parametersGA = buildParametersGA(
                POPULATION_SIZE,maxEvaluations, BIT_FLIP_PROBABILITY, CROSSOVER_PROBABILITY, initialSeed + i, problem.getOptimum());
            EvolutionaryAlgorithm ga = new EvolutionaryAlgorithm(parametersGA, problem);
            start = System.nanoTime();
            result = ga.run();
            timeMs = (System.nanoTime() - start) / 1_000_000;

            CsvWriter.writeSummary(summaryOut, "GA", instance, parametersGA, result, timeMs);
            CsvWriter.writeTrace(traceOut, "GA", instance, parametersGA, result);
            summaryOut.flush();
            traceOut.flush();

            System.out.println("Done: seed=" + (initialSeed + i));
        }

        summaryOut.close();
        traceOut.close();
    }

    private static Map<String, Double> buildParametersSA(int maxEvaluations,
            double initialTemp, double alpha, long seed, double optimum) {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put(SimulatedAnnealing.MAX_FUNCTION_EVALUATIONS_PARAM, (double) maxEvaluations);
        parameters.put(SimulatedAnnealing.RANDOM_SEED_PARAM, (double) seed);
        parameters.put(SimulatedAnnealing.INITIAL_TEMPERATURE_PARAM, initialTemp);
        parameters.put(SimulatedAnnealing.ALPHA_PARAM, alpha);

        if (optimum > 0) {
            parameters.put(SimulatedAnnealing.OPTIMUM_PARAM, optimum);
        }
        return parameters;
    }

    private static Map<String, Double> buildParametersGA(int populationSize, int maxEvaluations,
            double bitFlipProb, double crossoverProb, long seed, double optimum) {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM, (double) populationSize);
        parameters.put(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM, (double) maxEvaluations);
        parameters.put(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM, bitFlipProb);
        parameters.put(SinglePointCrossover.CROSSOVER_PROBABILITY_PARAM, crossoverProb);
        parameters.put(EvolutionaryAlgorithm.RANDOM_SEED_PARAM, (double) seed);
        if (optimum > 0) {
            parameters.put(EvolutionaryAlgorithm.OPTIMUM_PARAM, optimum);
        }
        return parameters;
    }
}
