package com.physmo.javolverexamples.programming;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategyRandomize;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

public class TestProgram {
    static int populationSize = 2500;
    static int batchSize = 10;


    public static void main(String[] args) {

        BasicDisplay bd = new BasicDisplayAwt(400, 400);

        Javolver testEvolver = new Javolver(new GeneProgram(WordEvaluator.class), populationSize)
                .keepBestIndividualAlive(false)
                .parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.1, 1))
                .addMutationStrategy(new MutationStrategySwap(0.1,1))
                //.addMutationStrategy(new MutationStrategyRandomize(0.1))
                .setSelectionStrategy(new SelectionStrategyTournament(0.15))
                .setBreedingStrategy(new BreedingStrategyCrossover());


        int iteration = 0;

        for (int j = 0; j < 50000; j++) {

            bd.startTimer();

            for (int i = 0; i < batchSize; i++) {
                testEvolver.doOneCycle();
                iteration++;
            }



            System.out.print("iteration: " + iteration + "  Time in ms: " + bd.getEllapsedTime() + "  ");
            testEvolver.report();
            System.out.println(testEvolver.findBestScoringIndividual().toString());

            Individual ind = testEvolver.findBestScoringIndividual();
            ((GeneProgram)ind).render(bd);

        }

        System.out.print("END ");
    }

}
