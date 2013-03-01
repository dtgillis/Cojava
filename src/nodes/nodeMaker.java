package nodes;

public class nodeMaker {
	int nodeIndex;
	node curNode;
	
	public nodeMaker(){
		nodeIndex = 0;
		curNode = null;
	}
	public void makeNewNode(int begin,int end,double gen,int pop){
		curNode = new node(begin, end, gen, pop, nodeIndex);
	}
	public node getCurNode(){//gets the last made node...
		return curNode;
	}
	public void makeEmptyNode(double gen,int pop,node des1){
		curNode = new node(0,0, gen, pop,nodeIndex);
		curNode.descendent[0] = null;
		curNode.descendent[1] = des1;
		
		
	}

}
