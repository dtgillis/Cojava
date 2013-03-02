package nodes;

import java.util.ArrayList;

public class nodeList {

	
	private final int NODELIST_INITSIZE = 200;
	int numMembers,sizeNow;
	ArrayList<node> nodes;
	
	public nodeList(){
		numMembers = 0 ;
		sizeNow = NODELIST_INITSIZE;
		
	}
	public void addNode(node aNode){
		if(numMembers >= sizeNow -1){
			sizeNow = 2;
			//array list handles size dynamically 
		}
		nodes.add(numMembers, aNode);
		numMembers++;
	}
	public void removeNode(node aNode){
		
		for(int i=0;i<nodes.size();i++){
			if(nodes.get(i).name == aNode.name){
				nodes.remove(i);
				return;
			}
		}
	}
	public void printNodeNames(){
		for(int i=0;i<nodes.size();i++){
			System.out.println(nodes.get(i).name);
		}
	}
	public int getNumMembers(){
		return numMembers;
	}
	public int getSizeNow(){
		return sizeNow;
	}
	public node getNode(int index){
		return nodes.get(index);
	}
}
