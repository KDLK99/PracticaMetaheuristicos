package es.uma.informatica.misia.ae.simpleea;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class SimulatedAnnealing {
    public static final String MAX_FUNCTION_EVALUATIONS_PARAM = "maxFunctionEvaluations";
    public static final String RANDOM_SEED_PARAM = "randomSeed";
    public static final String INITIAL_TEMPERATURE_PARAM = "initialTemperature";
    public static final String ALPHA_PARAM = "alpha";
    public static final String OPTIMUM_PARAM = "optimum";

    private Problem problem;
    private int functionEvaluations;
    private int maxFunctionEvaluations;
    private Random rnd;

    private Individual bestSolution;
    private Individual currentSolution;
    private int bestSolutionEvaluations;
    private double optimum;
    private Trace trace;

    private double initialTemperature;
    private double alpha;

    private int nullMoves;
    private int acceptedMoves;

    private Mutation mutation;

    public SimulatedAnnealing(Map<String, Double> parameters, Problem problem) {
        configureAlgorithm(parameters, problem);
    }

    private void configureAlgorithm(Map<String, Double> parameters, Problem problem) {
        maxFunctionEvaluations = parameters.get(MAX_FUNCTION_EVALUATIONS_PARAM).intValue();
        long randomSeed = parameters.get(RANDOM_SEED_PARAM).longValue();
        optimum = parameters.getOrDefault(OPTIMUM_PARAM, Double.POSITIVE_INFINITY);
        initialTemperature = parameters.get(INITIAL_TEMPERATURE_PARAM);
        alpha = parameters.get(ALPHA_PARAM);

        this.problem = problem;

        rnd = new Random(randomSeed);
        mutation = new SingleBitFlipMutation(rnd);
    }

    public RunResult run() {
        currentSolution = generateInitialSolution();
        double temperature = initialTemperature;
        functionEvaluations = 0;
        acceptedMoves = 0;
        nullMoves = 0;
        bestSolution = null;
        bestSolutionEvaluations = 0;
        trace = new Trace();

        evaluateIndividual(currentSolution);
        while (functionEvaluations < maxFunctionEvaluations && bestSolution.getFitness() < optimum) {
            Individual neighbor = mutation.apply(currentSolution);
            evaluateIndividual(neighbor);
            double delta = neighbor.getFitness() - currentSolution.getFitness();
            if (delta >= 0) {
                if (nullMovement(neighbor, currentSolution)) {
                    nullMoves++;
                } else {
                    currentSolution = neighbor;
                    acceptedMoves++;
                }
                
            } else {
                double acceptanceProbability = Math.exp(delta / temperature);
                if (rnd.nextDouble() < acceptanceProbability) {
                    currentSolution = neighbor;
                    acceptedMoves++;
                }
            }
            temperature *= alpha;
        }
        return new RunResult(bestSolution, functionEvaluations, bestSolutionEvaluations, trace);
    }

    private boolean nullMovement(Individual neighbor, Individual current) {
        BinaryString newBinaryString = (BinaryString) neighbor;
        BinaryString oldBinaryString = (BinaryString) current;
        
        return Arrays.equals(newBinaryString.getChromosome(), oldBinaryString.getChromosome());
    }

    public int getNullMoves() {
        return nullMoves;
    }

    public int getAcceptedMoves() {
        return acceptedMoves;
    }

    private void evaluateIndividual(Individual individual) {
        double fitness = problem.evaluate(individual);
        individual.setFitness(fitness);
        functionEvaluations++;
        checkIfBest(individual);
    }

    private void checkIfBest(Individual individual) {
        if (bestSolution == null || individual.getFitness() > bestSolution.getFitness()) {
            bestSolution = individual;
            bestSolutionEvaluations = functionEvaluations;
            trace.addEntry(bestSolutionEvaluations, bestSolution.getFitness());
        }
    }

    private Individual generateInitialSolution() {
        return problem.generateRandomIndividual(rnd);
    }
}
