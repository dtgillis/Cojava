package simulator;

import geneConversion.gc;
import haplos.hap;
import historical.histWorker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

import pointers.doublePointer;
import recomb.recListMaker;

import cosiRand.poisson;
import cosiRand.randomNum;

import migration.migrationWorker;
import mutate.mutList;
import mutate.mutations;
//import mutate.mutations;

import coalesce.coalesce;
import coalescent.CoalescentMain;
import demography.TreeTime2;
import demography.demography;

public class sim {
		double coalesceRate,migrateRate,geneConvRate,recombRate,poissonRate,theta;
		int fixedNumMut;
		poisson poissoner;
		final boolean DEBUG = false;
		demography dem;
		gc geneConversion;
		recListMaker recomb;
		randomNum random;
		histWorker histFactory;
		coalesce coalesce;
		migrationWorker migFactory;
		mutations mutate;
		
		
		public sim(demography adem, poisson aPois, gc aGeneConverter,recListMaker aRecomb,randomNum aRNG,histWorker aHistFact,
					coalesce aCoal,migrationWorker aMigFactory,mutations aMutate){
			fixedNumMut = -1;
			poissoner = aPois;
			dem = adem;
			geneConversion = aGeneConverter;
			recomb = aRecomb;
			random = aRNG;
			histFactory = aHistFact;
			coalesce = aCoal;
			migFactory = aMigFactory;
			mutate = aMutate;
		}
		public void simSetLength(int l){
			recomb.setLength(l);
			geneConversion.setLength(l);
		}
		/* To execute the coalescent simulator:
		 * 1. Calculate the times to the next historical and poisson events.
		 * 2. Choose the closest event, and execute it.
		 * 3. Repeat until sim_complete().
		 * 
		 * Important assumption:
		 * Historical events are appropriately spaced such that
		 * historical_event_time will never be negative unless
		 * there are no historical events left.
		 *
		 */
		public double simExecute() throws InterruptedException{
			double gen = 0;
			double historical_event_time;
			double  poisson_event_time;	
			boolean coal = false;
			boolean complete_flag = false;	

			while (!complete_flag) {
			  coal = false;
			  historical_event_time = (double) simGetHistEvent(gen);
			  poisson_event_time = simGetPoisEvent();
			  
			  if (DEBUG) {
			    System.out.print(String.format("recomb: %f coalesce: %f migrate: %f geneconvert: %f\n", 
				   recombRate, coalesceRate, migrateRate, geneConvRate));
			    System.out.print(String.format("poisson time: %f, hist time: %f\n", 
				   poisson_event_time, historical_event_time));
			  }
			  
			  if (historical_event_time < 0 
			      || poisson_event_time < historical_event_time) {
			    gen += poisson_event_time;
			    coal = simDoPoisson (gen);
			    if (coal) {complete_flag = dem.doneCoalescent2();}
			  }
			  else {
			    gen += historical_event_time;
			    gen = histFactory.historicalEventExecute(gen);
			    complete_flag = dem.doneCoalescent2(); 
			  }

			}
			return gen;
			
		}
		/* This is called after sim_execute is finished. */
		/*
		 * A region is defined as the sequence between two recombination
		 * events.
		 *
		 * 1. Calculate the total time in the tree for all the branches for
		 *    all regions.
		 * 2. Mutations are poisson distributed, with lambda specified by
		 *    the mutation rate. Each region has a number of mutations 
		 *    proportional to the time spent in the tree for that region.
		 * 3. For each mutation in that region, choose a random location
		 *    within that region, and call a function that places the
		 *    mutation.
		 */
		
