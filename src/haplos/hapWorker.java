package haplos;

import java.util.ArrayList;

import coalescent.CoalescentMain;
import pop.population;

public class hapWorker {
	hap haplos;
	final int INIT_SIZE = 100;
	public hapWorker(hap aHap){	
		haplos = aHap;
	}
	public void hapAssignChroms(){
		population aPop;
		int npop,nchr,chromIndex,popName;
		int running = 0;
		npop = CoalescentMain.dem.getNumPops();
		haplos.setnPop(npop);
		haplos.setPopId(new int[npop]);
		assert(haplos.getPopId()!=null);
		
		for(int ipop = 0 ;ipop<npop;ipop++){
			running += CoalescentMain.dem.getNumNodesInPopByIndex(ipop);
			haplos.setMutArraySize(ipop,CoalescentMain.dem.getPopNameByIndex(ipop));
			
		}
		
		haplos.setnChrom(running);
		haplos.setWhichPopInd(new int[running]);
		haplos.setMutArraySize(new int[running]);
		haplos.setnMut(new int[running]);
		haplos.setMutIndex(new ArrayList<int[]>(running));
		assert(haplos.getWhichPopInd()!=null);
		assert(haplos.getMutIndex()!=null);
		assert(haplos.getMutArraySize()!=null);
		assert(haplos.getnMut()!=null);
		
		running = 0;
		for(int ipop = 0;ipop<npop; ipop++){
			aPop = CoalescentMain.dem.getPopByIndex(ipop);
			nchr = aPop.getNumNodes();
			popName = aPop.getPopName();//not sure this is needed?
			for(int i = 0;i<nchr;i++){
				chromIndex = aPop.getNode(i).getName();
				haplos.setMutArraySize(chromIndex, INIT_SIZE);
				haplos.setMutIndex(chromIndex, new int[INIT_SIZE]);
				haplos.setWhichPopInd(chromIndex,ipop);
				}
			running += nchr;
		}
		
	}
}
