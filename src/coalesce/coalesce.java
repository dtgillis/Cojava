package coalesce;

import cosiRand.randomNum;
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
		int numpops = dem.getNumPops();
		int i;
		double rate = 0;
		int numnodes;
		int popsize;
		
		for (i = 0; i < numpops; i++) {
			numnodes = dem.getNumNodesInPopByIndex(i);
			popsize = dem.getPopSizeByIndex(i);
			if (numnodes > 1){
				long num = (numnodes * (numnodes -1 ));
				long den = 4 * popsize;
				rate += (double)num/(double)den;
			}
		}
		
		lastRate = rate;
		return rate;
	}
	public void setLastRate(double rate) {
		lastRate = rate;
		
	}
	public double getLastRate(){
		return lastRate;
	}
	public int coalescePickPopIndex(){

		double  rate = 0,
			randcounter = random.randomDouble() * coalesceGetRate();
		int     popindex = 0,
			numpops = dem.getNumPops(),
			numnodes,
			popsize,
			i;
		
		if (numpops > 1) {
			for (i = 0; i < numpops && randcounter > rate; i++) {
				numnodes = dem.getNumNodesInPopByIndex(i);
				popsize = dem.getPopSizeByIndex(i);
				if (numnodes > 1){
					long num = (numnodes*(numnodes-1));
					long den = 4*popsize;
					rate+= (double) num / (double) den;
					
				}
					//rate += (double) ((numnodes * (numnodes - 1))/(4 * popsize));
				
			}		
			popindex = i - 1;
		}
		return popindex;
		
	}
	
}
