package nodes;

public class nodeMaker {
	int nodeIndex;
	
	
	public nodeMaker(){
		nodeIndex = 0;
	}
	public node makeNewNode(int begin,int end,double gen,int pop){
		return new node(begin, end, gen, pop, nodeIndex);
	}
	
	public node makeEmptyNode(double gen,int pop,node des1){
		node curNode = new node(0,0, gen, pop,nodeIndex);
		//curNode.segment = null;
		curNode.descendent[0] = null;
		curNode.descendent[1] = des1;
		return curNode;
	}

}
