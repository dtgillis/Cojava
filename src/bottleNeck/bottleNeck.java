package bottleNeck;

import cosiRand.poisson;
import coalescent.CoalescentMain;

public class bottleNeck {
	public static void bottleNeckExecute(int popName,double coeff,int gen){
		int numNodes = CoalescentMain.dem.getNumNodesInPopByName(popName);
		double t = 0,effectiveN,rate,temp;
		if(numNodes<2)return;
		effectiveN = -1.0 / (2.0 * Math.log(1.0 - coeff));
		rate = (double) (4* effectiveN)/ (numNodes * (numNodes-1));
		temp = poisson.poissonGetNext(1/rate);
		t+=temp;
		while(t<=1.0){
			CoalescentMain.dem.coalesceByName(popName, gen + t);
			numNodes --;
			if(numNodes>1){
				rate = (double)(4*effectiveN)/(numNodes * (numNodes -1));
				t+=poisson.poissonGetNext(1/rate);
			}
			else t++;
		}
	}
}
