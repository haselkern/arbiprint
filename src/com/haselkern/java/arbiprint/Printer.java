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
	private String printername, command;
	
	public Printer(List<File> filelist, String username, String password, String printername, String command, Main callback){
		files = new File[filelist.size()];
		files = filelist.toArray(files);
		
		this.callback = callback;
		this.username = username;
		this.password = password;
		this.printername = printername;
		this.command = command;
	}
	
	@Override
	public void run() {
		
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
				
				String filePath = "arbiprint/" + f.getName();
				
				// Upload
				sftpChannel.put(f.getAbsolutePath(), filePath);
				
				// Run print command
				ChannelExec execChannel = (ChannelExec) session.openChannel("exec");
				execChannel.setCommand(command.replaceAll("%FILE%", filePath).replaceAll("%PRINTER%", printername));
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
