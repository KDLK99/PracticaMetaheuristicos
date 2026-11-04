package es.uma.informatica.misia.ae.simpleea;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class KnapsackMain {
    public static void main(String args[]) throws FileNotFoundException {

        if (args.length < 5) {
            System.err.println("Invalid number of arguments");
            System.err.println(
                    "Arguments: <population size> <function evaluations> <bitflip probability> <filename> <instance> [<random seed>]");
            return;
        }

        int instance = Integer.parseInt(args[4]);
        String filename = args[3];
        Knapsack problem = new Knapsack(filename, instance);

        Map<String, Double> parameters = readEAParameters(args);
        EvolutionaryAlgorithm evolutionaryAlgorithm = new EvolutionaryAlgorithm(parameters, problem);

        System.out.println("Running Knapsack problem from file: " + filename + ", instance: " + instance);
        Individual bestSolution = evolutionaryAlgorithm.run();
        System.out.println("Best fitness: " + bestSolution.getFitness() + " / optimum: " + problem.getOptimum());
    }

    private static Map<String, Double> readEAParameters(String[] args) {
        Map<String, Double> parameters = new HashMap<>();
        parameters.put(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM, Double.parseDouble(args[0]));
        parameters.put(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM, Double.parseDouble(args[1]));
        parameters.put(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM, Double.parseDouble(args[2]));

        long randomSeed = System.currentTimeMillis();
        if (args.length > 5) {
            randomSeed = Long.parseLong(args[5]);
        }
        parameters.put(EvolutionaryAlgorithm.RANDOM_SEED_PARAM, (double) randomSeed);
        return parameters;
    }
}
