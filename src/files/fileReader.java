package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import recomb.recListMaker;

import coalescent.ArgHandler;
public class fileReader {
	ArgHandler fileSet;
	recListMaker recomb;

	private boolean debug = true;
	public fileReader(ArgHandler aFileSet ){
		fileSet = aFileSet;
		
	}
	public void paramFileProcess(){
		try {
			BufferedReader stream = getFileToRead(fileSet.getParamFile().toString());
			String line;
			while((line = stream.readLine())!=null){//reading everything line by line
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
	private void processParamBuffer(String line) {//gonna have to make some objects
		//process the param file
		if(line.contains("length")){
			System.out.println(line);
		}
		else if (line.contains("recomb_file")){
			System.out.println(line);
			processRecombFile(line);
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
	private void processRecombFile(String line) {
		try{
		BufferedReader aStream;
		String fileType = "recomb_file";
		//line.replace(" ", "");//maybe bad for windows machines with spaces in filename
		line = line.trim();
		line = line.substring(fileType.length());
		aStream = getFileToRead(line.trim());
		recomb = new recListMaker();
			while((line = aStream.readLine() )!=null){
				String[] result = line.split("\\s+");
				int start = Integer.parseInt(result[0]);
				double rate = Double.parseDouble(result[1]);
				recomb.addRecombSiteLL(start, rate);
			}
		aStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private BufferedReader getFileToRead(String aPath){
	 BufferedReader bf;
	try {
		bf = new BufferedReader(new FileReader(aPath));
		return bf;
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	 
	}
}
