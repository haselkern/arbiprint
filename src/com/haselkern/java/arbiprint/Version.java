package com.haselkern.java.arbiprint;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Version {

	/**
	 * Read version from file
	 */
	public static String getString(){

		try{

			BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(("resources/VERSION.txt"))));
			String version = reader.readLine();
			reader.close();

			return version;

		}catch (Exception e){
			e.printStackTrace();
			// Couldn't load version
			return "";
		}

	}

	/**
	 * Returns a numeric representation of the version string.
	 * It should be formatted like 1, 1.1, or 1.1.1
	 * If the version string is not formatted correctly, this will return 0
	 */
	public static double getDouble(){

		String version = getString();

		// Parse to double string
		boolean hasDot = false;
		StringBuilder cleanString = new StringBuilder();
		for(char c : version.toCharArray()){
			// Append only the first dot
			if(c == '.'){
				if(!hasDot){
					hasDot = true;
					cleanString.append('.');
				}
			}
			// All other chars can be appended
			else{
				cleanString.append(c);
			}
		}

		// Return double from cleaned string
		try{
			return Double.parseDouble(cleanString.toString());
		} catch (Exception e){
			e.printStackTrace();
			return 0;
		}

	}
	
}
