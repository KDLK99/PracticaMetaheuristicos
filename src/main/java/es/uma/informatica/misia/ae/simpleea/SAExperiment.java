package es.uma.informatica.misia.ae.simpleea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class SAExperiment {
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

        double[] initialTemperatures = { 25, 50, 100, 200, 400, 800, 1600, 3200, 6400 };
        double[] finalTemperatures = { 0.1, 0.5, 1, 2, 5, 10, 25, 50, 100, 200 };

        new File(outputDir).mkdirs();
        PrintWriter summaryOut = new PrintWriter(new File(outputDir, "sa_summary.csv"));
        PrintWriter traceOut = new PrintWriter(new File(outputDir, "sa_trace.csv"));
        summaryOut.println(CsvWriter.SUMMARY_HEADER);
        traceOut.println(CsvWriter.TRACE_HEADER);

        for (double initialTemp : initialTemperatures) {
            for (double finalTemp : finalTemperatures) {
                if (finalTemp > initialTemp) {
                    continue;
                }
                double alpha = Math.pow(finalTemp / initialTemp, 1.0 / maxEvaluations);
                for (int i = 0; i < numSeeds; i++) {
                    Map<String, Double> parameters = buildParameters(
                            maxEvaluations, initialTemp, alpha, initialSeed + i, problem.getOptimum());
                    SimulatedAnnealing sa = new SimulatedAnnealing(parameters, problem);
                    long start = System.nanoTime();
                    RunResult result = sa.run();
                    long timeMs = (System.nanoTime() - start) / 1_000_000;

                    CsvWriter.writeSummary(summaryOut, "SA", instance, parameters, result, timeMs);
                    CsvWriter.writeTrace(traceOut, "SA", instance, parameters, result);
                    summaryOut.flush();
                    traceOut.flush();
                }
                System.out.println("Done: initialTemp=" + initialTemp + " alpha=" + alpha + " finalTemp=" + finalTemp);
            }
        }

        summaryOut.close();
        traceOut.close();
    }

    private static Map<String, Double> buildParameters(int maxEvaluations,
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
}
