package edgar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {
	// Instance Variables
	File file;
	FileWriter fw;
	BufferedWriter bw;
	
	public CSVWriter() throws IOException {
		file = new File("database.csv");
		
		// Create the file if it doesn't exist
		if(!file.exists()) {
			file.createNewFile();
		}
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		bw.write("ID,Company Name,Industry");
		bw.newLine();
	}
	
	public void writeToCSV() throws IOException {
		// Creating an Array of File Objects
		List<String> filesInDirectory = new ArrayList<String>();
		File[] fileArray = new File("download/").listFiles();
		
		// Add the file names to a List
		for(File file : fileArray) {
			if(file.isFile()) {
				filesInDirectory.add(file.getName());
			}
		}
		
		// Loop through the files in the List<String>
		for(int i=0; i<filesInDirectory.size();i++) {
			// Creating a new File Object
			String fileName = filesInDirectory.get(i);
			File tempFile = new File("download/" + fileName);
			// BufferedReader for reading the file
			BufferedReader reader = new BufferedReader(new FileReader(tempFile));
			// Initializing the line variable that will hold the current line in file
			String line = null;
			// An array to hold the temporary values
			String[] outputArray = new String[3];
			// Assigning an ID for file
			outputArray[0] = new Integer(i).toString();
			boolean companyFound = false;
			boolean industryFound = false;
			
			// Looping through the lines in the file
			while((line = reader.readLine()) != null) {
				if((line.indexOf("COMPANY CONFORMED NAME:")> 0) && (!companyFound)) {
					companyFound = true;
					String company = line.substring(25);
					company = company.trim();
					company = company.replace(",", " ");
					outputArray[1] = company;
				} else if((line.indexOf("STANDARD INDUSTRIAL CLASSIFICATION:")  > 0) && (!industryFound)) {
					industryFound = true;
					String industry = line.substring(37);
					industry = industry.trim();
					industry = industry.replace(",", " ");
					outputArray[2] = industry;
				}
			}
			
			if (outputArray[2] == null) {
				outputArray[2] = "N/A";
			}
			
			String tempOutputString=outputArray[0] + "," + outputArray[1] + "," + outputArray[2];
			bw.write(tempOutputString);
			bw.newLine();
		}
		
		
	}
}