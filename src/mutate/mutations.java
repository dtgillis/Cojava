package mutate;

import nodes.node;

public class mutations {
	double location;
	node ancNode1,ancNode2;
	mutNodeList mutNodes;
	
	public mutations(){
		
	}
	public void setLocation(double aLoc){
		location = aLoc;
		
	}
	public double getLocation(){
		return location;
	}
	public void setAncNode1(node aNode){
		ancNode1 = aNode;
	}
	public node getAncNode1(){
		return ancNode1;
	}
	public void setAncNode2(node aNode){
		ancNode2 = aNode;
	}
	public node getAncNode2(){
		return ancNode2;
	}
	
}
