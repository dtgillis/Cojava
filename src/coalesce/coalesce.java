package coalesce;

import cosiRand.randomNum;
import coalescent.CoalescentMain;
import demography.demography;

public class coalesce {
	demography dem;
	double lastRate;
	randomNum random;
	public coalesce(demography adem,randomNum aRNG){
		random = aRNG;
		lastRate = 0;
		dem = adem;
	}
	public double coalesceGetRate(){
		int numPops = dem.getNumPops();
		double rate = 0;
		int numNodes,popSize;
		
		for(int i=0;i<numPops;i++){
			numNodes = dem.getNumNodesInPopByIndex(i);
			popSize = dem.getPopSizeByIndex(i);
			if(numNodes>1){
				rate+=(double)(numNodes*(numNodes-1))/(4* popSize);
			}
			
		}
		setLastRate(rate);
		return rate;
	}
	public void setLastRate(double rate) {
		lastRate = rate;
		
	}
	public double getLastRate(){
		return lastRate;
	}
	public int coalescePickPopIndex(){
		double rate = 0;
		double randCounter = random.randomDouble();
		int i,popIndex,numPops,numNodes,popSize;
		popIndex = 0;
		numPops = dem.getNumPops();
		if(numPops>1){
			for( i=0;i<numPops&&randCounter > rate; i++){
				numNodes = dem.getNumNodesInPopByIndex(i);
				popSize = dem.getPopSizeByIndex(i);
				if(numNodes>1)
					rate += (double)(numNodes * (numNodes -1))/(4*popSize);
			}
			popIndex = i-1; 
		}
		return popIndex;
		
	}
	
}
