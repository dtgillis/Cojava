package recomb;


import nodes.node;
import pointers.doublePointer;
import cosiRand.randomNum;
import demography.demography;

public class recListMaker {
	
	private class recombStructure{
		int startBase;
		double rate,cumulative;
		recombStructure next;
		public recombStructure(int aStartBase,double aRate,
				double aCumulative,recombStructure aNext){
			startBase = aStartBase;
			rate = aRate;
			cumulative = aCumulative;
			next = aNext;
		}
	}
	demography dem;
	int length,count;
	double recombRate,lastRate;
	recombStructure recombs;
	randomNum random;
	//ArrayList<recombStructure> recList;
	
	public recListMaker(demography adem, randomNum aRNG){
		random = aRNG;
	    dem = adem;
	}
	public void setLength(int i){
		length = i;
	}
	public int getLength(){
		return length;
	}
	
	public void addRecombSiteLL(int aStart,double aRate){
		recombStructure  newRecomb = new recombStructure(aStart,aRate,0.0,null);
		recombStructure tempRecomb = recombs;
		if(recombs == null){
			recombs = newRecomb;
			return;
		}
		else if (recombs.startBase>aStart){
			newRecomb.next = recombs;
			recombs = newRecomb;
			return;			
		}
		else{
		while (tempRecomb.next !=null){
			if(tempRecomb.next.startBase > aStart){
				newRecomb.next = tempRecomb.next;
				tempRecomb.next = newRecomb; 
				return;
			}
			tempRecomb = tempRecomb.next;
		}
		newRecomb.next = null;
		tempRecomb.next = newRecomb;
		}
		
	}
	
	public recombStructure getRecomb(){
		return recombs;
	}
	
	public void recomb_calc_r(){
		double rr = 0;
		recombStructure tempRecomb = recombs;
		while(tempRecomb!=null && tempRecomb.startBase < length){
			if(tempRecomb.next == null || tempRecomb.next.startBase > length)
				rr += (length - tempRecomb.startBase)*tempRecomb.rate;
			else
				rr += (tempRecomb.next.startBase - tempRecomb.startBase) * tempRecomb.rate;
			
			tempRecomb.cumulative = rr;
			tempRecomb = tempRecomb.next;
		}
		recombRate = rr;
	}
	public double recombGetR(){
		return recombRate;
	}
	public node[] recombExecute(double gen,int popIndex, doublePointer location){
		double loc;
		double temp,temp1;
		recombStructure tempRecomb = recombs;
		double rr = 0 ;
		double end;
		
		temp1 = random.randomDouble();
		temp = (double) (temp1 * (recombRate));
		
		while (tempRecomb != null && tempRecomb.startBase < length && rr < temp){
			if(tempRecomb.next==null|| tempRecomb.next.startBase > length){
				rr += (length - tempRecomb.startBase)* tempRecomb.rate;
			}
			else{
				rr += (tempRecomb.next.startBase - tempRecomb.startBase) * tempRecomb.rate;
			}
			if(rr < temp){
				tempRecomb = tempRecomb.next;
			}
		}
		
		if(tempRecomb.next == null || tempRecomb.next.startBase > length)
			end = length;
		else end = tempRecomb.next.startBase;
		
		loc = (double) ((int) (end - (rr - temp)/ tempRecomb.rate)) / length;
		location.setDouble(loc); // pointer magic ?
		return dem.recombineByIndex(popIndex, gen, loc);
	}
	public double recombGetRate(){
		int numPops = dem.getNumPops();
		int i;
		double rate = 0;
		int numNodes;
		if(recombRate == 0) return 0;
		for(i=0;i<numPops;i++){
			numNodes = dem.getNumNodesInPopByIndex(i);
			rate += (numNodes* recombRate);
		}
		lastRate = rate;
		return rate;
	}
	public int recombPickPopIndex(){
		/* figure out which pop to recombine */
		  /* Note: the use of lastrate looks fragile to me, with little time savings.
		     It assumes that get_rate has been called immediately before this 
		     function, with no change in the number of chromosomes since. sfs */
		int popIndex =0,i,numNodes;
		double randCounter = random.randomDouble() * lastRate;
		double rate = 0;
		int numPops = dem.getNumPops();
		
		//weigh pops by numNodes
		if(numPops>1){
			for(i=0;i<numPops && rate<randCounter; i++){
				numNodes = dem.getNumNodesInPopByIndex(i);
				rate+=(numNodes*recombRate);
			}
			popIndex = i -1;
		}
		return popIndex;
	}
}
