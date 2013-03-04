package coalescent;

import demography.demography;
import files.fileReader;

public class CoalescentMain {
	static ArgHandler fileHolder;
	static fileReader inputHandler;
	public static demography dem;
	public static void main(String[] args) {
		fileHolder = new ArgHandler(args);
		fileHolder.setArguments();
		dem = new demography();
		dem.setLogFile(fileHolder.getLogFile());
		dem.initRecomb();
		
		inputHandler = new fileReader(fileHolder);
		inputHandler.paramFileProcess();
		
		
	}

}
