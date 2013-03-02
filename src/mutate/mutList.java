package mutate;

public class mutList {
	final int INITSIZE = 500;
	int nMut;
	double pos;
	int arraySize;

	public mutList(){
		nMut = 0;
		arraySize = INITSIZE;
		
	}
	public void setNumMut(int numMut){
		nMut = numMut;
	}
	public int getNumMut(){
		return nMut;
	}
	public void setPos(double aPos){
		pos = aPos;
	}
	public void setArraySize(int anArraySize){
		arraySize = anArraySize;
	}
	
}