package segment;

public class segWorker {
		
	public segWorker(){
		
	}
	public seg segInverse(seg aSegment){
		seg newSeg , newSeg2;
		seg oldSeg, headSeg;
		boolean setend = true;
		if(aSegment == null){
			return new seg(0,1);
		}
		
		headSeg = new seg(0,0);
		newSeg = headSeg;
		
		oldSeg = aSegment;
		
		if(oldSeg.begin > 0)
			newSeg.begin = 0;
		else{
			newSeg.end = oldSeg.begin;
			oldSeg = oldSeg.next;
		}
		while (oldSeg != null){
			newSeg.end = oldSeg.begin;
			if(oldSeg.end != 1){
				newSeg2 = new seg(0,0);
				newSeg.next = newSeg2;
				newSeg = newSeg2;
				newSeg.begin = oldSeg.end;
				oldSeg = oldSeg.next;
			}
			else{
				setend = false;
				oldSeg = oldSeg.next;
			}
		}
			if(setend)
				newSeg.end=1;
			
			newSeg.next = null;
			return headSeg;
		}
	public seg segUnion(seg seg1, seg seg2){
		double begin,end;
		seg activeSeg,nextSeg,tempSeg,newSeg;
		
		if (seg1 != null && seg2 != null){
			newSeg = null;
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
			//System.exit(0);
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
		//now either activeSeg.next is null or nextSeg is null
		if(nextSeg != null){
			activeSeg = nextSeg;
		}
		else activeSeg = activeSeg.next;
		
		while(activeSeg!= null){
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
}

