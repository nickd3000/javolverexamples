package com.physmo.javolverexamples;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySingle;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRoulette;
import com.physmo.javolverexamples.GeneHillClimb;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.*;

public class HillClimb {

    public static void main(String[] args) {

        BasicDisplay disp = new BasicDisplayAwt(400, 400);

        int populationSize = 30;

        Javolver javolver = new Javolver(new GeneHillClimb(),populationSize)
                .keepBestIndividualAlive(false)
                //.enableCompatability(0.1,5.0)
                .addMutationStrategy(new MutationStrategySingle(0.02))
                //.addMutationStrategy(new MutationStrategySimple(1.0, 0.005))
                //.setSelectionStrategy(new SelectionStrategyTournament(0.015))
                .setSelectionStrategy(new SelectionStrategyRoulette())
                //.setSelectionStrategy(new SelectionStrategyRouletteRanked())
                //.setSelectionStrategy(new SelectionStrategyRouletteRanked())
                .setBreedingStrategy(new BreedingStrategyUniform());


        for (int j = 0; j < 50000; j++) {

            // The main evolution function.
            javolver.doOneCycle();
            //Descent.descent2(javolver,2,0.6);//slidingMutationAmount);


            // Draw fittest individual every n frames.
            //if ((j%2)==0) {
            if (j>0) {
            //if (disp.getEllapsedTime()>1000/60) {
                disp.startTimer();
                // Find the best individual for drawing.
                GeneHillClimb top = (GeneHillClimb)javolver.findBestScoringIndividual();

                int pad = 50;
                disp.cls(new Color(64, 64, 64));
                top.draw(disp, 1);

                // Draw each individual in pool.
                disp.setDrawColor(Color.white);
                for (Individual i : javolver.getPool()) {
                    double x = i.dna.getDouble(0)*(double)disp.getWidth();
                    double y = i.dna.getDouble(1)*(double)disp.getWidth();;
                    disp.drawFilledCircle(x,y,3);
                }

                disp.refresh(30);
            }

        }

        System.out.print("END ");

    }

}
