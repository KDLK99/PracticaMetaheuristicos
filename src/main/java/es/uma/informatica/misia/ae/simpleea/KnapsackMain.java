package es.uma.informatica.misia.ae.simpleea;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class KnapsackMain {
    public static void main(String args[]) throws FileNotFoundException {

        if (args.length < 6) {
            System.err.println("Invalid number of arguments");
            System.err.println(
                    "Arguments: <population size> <function evaluations> <bitflip probability> <crossover probability> <filename> <instance> [<random seed>]");
            return;
        }

        int instance = Integer.parseInt(args[5]);
        String filename = args[4];
        Knapsack problem = new Knapsack(filename, instance);

        Map<String, Double> parameters = readEAParameters(args, problem.getOptimum());
        EvolutionaryAlgorithm evolutionaryAlgorithm = new EvolutionaryAlgorithm(parameters, problem);

        System.out.println("Running Knapsack problem from file: " + filename + ", instance: " + instance);
        RunResult result = evolutionaryAlgorithm.run();
        System.out.println("Best fitness: " + result.getBestSolution().getFitness() + " / optimum: " + problem.getOptimum());
        System.out.println("Function evaluations: " + result.getFunctionEvaluations());
        System.out.println("Best solution evaluations: " + result.getBestSolutionEvaluations());
        System.out.println("Random seed: " + parameters.get(EvolutionaryAlgorithm.RANDOM_SEED_PARAM).longValue());
    }

    private static Map<String, Double> readEAParameters(String[] args, double optimum) {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM, Double.parseDouble(args[0]));
        parameters.put(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM, Double.parseDouble(args[1]));
        parameters.put(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM, Double.parseDouble(args[2]));
        parameters.put(SinglePointCrossover.CROSSOVER_PROBABILITY_PARAM, Double.parseDouble(args[3]));
        long randomSeed = System.currentTimeMillis();
        if (args.length > 6) {
            randomSeed = Long.parseLong(args[6]);
        }
        parameters.put(EvolutionaryAlgorithm.RANDOM_SEED_PARAM, (double) randomSeed);

        if (optimum > 0) {
            parameters.put(EvolutionaryAlgorithm.OPTIMUM_PARAM, optimum);
        }

        return parameters;
    }
}
