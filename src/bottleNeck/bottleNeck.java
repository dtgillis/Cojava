package bottleNeck;

import cosiRand.poisson;
import demography.demography;

public class bottleNeck {
	poisson poissoner;
	demography dem;
	public bottleNeck(demography aDem,poisson aPois){
		poissoner = aPois;
		dem = aDem;
	}
	public void bottleNeckExecute(int popName,double coeff,int gen){
		int numNodes = dem.getNumNodesInPopByName(popName);
		double t = 0,effectiveN,rate,temp;
		if(numNodes<2)return;
		effectiveN = -1.0 / (2.0 * Math.log(1.0 - coeff));
		rate = (double) (4* effectiveN)/ (numNodes * (numNodes-1));
		temp = poissoner.poissonGetNext(1/rate);
		t+=temp;
		while(t<=1.0){
			dem.coalesceByName(popName, gen + t);
			numNodes --;
			if(numNodes>1){
				rate = (double)(4*effectiveN)/(numNodes * (numNodes -1));
				t+=poissoner.poissonGetNext(1/rate);
			}
			else t++;
		}
	}
}
