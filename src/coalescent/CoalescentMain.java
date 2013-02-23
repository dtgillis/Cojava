package coalescent;

import files.fileReader;

public class CoalescentMain {
	static ArgHandler fileHolder;
	static fileReader inputHandler;
	public static void main(String[] args) {
		fileHolder = new ArgHandler(args);
		fileHolder.setArguments();
		inputHandler = new fileReader(fileHolder);
		inputHandler.paramFileProcess();
	}

}
