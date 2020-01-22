package com.physmo.javolverexamples;

import com.physmo.javolver.Javolver;
import com.physmo.javolver.breedingstrategy.BreedingStrategyUniform;
import com.physmo.javolver.mutationstrategy.MutationStrategySimple;
import com.physmo.javolver.selectionstrategy.SelectionStrategyTournament;
import com.physmo.minvio.BasicDisplay;
import com.physmo.minvio.BasicDisplayAwt;

import java.awt.*;

public class SpherePacker {

	public static void main(String[] args) {


		BasicDisplay disp = new BasicDisplayAwt(300, 300);

		int populationSize = 50;
		int numberOfSpheres = 20;

		Javolver testEvolver = new Javolver(new GeneSpherePacker(numberOfSpheres),populationSize)
				.keepBestIndividualAlive(false)
				.addMutationStrategy(new MutationStrategySimple(0.1, 0.2))
				.setSelectionStrategy(new SelectionStrategyTournament(0.15))
				//.setSelectionStrategy(new SelectionStrategyRouletteRanked())
				.setBreedingStrategy(new BreedingStrategyUniform());

		
		int boxSize = 200;
		Color boxCol = Color.DARK_GRAY;
		disp.startTimer();
		
		for (int j = 0; j < 5000000; j++) {
			
			// Change the mutation amount during the simulation.
			//testEvolver.config.mutationAmount = anneal(5,0.1,15000,j);
			
			// The main evolution function.
			testEvolver.doOneCycle();
			//Descent.descent2(testEvolver, 3, 0.1);
			
			// Draw fittest individual every n frames.
			//if ((j%25)==0) {
			if (disp.getEllapsedTime()>1000/30) {
				disp.startTimer();
				// Find the best individual for drawing.
				GeneSpherePacker top = (GeneSpherePacker)testEvolver.findBestScoringIndividual();
				
				int pad = 50;
				disp.cls(new Color(64, 64, 64));
				top.draw(disp, pad,pad);
				
				disp.setDrawColor(Color.white);
				disp.drawRect(pad, pad, pad+boxSize, pad+boxSize);
				
				disp.refresh();
			}
			
			if ((j%50)==0) { // Print report every few iterations.
				GeneSpherePacker top = (GeneSpherePacker)testEvolver.findBestScoringIndividual();
				top.getCoverage();
				double coverage = top.getCoverage();
				
				System.out.println("Coverage " + coverage / (200.0*200.0) + "  Time: " + disp.getEllapsedTime() + " Score: "+top.getScore());
				//disp.startTimer();
			}
			
		}
		
		//testEvolver.runUntilMaximum();

		System.out.print("END ");
	
	}
}



