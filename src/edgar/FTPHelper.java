package edgar;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class FTPHelper {
	// Instance Variables
	FTPClient client;
	RawFileSearch rfs;
	
	/**
	 * Class for connecting to a remote FTP server and downloading a file
	 */
	public FTPHelper() {
		this.client = new FTPClient();
		rfs = new RawFileSearch();
	}
	
	/**
	 * Method for connecting to a remote server. 
	 * It is necessary to first run this method so the client is connected
	 * @param server the remote server to connect to
	 * @throws FTPException generic FTP error. Please view stack trace
	 * @throws FTPIllegalReplyException Thrown when server replies in an illegal way
	 * @throws IOException File related exception
	 */
	public void connectToServer(String server) throws FTPException, FTPIllegalReplyException, IOException {
		// Connecting to the remote site
		client.connect(server);
		// Anonymous connection to the remote site
		client.login("anonymous", "ftp4j");
	}
	
	/**
	 * Method for downloading a single file from a given filename.
	 * Method assumes the directory is edgar/data/cki/filename
	 * The file will download to download/filename
	 * @param cki The company's Central Index Team
	 * @param filename the file on the server to download.
	 * @throws FTPException Generic FTP exception
	 * @throws FTPIllegalReplyException Thrown when server replies in an illegal way
	 * @throws IOException Generic file exception. See stack trace
	 * @throws FTPAbortedException Thrown if FTP connection is aborted
	 * @throws FTPDataTransferException Thrown due to network errors
	 */
	public void downloadSingleFile(String cki, String filename) throws FTPException, FTPIllegalReplyException, IOException, FTPAbortedException, FTPDataTransferException {
		// changing into the proper directory
		client.changeDirectory("/edgar");
		client.changeDirectory("data/" + cki + "/");
		// Downloading the file
		System.out.println("Beginning Download ...");
		client.download(filename, new java.io.File("download/" + filename));
		System.out.println("Download complete!");
		// Cleaning the downloaded files
		rfs.beginSearchOfFile(filename);
	}
	
	/**
	 * Method for downloading a batch of files
	 * This method takes an arrayList in the format /edgar/data/cik/filename
	 * @param fileNames an ArrayList<String> that holds a list of the filepaths
	 * @throws FTPException
	 * @throws FTPIllegalReplyException
	 * @throws IOException
	 * @throws FTPAbortedException
	 * @throws FTPDataTransferException
	 */
	public void downloadBatchFiles(ArrayList<String> fileNames) throws FTPException, FTPIllegalReplyException, IOException, FTPAbortedException, FTPDataTransferException {
		// Arrays to hold the counts for the array size, number of errors, and number of downloads
		int arraySize = fileNames.size();
		int errorCount = 0;
		int downloadCount = 0;
		// looping through each of the files
		for(int i=0; i<arraySize; i++) {
			// Pause the download every 5 seconds to prevent request timeout
			try {
				if (downloadCount%20 == 0) {
					// Pause after 20 downloads for 5 seconds
					try {
						sleepDownload(5000);
					} catch(InterruptedException ie) {
						ie.printStackTrace();
					}
				}
				// Get the name of a file from the arrayList of files
				String filePath = fileNames.get(i);
				// split the path into the different parts
				String[] fileURISplit = filePath.split("/");
				// Assign the clients CKI from the split URL
				String cki = fileURISplit[2];
				// Assign the filename based on the split URL
				String filename = fileURISplit[3];
				// Displaying message to the user
				System.out.println("Downloading file " + (i+1) + " out of " + (arraySize+1) + " - " + filePath);
				// Downloading the single file
				downloadSingleFile(cki, filename);
				// Increasing the download count
				downloadCount++;
				// Disconnecting from the client
				client.disconnect(true);
				// Reconnecting to the client
				connectToServer("ftp.sec.gov");
			} catch (FTPException ftpe) {
				System.out.println("General FTP Error");
				try {
					sleepDownload(30000);
				} catch(InterruptedException ie) {
					ie.printStackTrace();
				}
				errorCount++;
			} catch (SocketTimeoutException ste) {
				System.out.println("Request timed out");
				try {
					sleepDownload(30000);
				} catch(InterruptedException ie) {
					ie.printStackTrace();
				}
				errorCount++;
			}
			System.out.println();
		}
		System.out.println("----------------------------------------");
		System.out.println("Batch Download complete!");
		System.out.println(errorCount + " files failed to download");
	}
	
	private void sleepDownload(int sleepTime) throws InterruptedException, IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		// Disconnecting from the client
		client.disconnect(true);
		System.out.println("Sleeping for " + (sleepTime/1000) + " seconds" );
		// Sleeping for the given amount of milliseconds
		Thread.sleep(sleepTime);
		connectToServer("ftp.sec.gov");
	}
}
