package segment;

public class segWorker {
		
	public segWorker(){
		
	}
	public seg segInverse (seg segptr) 
	{
	  seg newseg, newseg2;
	  seg oldseg, headseg;
	  boolean setend = true;

	  if (segptr == null) 
	    return new seg(0,1);

	  headseg = new seg(0,0);
	  
	  newseg = headseg;

	  oldseg = segptr;

	  if (oldseg.begin > 0)
	    newseg.begin = 0;
	  else {
	    newseg.begin = oldseg.end;
	    oldseg = oldseg.next;
	  }

	  while (oldseg != null) {
	    newseg.end = oldseg.begin;
	    if (oldseg.end != 1) {
				
	      newseg2 = new seg(0,0);
	      newseg.next = newseg2;
	      newseg = newseg2;
	      newseg.begin = oldseg.end;
	      oldseg = oldseg.next;
	    }
	    else {
	      setend = false;
	      oldseg = oldseg.next;
	    }
	  }
	       
	  if(setend)
	    newseg.end = 1;


	  newseg.next = null;
	  return headseg;
	}
	public seg segUnion(seg seg1, seg seg2){
		double begin,end;
		seg activeSeg,nextSeg,tempSeg,newSeg;
		newSeg = null;
		if (seg1 != null && seg2 != null){
			
			if(seg1.begin<seg2.begin){
				begin = seg1.begin;
				end = seg1.end;
				nextSeg = seg1.next;
				activeSeg = seg2;
			}
			else{
				begin = seg2.begin;
				end = seg2.end;
				nextSeg = seg2.next;
				activeSeg = seg1;
			}
			if(nextSeg !=null && (activeSeg.begin > nextSeg.begin)){
				tempSeg = nextSeg;
				nextSeg = activeSeg;
				activeSeg = tempSeg;
			}
		
		}
		else{
			System.out.println("segUnion: you did something wrong - null segment");
			System.exit(0);
			return null;
		}
		// updating begin/end using activeSeg
		if(activeSeg.begin > end){
			newSeg = segAdd(newSeg,begin,end);
			begin = activeSeg.begin;
			end = activeSeg.end;
		}
		else if (activeSeg.end > end){
			end = activeSeg.end;
		}
		while (activeSeg.getNext()!=null && nextSeg!=null){
			if(activeSeg.getNext().getBegin() > nextSeg.getBegin()){
				tempSeg = nextSeg;
				nextSeg = activeSeg.getNext();
				activeSeg = tempSeg;
			}
			else{
				activeSeg = activeSeg.getNext();
			}
			//iff possible segment is disjoint add this segment and startover
			if(activeSeg.getBegin()>end){
				newSeg = this.segAdd(newSeg, begin, end);
				begin = activeSeg.getBegin();
				end = activeSeg.getEnd();
			}
			else if(activeSeg.getEnd()>end){
				end = activeSeg.getEnd();
			}
			
		}
		//now either activeSeg.next is null or nextSeg is null
		if(nextSeg != null){
			activeSeg = nextSeg;
		}
		else activeSeg = activeSeg.next;
		
		while(activeSeg!= null ){
			//first check if activeSeg.next overlaps the current begin/end
			//then copy all of activeSeg to newSeg
			if(activeSeg.begin > end){
				newSeg = segAdd(newSeg,begin,end);
				begin = activeSeg.begin;
				end = activeSeg.end;
			}
			else if (activeSeg.end > end){
				end = activeSeg.end;
			}
			activeSeg = activeSeg.next;
		}
		newSeg = segAdd(newSeg,begin,end);
		
		return newSeg;
	}
	public seg segIntersect(seg seg1,seg seg2){
		seg newSeg = null;
		double begin,end;
		seg firstSeg,secondSeg,tempSeg;
		if(seg1 ==null || seg2 == null){
			System.out.println("Stop! something is wrong in segIntersect");
		}
		if(seg1.begin < seg2.begin){
			firstSeg = seg1;
			secondSeg = seg2;
		}
		else{
			firstSeg = seg2;
			secondSeg = seg1;
		}
		
		while(firstSeg !=null && secondSeg !=null){
			//which segment is first?
			if(firstSeg.begin > secondSeg.begin){
				tempSeg = firstSeg;
				firstSeg = secondSeg;
				secondSeg = tempSeg;
			}
			//if they overlap
			
			if(firstSeg.end>secondSeg.begin){
				begin = secondSeg.begin;
				
				//if second is contained completely in the first
				if(firstSeg.end > secondSeg.end){
					end = secondSeg.end;
					secondSeg = secondSeg.next;
				}
				else{
					end = firstSeg.end;
					firstSeg = firstSeg.next;
				}
				newSeg = segAdd(newSeg,begin,end);
			}
			else firstSeg = firstSeg.next;
		}
		return newSeg;
	}
	public seg segAdd(seg curSeg, double begin, double end) {
		seg newSeg = new seg(0,0);
		if(curSeg == null){
			newSeg.begin = begin;
			newSeg.end = end;
			newSeg.next = curSeg;
			return newSeg;
		}
		else if(curSeg.begin > begin){
			newSeg.begin = begin;
			newSeg.end = end;
			newSeg.next = curSeg;
			return newSeg;
		}
		else{
			curSeg.next = segAdd(curSeg.next,begin,end);
			return curSeg;
		}
		
	}
	public boolean segContains(seg aSeg,double loc){
		while (aSeg != null){
			if(loc>= aSeg.begin){
				if(loc<=aSeg.end) return true;
				
				aSeg = aSeg.next;
			}
			else return false;
		}
		return false;
	}
	public void printSeg(seg aSeg){
		if(aSeg != null){
			System.out.println(aSeg.begin + "  " + aSeg.end);
			printSeg(aSeg.next);
			
		}
	}
}

