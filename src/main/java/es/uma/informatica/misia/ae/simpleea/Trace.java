package es.uma.informatica.misia.ae.simpleea;

import java.util.ArrayList;
import java.util.List;

public class Trace {
    private final List<Integer> functionEvaluations;
    private final List<Double> bestFitness;

    public Trace() {
        this.functionEvaluations = new ArrayList<>();
        this.bestFitness = new ArrayList<>();
    }

    public void addEntry(int evaluations, double fitness) {
        this.functionEvaluations.add(evaluations);
        this.bestFitness.add(fitness);
    }

    public int getEvaluation(int index) {
        return functionEvaluations.get(index);
    }

    public double getBestFitness(int index) {
        return bestFitness.get(index);
    }

    public int size() {
        return functionEvaluations.size();
    }
}
