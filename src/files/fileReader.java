package files;

import java.io.BufferedReader;
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
							line = "";
							//break;
						}
						else if(charLine[i]=='#' && i!=0){//comment at a point in line kill comment 
							line = line.substring(0, i);
							//processParamBuffer(line);
							i = charLine.length;
							
						}
					
					}
				if(!(line.length()==0)){
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
			System.out.println(line);
		}
		else if (line.contains("recomb_file")){
			System.out.println(line);
		}
		else if (line.contains("mutation_rate")){
			System.out.println(line);
		}
		else if (line.contains("infinite_sites")){
			System.out.println(line);
		}
		else if (line.contains("gene_conversion_rate")){
			System.out.println(line);
		}
		else if (line.contains("number_mutation_sites")){
			System.out.println(line);
		}
		else if (line.contains("pop_label")){
			System.out.println(line);
		}
		else if (line.contains("pop_size")){
			System.out.println(line);
		}
		else if (line.contains("sample_size")){
			System.out.println(line);
		}
		else if (line.contains("pop_define")){
			System.out.println(line);
		}
		else if (line.contains("pop_event")){
			System.out.println(line);
		}
		else if (line.contains("random_seed")){
			System.out.println(line);
		}
		else {
			String err = line + "is not a valid parameter \n" +
					"this parameter will be skipped";
			System.out.println(err);
		}
	}
}
