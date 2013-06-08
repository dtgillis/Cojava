package haplos;

import demography.demography;

public class Hap2 {
	
	private class popMap{
		int totPop;
		int[] ind2name;
		int[] ind2size;
		popMap(int atotPop){
			totPop = atotPop;
			ind2name = new int[totPop];
			ind2size = new int[totPop];
		}
		void setInd2Name(int index, int name){
			ind2name[index]=name;
		}
		void setInd2size(int index, int size){
			ind2size[index]=size;
		}
		int getPopName(int index){
			return ind2name[index];
		}
		int getPopSize(int index){
			return ind2size[index];
		}
	}
	demography dem;
	popMap aPopMap;
	public Hap2(demography adem){
		dem = adem;
		aPopMap = new popMap(dem.getNumPops());	
		setPopMap();
	}
	private void setPopMap(){
		for(int i=dem.getNumPops()-1;i>=0;i--){
			int index = dem.getNumPops()-1-i;
			aPopMap.setInd2Name(index,dem.getPopNameByIndex(i));
			aPopMap.setInd2size(index, dem.getNumNodesInPopByIndex(i));
			}
	}
	
	
}
