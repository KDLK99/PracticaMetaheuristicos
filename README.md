This project is a modified version of [SimpleEvolutionaryAlgorithm](https://github.com/NEO-Research-Group/SimpleEvolutionaryAlgorithm) by Francisco Chicano (NEO Research Group, University of Malaga), based on commit `a6f44d4`.
The original is a simple evolutionary algorithm in Java created for didactical purposes.

This program is free software, distributed under the terms of the GNU General Public License
version 3. See [LICENSE](LICENSE).

## Changes from the original (November 2026)

- `Knapsack`: multidimensional knapsack problem (OR-Library mknap format)
  with a DROP repair operator.
- Crossover probability added to `SinglePointCrossover`.
- Stop when a known optimum is reached, `RunResult` and improvement `Trace`.
- `SimulatedAnnealing` with `SingleBitFlipMutation` (new trajectory algorithm).
- Experiment drivers (`KnapsackExperiment`, `SAExperiment`, `FinalExperiment`)
  and `CsvWriter` for CSV output.
- The original `Main` (Onemax example) was removed.