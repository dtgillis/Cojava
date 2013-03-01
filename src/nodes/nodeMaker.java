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
	public node getCurNode(){
		return curNode;
	}
	
}
