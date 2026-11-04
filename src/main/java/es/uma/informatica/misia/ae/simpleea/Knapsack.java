package es.uma.informatica.misia.ae.simpleea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Knapsack implements Problem {
    private int n;
    private int m;
    private double[] values;
    private double[][] weights;
    private double[] capacities;
    private double optimum;
    private Integer[] order;

    public Knapsack(String file, int instance) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        scanner.useLocale(Locale.US);

        int numInstances = scanner.nextInt();
        if (instance < 1 || instance > numInstances) {
            scanner.close();
            throw new IllegalArgumentException("Instance must be between 1 and " + numInstances);
        }

        for (int i = 1; i <= instance; i++) {
            n = scanner.nextInt();
            m = scanner.nextInt();
            optimum = scanner.nextDouble();

            values = new double[n];
            for (int j = 0; j < n; j++) {
                values[j] = scanner.nextDouble();
            }

            weights = new double[m][n];
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < n; k++) {
                    weights[j][k] = scanner.nextDouble();
                }
            }

            capacities = new double[m];
            for (int j = 0; j < m; j++) {
                capacities[j] = scanner.nextDouble();
            }
        }
        scanner.close();

        double[] ratio = new double[n];
        for (int j = 0; j < n; j++) {
            double sum = 0;
            for (int k = 0; k < m; k++) {
                sum += weights[k][j] / capacities[k];
            }
            ratio[j] = values[j] / sum;
        }

        order = new Integer[n];
        for (int j = 0; j < n; j++) {
            order[j] = j;
        }
        Arrays.sort(order, (a, b) -> Double.compare(ratio[a], ratio[b]));
    }

    private double[] calculateTotalWeights(BinaryString individual) {
        double[] totalWeights = new double[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                totalWeights[j] += weights[j][i] * individual.getChromosome()[i];
            }
        }
        return totalWeights;
    }

    private boolean isFeasible(double[] totalWeights) {
        for (int i = 0; i < m; i++) {
            if (totalWeights[i] > capacities[i]) {
                return false;
            }
        }
        return true;
    }

    private void repair(BinaryString individual, double[] totalWeights) {
        for (int i = 0; i < n && !isFeasible(totalWeights); i++) {
            int j = order[i];
            if (individual.getChromosome()[j] == 1) {
                individual.getChromosome()[j] = 0;
                for (int k = 0; k < m; k++) {
                    totalWeights[k] -= weights[k][j];
                }
            }
        }
    }

    public double getOptimum() {
        return optimum;
    }

    public double evaluate(Individual individual) {
        BinaryString binaryString = (BinaryString) individual;
        double result = 0.0;
        double[] totalWeights = calculateTotalWeights(binaryString);

        if (!isFeasible(totalWeights)) {
            repair(binaryString, totalWeights);
        }

        for (int i = 0; i < binaryString.getChromosome().length; i++) {
            result += binaryString.getChromosome()[i] * values[i];
        }
        return result;
    }

    public BinaryString generateRandomIndividual(Random rnd) {
        return new BinaryString(n, rnd);
    }
}
