package es.uma.informatica.misia.ae.simpleea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class KnapsackExperiment {
    public static void main(String[] args) throws FileNotFoundException {
        
        if (args.length < 6) {
            System.err.println("Invalid number of arguments");
            System.err.println(
                    "Arguments: <filename> <instance> <population size> <max evaluations> <number of seeds> <output directory> [initial random seed]");
            return;
        }


        String filename = args[0];
        int instance = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        int maxEvaluations = Integer.parseInt(args[3]);
        int numSeeds = Integer.parseInt(args[4]);
        String outputDir = args[5];
        int initialSeed = args.length > 6 ? Integer.parseInt(args[6]) : 1;

        Knapsack problem = new Knapsack(filename, instance);

        double[] crossoverProbs = { 0.2, 0.5, 0.8, 1.0 };
        double[] bitFlipProbs = { 0.08, 0.12, 0.16};

        new File(outputDir).mkdirs();
        PrintWriter summaryOut = new PrintWriter(new File(outputDir, "ga_summary.csv"));
        PrintWriter traceOut = new PrintWriter(new File(outputDir, "ga_trace.csv"));
        summaryOut.println(CsvWriter.SUMMARY_HEADER);
        traceOut.println(CsvWriter.TRACE_HEADER);

        for (double pc : crossoverProbs) {
            for (double pm : bitFlipProbs) {
                for (int i = 0; i < numSeeds; i++) {
                    Map<String, Double> parameters = buildParameters(
                            populationSize, maxEvaluations, pm, pc, initialSeed + i, problem.getOptimum());
                    EvolutionaryAlgorithm ga = new EvolutionaryAlgorithm(parameters, problem);
                    RunResult result = ga.run();

                    CsvWriter.writeSummary(summaryOut, "GA", instance, parameters, result);
                    CsvWriter.writeTrace(traceOut, "GA", instance, parameters, result);
                    summaryOut.flush();
                    traceOut.flush();
                }
                System.out.println("Done: pc=" + pc + " pm=" + pm);
            }
        }

        summaryOut.close();
        traceOut.close();
    }

    private static Map<String, Double> buildParameters(int populationSize, int maxEvaluations,
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
