package com.haselkern.java.arbiprint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Prefs {
	
	private static final String KEY_PRINT_CMD = "command";
	private static final String KEY_USER = "user";
	public static final String KEY_PASS = "password";
	private static final String KEY_PRINTER = "printers";
	private static final String DEFAULT_PRINT_CMD = "a2ps -1 -P %PRINTER% %FILE% 2>> arbiprint/log.txt";
	private static final String DEFAULT_USER = "";
	private static final String DEFAULT_PASSWORD = "";
	private static final String DEFAULT_PRINTER = "";
	private static final String KEY_HOST = "host";
	private static final String DEFAULT_HOST = "chiemsee.informatik.uni-oldenburg.de";
	
	
	// Single instance of properties we are working on
	private static Properties props;
	
	// Do not allow instancing
	private Prefs() { }
	
	public static String getPrintCommand(){
		check();
		return props.getProperty(KEY_PRINT_CMD);
	}
	
	public static String getUser(){
		check();
		return props.getProperty(KEY_USER);
	}
	public static String getPassword(){
		check();
		return props.getProperty(KEY_PASS);
	}

	public static String getHost(){
		check();
		return props.getProperty(KEY_HOST);
	}
	
	public static void setUser(String user){
		check();
		props.setProperty(KEY_USER, user);
		writeProps();
	}

	public static void setPassword(String password){
		check();
		props.setProperty(KEY_PASS, password);
		writeProps();
	}
	
	public static void setPrintCommand(String command){
		check();
		props.setProperty(KEY_PRINT_CMD, command);
		writeProps();
	}
	
	public static void setHost(String host){
		check();
		props.setProperty(KEY_HOST, host);
		writeProps();
	}

	// Restore default settings
	public static void revert(){
		check();
		props.setProperty(KEY_HOST, DEFAULT_HOST);
		props.setProperty(KEY_PRINTER, DEFAULT_PRINTER);
		props.setProperty(KEY_PRINT_CMD, DEFAULT_PRINT_CMD);
		props.setProperty(KEY_USER, DEFAULT_USER);
		props.setProperty(KEY_USER, DEFAULT_PASSWORD);
		writeProps();
	}
	
	// Check if properties are loaded and complete
	private static void check(){
		
		// Read props if they don't exist in memory
		if(props == null){
			props = readProps();
		}

		// Make sure all keys are set
		if(!props.containsKey(KEY_PRINT_CMD)){
			props.put(KEY_PRINT_CMD, DEFAULT_PRINT_CMD);
		}
		if(!props.containsKey(KEY_USER)){
			props.put(KEY_USER, DEFAULT_USER);
		}
		if(!props.containsKey(KEY_PRINTER)){
			props.put(KEY_PRINTER, DEFAULT_PRINTER);
		}
		if(!props.containsKey(KEY_HOST)){
			props.put(KEY_HOST, DEFAULT_HOST);
		}

		writeProps();
		
	}
	
	public static String getFile(){
		return getFolder() + "config.txt";
	}
	
	public static String getFolder(){
		return System.getProperty("user.home") + "/.haselkern/arbiprint/";
	}
	
	// Read properties from file
	private static Properties readProps(){
		Properties props = new Properties();
		
		try {
			
			BufferedReader reader = Files.newBufferedReader(new File(getFile()).toPath());
			String line;
			while((line = reader.readLine()) != null){
				
				if(line.contains("=")){
					String[] keyValue = line.split("=");
					String value = keyValue.length > 1 ? keyValue[1] : "";
					props.put(keyValue[0], value);
				}
				
			}
			
		} catch (IOException e) {
			// No file access
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return props;
	}
	
	// Write properties to file
	private static void writeProps(){

		try {
			
			// Create directories
			File folder = new File(getFolder());
			folder.mkdirs();

			// Write file
			StringBuilder out = new StringBuilder();
			for(Object key : props.keySet()){
				String k = (String)key;
				out.append(k);
				out.append("=");
				out.append(props.getProperty(k));
				out.append("\n");
			}
			
			FileOutputStream fos = new FileOutputStream(new File(getFile()), false);
			fos.write(out.toString().getBytes());
			fos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}