		public int simMutate(File aFile, mutList aMutList, hap aHap) throws IOException{
			dem.setRecombArray();
			int i,summut = 0;
			double mutrate, probSum = 0;
			double loc;
			int numRegions,reg,numMuts = 0;
			double randMark,begin;
			double[] reglen,probRegion,treeTime;
			int[] nMutByRegion = null;
			numRegions = dem.getNumRegs();
			reglen = new double[numRegions];
			//double[] regTest = dem.getRegLengthArray();
			treeTime = new double[numRegions];
//			reglen = dem.getRegLengthArray();
			for(reg= 0;reg<numRegions;reg++){
				//treeTime[reg] = dem.totalTreeTime(reg);//this call is now parallel
				reglen[reg] = dem.getRecombArray()[reg+1] - dem.getRecombArray()[reg];
				probSum += treeTime[reg] + reglen[reg];
			}
			
			CoalescentMain.pool.invoke(new TreeTime2(0,numRegions,treeTime,dem));
			if(fixedNumMut>0){
				probRegion = new double[numRegions];
				nMutByRegion = new int[numRegions];
				for(reg = 0;reg<numRegions;reg++){
					probRegion[reg] = treeTime[reg]*reglen[reg] /probSum;
				}
				cosiRand.multiNom.multinom(numRegions, fixedNumMut, probRegion, nMutByRegion);
			}
			for(reg=0;reg<numRegions;reg++){
				BufferedWriter out = null;
				if(aFile!=null){
				FileWriter fileOut = new FileWriter(aFile.getName(),true);
				out = new BufferedWriter(fileOut);
				}
				
				begin = dem.getRecombArray()[reg];
				//double begin1 = dem.getRecombArray()[reg];
				if(fixedNumMut==-1){
					mutrate = theta * treeTime[reg] * reglen[reg];
					numMuts = poissoner.poission(mutrate);
				if(aFile!=null){	
						out.write(String.format("> [%f,  %f]  time %d E[muts] = %f (%d)\n",
								begin,reglen[reg],(int)treeTime[reg],mutrate,numMuts));
				}
				}
					
				else{ 
					numMuts = nMutByRegion[reg];
					if(aFile.canWrite()){
						out.write(String.format("> [%f, %f]  time %d E[muts] = %f (%d)\n",
									begin,reglen[reg],(int) treeTime[reg],0,numMuts));
					}
					
					
				}
				if(out!=null)
					out.close();
				
				summut += numMuts;
				
				for(i=0;i<numMuts;i++){
					loc = begin + random.randomDouble()*reglen[reg];
					randMark = random.randomDouble();
					mutate.mutateFindAndPrint(aFile, reg, loc, randMark, treeTime[reg], aMutList, aHap);
				}
			}
			return numMuts;
			
		}
		public double simGetPoissonRate(){
			coalesceRate = coalesce.coalesceGetRate(); 
			migrateRate = migFactory.migrateGetRate();
			recombRate = recomb.recombGetRate();
			geneConvRate = geneConversion.getRate();
			poissonRate = (double)(coalesceRate + migrateRate + recombRate + geneConvRate);
			return poissonRate;
			
		}
		public double simGetHistEvent(double gen){
			return histFactory.historicalGetNext(gen);
		}
		public double simGetPoisEvent(){
			return poissoner.poissonGetNext(this.simGetPoissonRate());
		}
		public boolean simDoPoisson(double gen){
			boolean didCoal = false;
			double randDouble = random.randomDouble();
			doublePointer dum =new doublePointer();
			doublePointer dum2 = new doublePointer();
			int popIndex;
			if(randDouble < recombRate /poissonRate){
				popIndex = recomb.recombPickPopIndex();
				recomb.recombExecute(gen, popIndex, dum);//another pointer...
			}
			else if(randDouble < (recombRate + migrateRate)/poissonRate){
				migFactory.migrateExecute(gen);
			}
			else if(randDouble < (recombRate + migrateRate + coalesceRate)/poissonRate){
				popIndex = coalesce.coalescePickPopIndex();
				dem.coalesceByIndex(popIndex, gen);
				didCoal = true;
			}
			else {
				popIndex = geneConversion.pickPopIndex();
				geneConversion.execute(gen, popIndex, dum, dum2);
			}
			return didCoal;
		}
		public double getCoalesceRate() {
			return coalesceRate;
		}


		public void setCoalesceRate(double coalesceRate) {
			this.coalesceRate = coalesceRate;
		}


		public double getMigrateRate() {
			return migrateRate;
		}


		public void setMigrateRate(double migrateRate) {
			this.migrateRate = migrateRate;
		}


		public double getGeneConvRate() {
			return geneConvRate;
		}


		public void setGeneConvRate(double geneConvRate) {
			this.geneConvRate = geneConvRate;
		}


		public double getRecombRate() {
			return recombRate;
		}


		public void setRecombRate(double recombRate) {
			this.recombRate = recombRate;
		}


		public double getPoissonRate() {
			return poissonRate;
		}


		public void setPoissonRate(double poissonRate) {
			this.poissonRate = poissonRate;
		}


		public double getTheta() {
			return theta;
		}


		public void setTheta(double theta) {
			this.theta = theta;
		}


		public int getNumMut() {
			return fixedNumMut;
		}


		public void setNumMut(int fixedNumMut) {
			this.fixedNumMut = fixedNumMut;
		}
		
}
