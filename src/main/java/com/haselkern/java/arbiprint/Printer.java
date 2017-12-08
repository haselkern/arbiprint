package com.haselkern.java.arbiprint;

import com.jcraft.jsch.*;
import javafx.application.Platform;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link Printer} handles connecting to the server and printing.
 */
public class Printer implements Runnable {

	/**
	 * @return A list of all available printers in the ARBI. Pairs are [display name, real name]
	 */
	public static Map<String, String> getPrinters(){

		Map<String, String> printers = new HashMap<>();
		printers.put("Blau", "lwblau");
		printers.put("Grün", "lwgruen");
		printers.put("Orange", "lworange");
		printers.put("Rot", "lwrot");
		printers.put("Violett", "lwviolett");
		return printers;

	}

	private File[] files;
	private IMainCallback callback;
	private String username, password;
	private String printername;

	/**
	 * Creates a new Printer object.
	 * @param filelist The list of files to be printed. May be null, if you do not want to call {@link Printer#run}
	 * @param username The username
	 * @param password The password of the user
	 * @param printername The name of the priner to print on. May be null, if you do not want to call {@link Printer#run}
	 * @param callback A callback for handling interactions with the GUI
	 */
	public Printer(List<File> filelist, String username, String password, String printername, IMainCallback callback){
		if (filelist != null){
			files = new File[filelist.size()];
			files = filelist.toArray(files);
		}

		this.username = username;
		this.password = password;
		this.printername = printername;
		this.callback = callback;
	}

	/**
	 * Starts the printing process.
	 */
	@Override
	public void run() {
		
		System.out.println("Printing " + files.length + " files as user " + username + " on printer " + printername);
		System.out.println("Host: " + Prefs.getHost());
		System.out.println("Command: " + Prefs.getPrintCommand());
		callback.setPrintButtonEnabled(false);
		
		try {

			JSch jsch = new JSch();
			Session session = jsch.getSession(username, Prefs.getHost(), 22);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			
			// Try to create folder
			try{
				sftpChannel.mkdir("arbiprint");				
			} catch(Exception e){
				// Folder already exists
			}
			
			// Upload and print files
			for(File f : files){
				
				try{
					// Sleep for a short while, so that the printer can keep up
					Thread.sleep(1000);
				} catch(Exception e){
					// Exception is irrelevant
				}

				String filePath = "arbiprint/" + Base64.getEncoder().encodeToString(f.getName().getBytes());
				
				// Upload
				sftpChannel.put(f.getAbsolutePath(), filePath);
				
				// Run print command
				ChannelExec execChannel = (ChannelExec) session.openChannel("exec");
				String printCommand = Prefs.getPrintCommand().replaceAll("%FILE%", filePath).replaceAll("%PRINTER%", printername);
				System.out.println("running: " + printCommand);
				execChannel.setCommand(printCommand);
				execChannel.connect();
				execChannel.disconnect();
				
				Platform.runLater(() -> {
					callback.removeFileFromList(f);
				});
			}

			sftpChannel.disconnect();
			session.disconnect();

		} catch(JSchException e){
			e.printStackTrace();

			// If e has a cause (UnkownHostException)
			if(e.getCause() != null && e.getCause().getClass() == UnknownHostException.class){
				Dialog.hostUnreachable();
			}
			else{				
				Dialog.loginFailed();
			}

		} catch (SftpException e) {
			e.printStackTrace();
		}
		
		callback.setPrintButtonEnabled(true);

	}

	/**
	 * Clears all the temporary files from the server.
	 * @throws JSchException If connecting or logging in went wrong.
	 */
	public void clearServerFiles() throws JSchException {

		JSch jsch = new JSch();
		Session session = jsch.getSession(username, Prefs.getHost(), 22);
		session.setPassword(password);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();

		ChannelExec execChannel = null;
		execChannel = (ChannelExec) session.openChannel("exec");
		execChannel.setCommand("rm arbiprint/*");
		execChannel.connect();
		execChannel.disconnect();

		session.disconnect();

	}
	
}
