package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;

public class SingleBitFlipMutation implements Mutation {
    private Random rnd;

    public SingleBitFlipMutation(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public Individual apply(Individual individual) {
        BinaryString mutated = new BinaryString((BinaryString) individual);

        int index = rnd.nextInt(mutated.getChromosome().length);
        byte value = mutated.getChromosome()[index];
        mutated.getChromosome()[index] = (byte) (1 - value);

        return mutated;
    }
}
