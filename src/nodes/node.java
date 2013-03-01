package nodes;


public class node {
	
		int name,pop;
		double gen;
		node[] descendent;
		// needs seg object 
		
		public node(double begin, double end, double aGen, int aPop,
			int aName){
		descendent = new node[2];
		descendent[0] = null;
		descendent[1] = null;
		gen = aGen;
		pop = aPop;
		name = aName;
		
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

