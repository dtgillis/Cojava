package coalesce;

import coalescent.CoalescentMain;

public class coalesce {
	
	double lastRate;
	public coalesce(){
		lastRate = 0;
	}
	public double coalesceGetRate(){
		int numPops = CoalescentMain.dem.getNumPops();
		double rate = 0;
		int numNodes,popSize;
		
		for(int i=0;i<numPops;i++){
			numNodes = CoalescentMain.dem.getNumNodesInPopByIndex(i);
			popSize = CoalescentMain.dem.getPopSizeByIndex(i);
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
		double randCounter = CoalescentMain.random.randomDouble();
		int i,popIndex,numPops,numNodes,popSize;
		popIndex = 0;
		numPops = CoalescentMain.dem.getNumPops();
		if(numPops>1){
			for( i=0;i<numPops&&randCounter > rate; i++){
				numNodes = CoalescentMain.dem.getNumNodesInPopByIndex(i);
				popSize = CoalescentMain.dem.getPopSizeByIndex(i);
				if(numNodes>1)
					rate += (double)(numNodes * (numNodes -1))/(4*popSize);
			}
			popIndex = i-1; 
		}
		return popIndex;
		
	}
	
}
