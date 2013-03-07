package simulator;

import haplos.hap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import mutate.mutList;
import mutate.mutations;

import coalescent.CoalescentMain;

public class sim {
		double coalesceRate,migrateRate,geneConvRate,recombRate,poissonRate,theta;
		int fixedNumMut;
		final boolean DEBUG = true;
		
		public sim(){
			fixedNumMut = -1;
		}
		public void simSetLength(int l){
			CoalescentMain.recomb.setLength(l);
			CoalescentMain.geneConversion.setLength(l);
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
		public double simExecute(){
			double gen=0,historicalEventTime,poissonEventTime;
			boolean coal =false;
			boolean completeFlag = false;
			while(completeFlag==false){
				coal = false;
				historicalEventTime = (double) simGetHistEvent(gen);
				poissonEventTime = this.simGetPoisEvent();
				if(DEBUG){
					System.out.println(String.format("recomb: %f coalesce: %f migrate: %f geneconvert: %f \n",
							recombRate,coalesceRate,migrateRate,geneConvRate));
					System.out.println(String.format("poisson time: %f, hist time: %f\n",poissonEventTime,historicalEventTime));
				}
				if(historicalEventTime < 0 || poissonEventTime < historicalEventTime){
					gen += poissonEventTime;
					coal = simDoPoisson(gen);
					if(coal){completeFlag = CoalescentMain.dem.doneCoalescent();}
					
				}
				else{
					gen += historicalEventTime;
					gen = CoalescentMain.histFactory.historicalEventExecute(gen);
					completeFlag = CoalescentMain.dem.doneCoalescent();
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
			int i,summut = 0;
			double mutrate, probSum = 0;
			double loc;
			int numRegions,reg,numMuts = 0;
			double randMark,begin;
			double[] reglen,probRegion,treeTime;
			//double probRegion,treeTime;
			int[] nMutByRegion = null;
			FileWriter fileOut = new FileWriter(aFile.getName(),true);
			BufferedWriter out = new BufferedWriter(fileOut);
			numRegions = CoalescentMain.dem.getNumRegs();
			// print mutate headers....
			reglen = new double[numRegions];
			treeTime = new double[numRegions];
			for(reg= 0;reg<numRegions;reg++){
				treeTime[reg] = CoalescentMain.dem.totalTreeTime(reg);
				reglen[reg] = CoalescentMain.dem.getRegLength(reg);
				probSum += treeTime[reg] + reglen[reg];
			}
			if(fixedNumMut>0){
				probRegion = new double[numRegions];
				nMutByRegion = new int[numRegions];
				for(reg = 0;reg<numRegions;reg++){
					probRegion[reg] = treeTime[reg]*reglen[reg] /probSum;
				}
				cosiRand.multiNom.multinom(numRegions, fixedNumMut, probRegion, nMutByRegion);
			}
			for(reg=0;reg<numRegions;reg++){
				begin = CoalescentMain.dem.regBegin(reg);
				if(fixedNumMut==-1){
					mutrate = theta * treeTime[reg] * reglen[reg];
					numMuts = cosiRand.poisson.poission(mutrate);
					if(aFile.canWrite()){
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
				summut += numMuts;
				for(i=0;i<numMuts;i++){
					loc = begin + cosiRand.randomNum.randomDouble()*reglen[reg];
					randMark = cosiRand.randomNum.randomDouble();
					new mutations().mutateFindAndPrint(aFile, reg, loc, randMark, treeTime[reg], aMutList, aHap);
				}
			}
			return numMuts;
			
		}
		public double simGetPoissonRate(){
			coalesceRate = CoalescentMain.coalesce.coalesceGetRate(); 
			migrateRate = CoalescentMain.migFactory.migrateGetRate();
			recombRate = CoalescentMain.recomb.recombGetRate();
			geneConvRate = CoalescentMain.geneConversion.getRate();
			poissonRate = (double)(coalesceRate + migrateRate + recombRate + geneConvRate);
			return poissonRate;
			
		}
		public double simGetHistEvent(double gen){
			return CoalescentMain.histFactory.historicalGetNext(gen);
		}
		public double simGetPoisEvent(){
			return cosiRand.poisson.poissonGetNext(this.simGetPoissonRate());
		}
		public boolean simDoPoisson(double gen){
			boolean didCoal = false;
			double randDouble = cosiRand.randomNum.randomDouble();
			double dum = 0,dum2 = 0;
			int popIndex;
			if(randDouble < recombRate /poissonRate){
				popIndex = CoalescentMain.recomb.recombPickPopIndex();
				CoalescentMain.recomb.recombExecute(gen, popIndex, dum);//another pointer...
			}
			else if(randDouble < (recombRate + migrateRate)/poissonRate){
				CoalescentMain.migFactory.migrateExecute(gen);
			}
			else if(randDouble < (recombRate + migrateRate + coalesceRate)/poissonRate){
				popIndex = CoalescentMain.coalesce.coalescePickPopIndex();
				CoalescentMain.dem.coalesceByIndex(popIndex, gen);
				didCoal = true;
			}
			else {
				popIndex = CoalescentMain.geneConversion.pickPopIndex();
				CoalescentMain.geneConversion.execute(gen, popIndex, dum, dum2);
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
