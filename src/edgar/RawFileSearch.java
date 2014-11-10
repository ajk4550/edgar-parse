package edgar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RawFileSearch {
	// Instance Variables
	ArrayList<String> fileNames;
	String[] keywords;
	
	public RawFileSearch() {
		this.keywords = new String[] {"license agreement","license", "agreement", "licensing agreement"};	
	}
	
	public RawFileSearch(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
		this.keywords = new String[] {"license agreement","license", "agreement","licensing agreement"};	
	}
	
	public void beginSearch() throws IOException {
		// looping through the list of files
		for (int i=0; i<fileNames.size();i++) {
			// getting the path to a single file
			String path = fileNames.get(i);
			// splitting the URI into the various parts
			String[] sortedURI = path.split("/");
			// Creating a new file object for the current file
			File file = new File("download/" + sortedURI[3]);
			// Creating a new BufferedReader to read the file
			BufferedReader reader = new BufferedReader(new FileReader(file));
			// Initializing the line variable that will hold the current line in file
			String line = null;
			// Variable used to determine if keyword was found in text, default to false
			boolean found = false;
			
			// Looping through the lines in the file
			while((line = reader.readLine()) != null) {
				// Taking the line in the file and splitting to the individual words
				String[] lineWords = line.split("");
				// Looping through each of the words
				for (int j=0;j<lineWords.length;j++) {
					// Taking that individual word and looping through a list of keywords to see if it is found
					for (int k=0;k<keywords.length;k++) {
						// If the keyword is found, mark found as true
						if (lineWords[j].equalsIgnoreCase(keywords[k])) {
							found = true;
						}
					}
					
				}
			}
			// Check if the file was found
			if(!found) {
				// Delete the file since the keyword was not found in the file
				if(file.delete()) {
					System.out.println(sortedURI[3] + " deleted! (keyword not found)");
				} else {
					System.out.println("Keyword found in file " + sortedURI[3]);
				}
			}
		}
	}
	
	public void beginSearchOfFile(String filename) throws IOException {
		System.out.println("Searching for keywords in " + filename);
		
		File file = new File("download/" + filename);
		// Creating a new BufferedReader to read the file
		BufferedReader reader = new BufferedReader(new FileReader(file));
		// Initializing the line variable that will hold the current line in file
		String line = null;
		// Variable used to determine if keyword was found in text, default to false
		boolean found = false;
		
		// Looping through the lines in the file
		while((line = reader.readLine()) != null) {
			// Taking the line in the file and splitting to the individual words
			String[] lineWords = line.split(" ");
			// Looping through each of the words
			for (int j=0;j<lineWords.length;j++) {
				// Taking that individual word and looping through a list of keywords to see if it is found
				for (int k=0;k<keywords.length;k++) {
					// If the keyword is found, mark found as true
					if (lineWords[j].equalsIgnoreCase(keywords[k])) {
						found = true;
					}
				}
				
			}
		}
		// Check if the file was found
		if(!found) {
			// Delete the file since the keyword was not found in the file
			if(file.delete()) {
				System.out.println(filename + " deleted! (keyword not found)");
			} else {
				System.out.println("Keyword found in file " + filename);
			}
		}
	}
}
