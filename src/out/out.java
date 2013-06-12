package out;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import coalescent.CoalescentMain;

import mutate.mutList;
import haplos.Hap2;
import haplos.hap;

public class out {

	boolean infSites = false;
	double[] sortVals;
	public out(){
		
	}
	
	public void setInfiniteSites(boolean aBol){
		infSites = aBol;
	}
	public void printHaps(String fileBase,int length, Hap2 haplos) throws IOException{
		//int[] badSnp = new int[(int)haplos.getSnpPos().length]; //no more than Half?
		//probably a lot less
		
		boolean[] usemut = new boolean[haplos.getSnpPos().length];
		for(int k=0;k<usemut.length;k++){
			usemut[k]=true;
		}
		if(!infSites){
			//int snpCount = 0;
			int j=0;
			for(int i=0;i<haplos.getSnpPos().length-1;i++){//so last j<length...
				j = i+1;
			
				if((int)(haplos.getSnpPos()[i]* length)==(int)(haplos.getSnpPos()[j]* length)){
					usemut[j]=false;
				}
			}
			
		}
		//split up the io into the different populations and write them at the same time!!!!
		int[] popBounds = new int[haplos.getTotPop()+1];
		popBounds[0]=0;
		for(int i=0;i<haplos.getTotPop();i++){
			popBounds[i+1]= popBounds[i]+haplos.getSampleSize4Pop(i);
		}
		CoalescentMain.pool.invoke(new parallelWriter(0,haplos.getTotPop(),haplos.getHapData(),haplos.getSnpPos(),
				haplos.getPopNameArray(),popBounds,usemut,length));
			
	}
		
		
		
		
		
		
		
		
		
}
	
