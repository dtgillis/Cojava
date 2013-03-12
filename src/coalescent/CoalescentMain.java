package coalescent;

import java.io.IOException;

import bottleNeck.bottleNeck;

import migration.migrationWorker;
import mutate.mutList;
import mutate.mutations;
import nodes.nodeWorker;
import recomb.recListMaker;
import segment.segWorker;
import simulator.sim;
import sweep.sweep;
import coalesce.coalesce;
import cosiRand.poisson;
import cosiRand.randomNum;
import demography.demography;
import files.fileReader;
import geneConversion.gc;
import haplos.hap;
import haplos.hapWorker;
import historical.histWorker;

public class CoalescentMain {
	static ArgHandler fileHolder;
	static fileReader inputHandler;
	static demography dem;
	static mutList aMutList;
	static gc geneConversion;
	//public static mutWorker muts;
	static recListMaker recomb;
	static sweep sweeper;
	static sim simulator;
	static hap haps;
	static hapWorker hapFactory;
	static histWorker histFactory;
	static coalesce coalesce;
	static migrationWorker migFactory;
	static nodeWorker nodeFactory;
	static mutations mutate;
	static randomNum random;
	static segWorker segFactory;
	static bottleNeck bottleneck;
	static poisson poissoner;
	public static void main(String[] args) {
		// get and process arguments
		fileHolder = new ArgHandler(args);
		fileHolder.setArguments();
		
		// instantiate all the objects that build on each other
		segFactory = new segWorker();
		random = new randomNum();
		haps = new hap();
		poissoner = new poisson(random);
		nodeFactory = new nodeWorker(segFactory);
		dem = new demography(nodeFactory,segFactory,random);
		dem.setLogFile(fileHolder.getLogFile());
		recomb = new recListMaker(dem, random);
		geneConversion = new gc(dem, random);
		migFactory = new migrationWorker(dem);
		bottleneck = new bottleNeck(dem,poissoner);
		histFactory = new histWorker(dem, migFactory, sweeper, null);
		coalesce = new coalesce(dem, random);
		simulator = new sim(dem, poissoner, geneConversion, recomb, random, histFactory, coalesce, migFactory, mutate);
		
		//now assign all the  info in our paramater file....
		inputHandler = new fileReader(fileHolder, dem, recomb, simulator, geneConversion, histFactory, random);
		inputHandler.paramFileProcess();
		sweeper = new sweep(dem, recomb, geneConversion, random, poissoner, nodeFactory);
		aMutList = new mutList();//mutlist init
		hapFactory = new hapWorker(haps, dem);
		hapFactory.hapAssignChroms();
		dem.initRecomb();
		sweeper.sweepInitMut(aMutList, haps);
		simulator.simExecute();
		try {
			simulator.simMutate(fileHolder.getOutFile(), aMutList, haps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
