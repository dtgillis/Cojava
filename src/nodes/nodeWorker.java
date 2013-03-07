package nodes;

import segment.seg;
import segment.segWorker;

public class nodeWorker {
	int nodeIndex;
	segWorker segFactory = new segWorker();
	
	public nodeWorker(){
		nodeIndex = 0;
	}
	public node makeNewNode(int begin,int end,double gen,int pop){
		node aNode = new node(begin, end, gen, pop, nodeIndex);
		nodeIndex++;
		return aNode;
	}
	
	public node makeEmptyNode(double gen,int pop,node des1){
		node aNode = new node(0,0, gen, pop,nodeIndex);
		nodeIndex++;
		//curNode.segment = null;
		aNode.descendent[0] = null;
		aNode.descendent[1] = des1;
		return aNode;
	}
	public node makeEmptyNode2(double gen, int pop, node des1,node des2){
		node aNode = new node(0, 0, gen, pop, nodeIndex);
		nodeIndex++;
		aNode.descendent[0] = des1;
		aNode.descendent[1] = des2;
		//aNode.segment = null;
		return aNode;
	}
	public node nodeCoalesce(node aNode1,node aNode2,double gen){
		node newNode = makeEmptyNode2(gen,aNode1.pop,aNode1,aNode2);
		newNode.segment = segFactory.segUnion(aNode1.segment, aNode2.segment);
		return newNode;
	}
	//this should probably return the nodes instead? nope it is the 
	//1 or 2 that is written to the out file? so how to make these persist
	// for the next part?
	public int nodeRecombine(node aNode, node newNode1, node newNode2,
			double gen, double loc){
		seg aSeg,seg1,seg2;
		aSeg = new seg(0,loc);
		seg1 = segFactory.segIntersect(aNode.segment, aSeg);
		seg2 = segFactory.segIntersect(aNode.segment,segFactory.segInverse(aSeg));
		aSeg = null;
		if(seg1 == null || seg2 == null){
			if(seg1 != null )
				seg1 = null;
			if(seg2 != null)
				seg2 = null;
			return 1;
		}
		newNode1 = makeEmptyNode(gen,aNode.pop, aNode);
		newNode1.segment = seg1;
		newNode2 = makeEmptyNode(gen,aNode.pop,aNode);
		newNode2.segment = seg2;
		return 2;
	}
	// this should probably also return the nodes instead? //also basically same function as above.
	// gene conversion //
	public int nodeGC(node aNode, node newNode1, node newNode2, double gen,
			double loc, double locend){
		seg aSeg,seg1,seg2;
		aSeg = new seg(loc,locend);
		seg1 = segFactory.segIntersect(aNode.getSegment(), aSeg);
		seg2 = segFactory.segIntersect(aNode.getSegment(), segFactory.segInverse(aSeg));
		aSeg = null;
		if(seg1 ==null || seg2 == null){
			if(seg1!=null)
				seg1 =null;
			if(seg2!=null)
				seg2 = null;
			
			return 1;
		}
		newNode1 = makeEmptyNode(gen,aNode.getPop(),aNode);
		newNode1.setSegment(seg1);
		newNode2 = makeEmptyNode(gen,aNode.getPop(),aNode);
		return 2;
	}
	
	public int nodeBreakOffSeg(node aNode, node newNode1, node newNode2,
			double begin, double end){
		double gen = aNode.getGen();
		seg aSeg, seg1,seg2;
		aSeg = new seg(begin,end);
		seg1 = segFactory.segIntersect(aNode.getSegment(), aSeg);
		seg2 = segFactory.segIntersect(aNode.getSegment(), segFactory.segInverse(aSeg));
		aSeg = null;
		
		if(seg1 == null || seg2 == null){
			if(seg1 != null)
				seg1 = null;
			if(seg2 != null)
				seg2 = null;
			return 1;
		}
		
		newNode1 = makeEmptyNode(gen,aNode.getPop(),aNode);
		newNode1.setSegment(seg1);
		newNode2 = makeEmptyNode(gen,aNode.getPop(),aNode);
		newNode2.setSegment(seg2);
		return 2;
		
	}
	public void nodeDelete(node aNode){
		if(aNode != null){
			nodeDelete(aNode.descendent[0]);
			nodeDelete(aNode.descendent[1]);
			aNode = null;
		}
	}

}
