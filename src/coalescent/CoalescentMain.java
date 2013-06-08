package coalescent;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import migration.migrationWorker;
import mutate.mutList;
import mutate.mutations;
import nodes.nodeWorker;
import out.out;
import recomb.recListMaker;
import segment.segWorker;
import simulator.sim;
import sweep.sweep;
import bottleNeck.bottleNeck;
import coalesce.coalesce;
import cosiRand.poisson;
import cosiRand.randomNum;
import demography.demography;
import files.fileReader;
import geneConversion.gc;
import haplos.Hap2;
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
	static Hap2 hapalt;
	public static ForkJoinPool pool;
	public static void main(String[] args) throws InterruptedException {
		// get and process arguments
		fileHolder = new ArgHandler(args);
		fileHolder.setArguments();
		// fork join pool for parallel stuff
		pool = new ForkJoinPool();
		// instantiate all the objects that build on each other
		segFactory = new segWorker();
		random = new randomNum();
		haps = new hap();
		poissoner = new poisson(random);
		nodeFactory = new nodeWorker(segFactory);
		dem = new demography(nodeFactory,segFactory,random);
		if(fileHolder.logFileSet)dem.setLogFile(fileHolder.getLogFile());
		
		mutate = new mutations(dem,segFactory);
		recomb = new recListMaker(dem, random);
		geneConversion = new gc(dem, random);
		migFactory = new migrationWorker(dem,random);
		bottleneck = new bottleNeck(dem,poissoner);
		sweeper = new sweep(dem, recomb, geneConversion, random, poissoner, nodeFactory, segFactory);

		histFactory = new histWorker(dem, migFactory, sweeper, bottleneck);
		coalesce = new coalesce(dem, random);
		simulator = new sim(dem, poissoner, geneConversion, recomb, random, histFactory, coalesce, migFactory, mutate);
		
		//now assign all the  info in our paramater file....
		inputHandler = new fileReader(fileHolder, dem, recomb, simulator, geneConversion, histFactory, random);
		inputHandler.paramFileProcess();
		hapalt = new Hap2(dem);
		//sweeper = new sweep(dem, recomb, geneConversion, random, poissoner, nodeFactory, segFactory);
		aMutList = new mutList();//mutlist init
		hapFactory = new hapWorker(haps, dem);
		hapFactory.hapAssignChroms();
		dem.initRecomb();
		sweeper.sweepInitMut(aMutList, haps);
		simulator.simExecute();
		try {
			simulator.simMutate(fileHolder.getSegFile(), aMutList, haps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out outwriter = new out();
		if(fileHolder.outFileSet){
			try {
				outwriter.printHaps("out", recomb.getLength(), aMutList, haps);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}

}
