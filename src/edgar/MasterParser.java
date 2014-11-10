package edgar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MasterParser {
	// Instance Variables
	BufferedReader reader;
	
	/**
	 * Method for parsing master.idk files
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public MasterParser(String filename) throws FileNotFoundException {
		// Creating a new file BufferedReader from the filename given
		reader = new BufferedReader(new FileReader(filename));
	}
	
	/**
	 * Method that parses through the Master.idx file and parses the file name out
	 * @return an ArrayList of the file names
	 * @throws IOException Generic exception thrown due to file issues
	 */
	public ArrayList<String> parseFileToArray() throws IOException {
		// ArrayList to hold the Final list of files
		ArrayList<String> fileList = new ArrayList<String>();
		// Initializing a line variable to hold current file line
		String line = null;
		// Looping through the file
		while((line = reader.readLine()) != null) {
			// On the current line in the file, find the first instance of edgar
			int startingIndex = line.indexOf("edgar");
			// tempFileName is a temporary variable to hold the fileName that will be built
			String tempFileName = "";
			// Looping through from the beginning of file name (edgar) to the end of that line
			for(int i = startingIndex; i<line.length(); i++) {
				// Appending the char at that position to the tempFileName
				tempFileName+=line.charAt(i);
			}
			// Add the tempFileName that was just built to the ArrayList
			fileList.add(tempFileName);
		}
		// return the list of FileNames
		return fileList;
	}
}
