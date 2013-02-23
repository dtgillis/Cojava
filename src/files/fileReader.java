package files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import coalescent.ArgHandler;

public class fileReader {
	ArgHandler fileSet;
	public fileReader(ArgHandler aFileSet ){
		fileSet = aFileSet;
		
	}
	public void paramFileProcess(){
		try {
			BufferedReader stream = new BufferedReader(
					new FileReader(fileSet.getParamFile()));
			String line;
			while((line = stream.readLine())!=null){
				char[] charLine = line.toCharArray();
					for(int i =0;i<charLine.length;i++){
						if(charLine[i]=='#' && i == 0){//comment
							i = charLine.length;
						}
						else if(charLine[i]=='#' && i!=0){//comment at a point in line kill comment 
							line = line.substring(0, i);
							processParamBuffer(line);
						}
						else
							processParamBuffer(line);
					}
			}
		stream.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error paramFileProcess" + e.getMessage() );
		}
	}
	private void processParamBuffer(String line) {
		//process the log file
		if(line.contains("length")){
			
		}
		else if (line.contains("recomb_file")){
			
		}
		else if (line.contains("mutation_rate")){
			
		}
		else if (line.contains("infinite_sites")){
			
		}
		else if (line.contains("gene_conversion_rate")){
			
		}
		else if (line.contains("number_mutation_sites")){
			
		}
		else if (line.contains("pop_label")){
			
		}
		else if (line.contains("pop_size")){
			
		}
		else if (line.contains("sample_size")){
			
		}
		else if (line.contains("pop_define")){
			
		}
		else if (line.contains("pop_event")){
			
		}
		else if (line.contains("random_seed")){
			
		}
		else {
			String err = line + "is not a valid parameter \n" +
					"this parameter will be skipped";
			System.out.println(err);
		}
	}
}
