package recomb;

import java.util.ArrayList;

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
	int length,count;
	double recombRate,lastRate;
	recombStructure recombs;
	ArrayList<recombStructure> recList;
	
	public recListMaker(){
	    
	}
	public void setLength(int i){
		length = i;
	}
	public int getLength(){
		return length;
	}
	
	public void addRecombSiteLL(int aStart,double aRate){
		recombStructure  newRecomb = new recombStructure(aStart,aRate,0.0,null);
		if(recombs == null){
			recombs = newRecomb;
		}
		else if (recombs.startBase>aStart){
			newRecomb.next = recombs;
			recombs = newRecomb;
		}
		else{
		recombStructure tempRecomb = recombs; 
				//new recombStructure(recombs.startBase,recombs.rate,
		//recombs.cumulative,recombs.next);
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
	
}
