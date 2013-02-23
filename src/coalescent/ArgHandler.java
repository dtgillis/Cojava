package coalescent;

import java.io.File;

public class ArgHandler {
		boolean logFileSet,segFileSet,outFileSet,paramFileSet;
		File logFile,segFile,outFile,paramFile;
		String[] arguments;
		public ArgHandler(String[] args){
			arguments = args;
		}
		public void setArguments(){
			int i = 0;
			while(i<arguments.length){
				if(arguments[i].equalsIgnoreCase("-l")){
					setLogFile(arguments[++i]);
					
				}
				else if(arguments[i].equalsIgnoreCase("-p")){
					setParamFile(arguments[++i]);
				
				}
				else if(arguments[i].equalsIgnoreCase("-s")){
					setSegFile(arguments[++i]);
					
				}
				else if(arguments[i].equalsIgnoreCase("-o")){
					setOutFile(arguments[++i]);
										
				}
				else{
					System.out.println("Warning: illegal argument passed");
					break;
				}
				i++;
			}
			parmFileCheck();
		}
		private void parmFileCheck() {
			if(!paramFileSet){
				System.out.println("A param File must be used");
				//add usage out text first 
				System.exit(0);
			}
			
		}
		private void setOutFile(String aFile) {//needs to be writable
			outFile = new File(aFile);
			outFileSet = true;
		}
		private void setSegFile(String aFile) {
			segFile = new File(aFile);
			segFileSet = true;
		}
		private void setParamFile(String aFile) {
			paramFile = new File(aFile);
			paramFileSet = true;
		}
		private void setLogFile(String aFile) {//needs to be writable
			logFile = new File(aFile);
			logFileSet = true;
		}
}
