package pop;

import nodes.node;
import nodes.nodeList;

public class population {
	String name;
	int popSize;
	char[] label;
	nodeList members;
	public population(String aName,int aPopSize, char[] aLabel){
		name = aName;
		popSize = aPopSize;
		label = aLabel;
		members = new nodeList();
		
	}
	public void removeNode(node aNode){
		members.removeNode(aNode);
		
	}
	public void addNode(node aNode){
		members.addNode(aNode);
	}
	public void printNodes(){
		members.printNodeNames();
	}
	public void setPopSize(int aSize){
		popSize = aSize;
	}
	public int getPopSize(){
		return popSize;
	}
	public int getNumNodes(){
		return members.getNumMembers();
	}
	public node getNode(int index){
		return members.getNode(index);
	}
	public String getPopName(){
		return name;
	}
	public char[] getPopLabel(){
		return label;
	}
}
