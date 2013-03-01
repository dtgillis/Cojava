package nodes;


public class node {
	
		int name,pop;
		double gen;
		node[] descendent;
		
		
		public node(double begin, double end, double aGen, int aPop,
			int aName){
		descendent = new node[2];
		descendent[0] = null;
		descendent[1] = null;
		gen = aGen;
		pop = aPop;
		name = aName;
		
		}
		
	}

