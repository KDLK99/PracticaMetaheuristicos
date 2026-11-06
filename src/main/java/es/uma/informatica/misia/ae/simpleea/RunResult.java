package es.uma.informatica.misia.ae.simpleea;

public class RunResult {
    private final Individual bestSolution;
    private final int functionEvaluations;
    private final int bestSolutionEvaluations;
    private final Trace trace;

    public RunResult(Individual bestSolution, int functionEvaluations, int bestSolutionEvaluations, Trace trace) {
        this.bestSolution = bestSolution;
        this.functionEvaluations = functionEvaluations;
        this.bestSolutionEvaluations = bestSolutionEvaluations;
        this.trace = trace;
    }

    public Individual getBestSolution() {
        return bestSolution;
    }

    public int getFunctionEvaluations() {
        return functionEvaluations;
    }

    public int getBestSolutionEvaluations() {
        return bestSolutionEvaluations;
    }

    public Trace getTrace() {
        return trace;
    }
}
