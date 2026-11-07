package es.uma.informatica.misia.ae.simpleea;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class SAMain {
    public static void main(String args[]) throws FileNotFoundException {

        if (args.length < 5) {
            System.err.println("Invalid number of arguments");
            System.err.println(
                    "Arguments: <initial temperature> <alpha> <function evaluations> <filename> <instance> [<random seed>]");
            return;
        }

        int instance = Integer.parseInt(args[4]);
        String filename = args[3];
        Knapsack problem = new Knapsack(filename, instance);

        Map<String, Double> parameters = readEAParameters(args, problem.getOptimum());
        SimulatedAnnealing evolutionaryAlgorithm = new SimulatedAnnealing(parameters, problem);

        System.out.println("Running Knapsack problem from file: " + filename + ", instance: " + instance);
        RunResult result = evolutionaryAlgorithm.run();
        System.out.println("Best fitness: " + result.getBestSolution().getFitness() + " / optimum: " + problem.getOptimum());
        System.out.println("Function evaluations: " + result.getFunctionEvaluations());
        System.out.println("Best solution evaluations: " + result.getBestSolutionEvaluations());
        System.out.println("Random seed: " + parameters.get(EvolutionaryAlgorithm.RANDOM_SEED_PARAM).longValue());
        System.out.println("Accepted moves: " + evolutionaryAlgorithm.getAcceptedMoves());
        System.out.println("Null moves: " + evolutionaryAlgorithm.getNullMoves());
    }

    private static Map<String, Double> readEAParameters(String[] args, double optimum) {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put(SimulatedAnnealing.INITIAL_TEMPERATURE_PARAM, Double.parseDouble(args[0]));
        parameters.put(SimulatedAnnealing.ALPHA_PARAM, Double.parseDouble(args[1]));
        parameters.put(SimulatedAnnealing.MAX_FUNCTION_EVALUATIONS_PARAM, Double.parseDouble(args[2]));
    
        long randomSeed = System.currentTimeMillis();
        if (args.length > 5) {
            randomSeed = Long.parseLong(args[5]);
        }
        parameters.put(EvolutionaryAlgorithm.RANDOM_SEED_PARAM, (double) randomSeed);

        if (optimum > 0) {
            parameters.put(EvolutionaryAlgorithm.OPTIMUM_PARAM, optimum);
        }

        return parameters;
    }
}
