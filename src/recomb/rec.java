package recomb;

public class rec {
	
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
	int length;
	double recombRate,lastRate;
	recombStructure recombs;
	public rec(){
	    recombs = null;
		recombRate = 0;
		lastRate = 0;
		
	}
	public void setLength(int i){
		length = i;
	}
	public int getLength(){
		return length;
	}
	public void addRecombSite(int aStart,double aRate){
		recombStructure tempRec = recombs;
		recombStructure newRecomb = new recombStructure(aStart,aRate,0.0,null);
		if(recombs == null){//first recomb site
			recombs = newRecomb;
			newRecomb.next = null;
			return;
			}
		else if (aStart<recombs.startBase){//put it on the top of current recombs queue..
			newRecomb.next = recombs;
			recombs = newRecomb;
			return;
		}
		else{// we have to move some stuff around....
			//while(tempRec) this part is weird why does it not reassign to recomb
			//after moving stuff around?
		}
		
	}
	
	
	
}
