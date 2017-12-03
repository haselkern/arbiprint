package com.haselkern.java.arbiprint;

import java.io.File;
import java.net.UnknownHostException;
import java.util.List;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import javafx.application.Platform;

public class Printer implements Runnable {

	private File[] files;
	private Main callback;
	private String username, password;
	private String printername;
	
	public Printer(List<File> filelist, String username, String password, String printername, Main callback){
		files = new File[filelist.size()];
		files = filelist.toArray(files);
		
		this.callback = callback;
		this.username = username;
		this.password = password;
		this.printername = printername;
	}
	
	@Override
	public void run() {
		
		System.out.println("Printing " + files.length + " files as user " + username + " on printer " + printername);
		System.out.println("Host: " + Prefs.getHost());
		System.out.println("Command: " + Prefs.getPrintCommand());
		
		Platform.runLater(() -> {
			callback.setEnablePrintButton(false);
		});

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
					Thread.sleep(1000);
				} catch(Exception e){}
				
				String filePath = "arbiprint/" + f.getName().replace(" ", "_");
				
				// Upload
				sftpChannel.put(f.getAbsolutePath(), filePath);
				
				// Run print command
				ChannelExec execChannel = (ChannelExec) session.openChannel("exec");
				execChannel.setCommand(Prefs.getPrintCommand().replaceAll("%FILE%", filePath).replaceAll("%PRINTER%", printername));
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
				Platform.runLater(() -> Dialog.hostUnreachable());
			}
			else{				
				Platform.runLater(() -> Dialog.loginFailed());
			}
			
		} catch (SftpException e) {
			e.printStackTrace();
		}
		
		Platform.runLater(() -> {
			callback.setEnablePrintButton(true);
		});

	}
	
}