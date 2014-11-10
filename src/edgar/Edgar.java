package edgar;

import java.util.ArrayList;

public class Edgar {
	public static void main(String[] args) {
		// Creating a new FTPHelper
		FTPHelper edgarFTP = new FTPHelper();
		try {
			// Parsing through the master.idx file and obtaining an array of the file names
			MasterParser mp = new MasterParser("master.idx");
			ArrayList<String> fileList = mp.parseFileToArray();
			// Connecting to the remote Server
			edgarFTP.connectToServer("ftp.sec.gov");
			// Downloading all of the files in the array
			edgarFTP.downloadBatchFiles(fileList);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
