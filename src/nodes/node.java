package nodes;

import segment.seg;


public class node {
	
		int name,pop;
		double gen;
		node[] descendent;
		seg segment;
		
		public node(double begin, double end, double aGen, int aPop,
			int aName){
		descendent = new node[2];
		descendent[0] = null;
		descendent[1] = null;
		segment = new seg(begin, end);
		gen = aGen;
		pop = aPop;
		name = aName;
		
		}
		public seg getSegment(){
			return segment;
		}
		public void setSegment(){
			
		}
		public node[] getDescendents(){
			return descendent;
		}
		public double getGen(){
			return gen;
		}
		public int getName(){
			return name;
		}
		public int getPop(){
			return pop;
		}
		public void printNode(node aNode){
			System.out.println("node: "+ aNode.name + " pop:  " + aNode.pop + " gen: " + aNode.gen + " ");
			//segs out as well
			System.out.println("desc: ");
			for(int i=0;i<2;i++){
				System.out.println(aNode.descendent[i].name);
			}
		}
	}

