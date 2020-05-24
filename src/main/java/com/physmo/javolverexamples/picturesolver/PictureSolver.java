package com.physmo.javolverexamples.picturesolver;

import com.physmo.javolver.Individual;
import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyCrossover;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategy;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.mutationstrategy.MutationStrategySingle;
import com.physmo.javolver.mutationstrategy.MutationStrategySwap;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRoulette;
import com.physmo.javolver.selectionstrategy.SelectionStrategyRouletteRanked;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.javolverexamples.TestLinePic;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;
import com.physmo.minvio.BasicGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PictureSolver {

    public static void restart(List<GenePicSolver> bestIndividuals, Javolver javolver) {
        GenePicSolver top = (GenePicSolver) javolver.findBestScoringIndividual();

        // Record the current best individual.
        bestIndividuals.add((GenePicSolver) top.clone());

        System.out.println("Best list size: "+bestIndividuals.size());

        // Randomise everyone
        for (Individual thing : javolver.getPool()) {
            ((GenePicSolver)thing).resetDna();
            thing.setUnprocessed();
        }

        int poolSize = javolver.getPool().size();
        int numBestIndividuals = bestIndividuals.size();

        if (numBestIndividuals<=5) return;

        for (int i=0;i<5;i++) {
            Individual thing = GetRandomIndividual(bestIndividuals);
            int target = (int)(Math.random()*(double)poolSize);
            ((GenePicSolver)(javolver.getPool().get(target))).setDnaFromOther((GenePicSolver) thing);
        }


    }

    public static GenePicSolver GetRandomIndividual(List<GenePicSolver> bestIndividuals) {
        int count = bestIndividuals.size();
        int index = (int)(Math.random()*(double)count);
        return bestIndividuals.get(index);
    }


    public static void main(String[] args) throws FileNotFoundException {

        List<GenePicSolver> bestIndividuals = new ArrayList<>();

        int populationSize = 10; //100; //60; //0;//100;
        int scoreStep = 2;

        int numberOfDrawingElements = 250; //50;
        //Class drawerClass = DnaDrawerPolys.class;
        //Class drawerClass = DnaDrawerSimpleSquares.class;
        //Class drawerClass = DnaDrawerString.class;
        Class drawerClass = DnaDrawerCircles.class;


        BufferedImage targetImage = null;
        try {
            //targetImage = ImageIO.read(new File("mona_lisa.jpg"));
            targetImage = ImageIO.read(new File(String.valueOf(TestLinePic.class.getResource("/odin.jpg").getFile())));
            //targetImage = ImageIO.read(new File("odin.jpg"));
        } catch (IOException e) {
            System.out.println("Image not found.");
        }

        BasicDisplay disp = new BasicDisplayAwt(targetImage.getWidth() * 2, targetImage.getHeight());

        BasicDisplay dispGraph = new BasicDisplayAwt(400, 200);
        BasicGraph graph = new BasicGraph(500);

        GenePicSolver gps = new GenePicSolver(targetImage, drawerClass, numberOfDrawingElements, scoreStep);

        MutationStrategy ms = new MutationStrategySimple(0.1, 0.5);

        Javolver javolver = new Javolver(gps, populationSize);
        javolver.keepBestIndividualAlive(false).parallelScoring(false)
                .addMutationStrategy(new MutationStrategySimple(0.1, 0.01))
                //.addMutationStrategy(new MutationStrategySingle(0.1))
                .addMutationStrategy(new MutationStrategySwap(0.01, 1))
                //.addMutationStrategy(ms)
                //.addMutationStrategy(new MutationStrategyRandomize(0.1))
                //.addMutationStrategy(new MutationStrategySwap(0.01, 2))
                //.addMutationStrategy(new MutationStrategyGeneBased(gps.geneIdMutationFrequency,gps.geneIdMutationAmount))
                .setSelectionStrategy(new SelectionStrategyTournament(0.2))
                //.setSelectionStrategy(new SelectionStrategyRoulette())
                //.setSelectionStrategy(new SelectionStrategyRouletteRanked())
                //.setBreedingStrategy(new BreedingStrategyUniform());
                .setBreedingStrategy(new BreedingStrategyCrossover());


        double previousScore = 0;

        double slidingMutationAmount = 1.0;

        int restartTimer = 0;

        // Perform a few iterations of evolution.
        for (int j = 0; j < 3000000; j++) {

            // Test controlling mutation with the mouse :)
            disp.tickInput();

            double uamnt = disp.getMouseX() / 200.0;
            double ufreq = disp.getMouseY() / 100.0;
            //((MutationStrategySimple)ms).amount = uamnt;
            //((MutationStrategySimple)ms).frequency = ufreq;

            slidingMutationAmount *= 0.9998;

            // Call the evolver class to perform one evolution step.
            javolver.doOneCycle();
            //Descent.descent2(testEvolver,4,0.52);//slidingMutationAmount);
            //Descent.descent3(testEvolver,4,0.05);


            if (j % 20 == 0) {
                restartTimer++;
                if (restartTimer>=100) {
                    restartTimer=0;
                    restart(bestIndividuals, javolver);
                    continue;
                }

                GenePicSolver top = (GenePicSolver) javolver.findBestScoringIndividual();
                disp.drawImage(targetImage, 0, 0);
                disp.drawImage(top.getImage(), targetImage.getWidth(), 0);
                disp.refresh();
                System.out.println("Score: " + top.getScore() + "(uamnt " + uamnt + "  freq: " + ufreq + ")" + "  M:" + slidingMutationAmount);

//				if (previousScore == top.getScore()) {
//					System.out.println("Earthquake!");
//					earthquake(testEvolver.getPool(),0.001);
//				}

                previousScore = top.getScore();

                graph.addData(top.getScore());
                dispGraph.cls(Color.WHITE);
                graph.draw(dispGraph, 0, 0, 400, 200, Color.BLUE);
                dispGraph.refresh();


            }

        }

        //disp.dr
    }

}
