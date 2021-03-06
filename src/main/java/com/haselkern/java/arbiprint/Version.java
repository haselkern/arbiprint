package com.haselkern.java.arbiprint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Holds information about versions and version comparisions.
 */
public class Version {

	private static String onlineVersion = null;
	private static int jarFileSize = 0;

	// Prevent instancing
	private Version(){}

	/**
	 * Read the current version from file
	 */
	public static String getVersionString(){

		try{

			InputStream versionStream = Version.class.getResourceAsStream("/VERSION.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(versionStream));
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
	 * Checks if the online available version is higher than the local one
	 */
	public static boolean updateAvailable(){

		BufferedReader reader = null;
		try {

			// Load online version
			URL checkme = new URL(Path.RELEASE_INFO_JSON);
			reader = new BufferedReader(new InputStreamReader(checkme.openStream()));

			JsonObject releaseInfo = new JsonParser().parse(reader).getAsJsonObject();
			onlineVersion = releaseInfo.get("tag_name").getAsString();
			jarFileSize = releaseInfo.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("size").getAsInt();

			reader.close();

			// Compare versions
			return isNewerVersion(getVersionString(), onlineVersion);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static int getJarFileSize() {
		return jarFileSize;
	}

	/***
	 * Returns true, if newVersion has a greater Version code than oldVersion.
	 * Example: v1.0 < v1.1 < v1.1.1 < v1.1.10 < v2.0 ...
	 * @param oldVersion The current version
	 * @param newVersion The version we want to check against
	 * @return true, if newVersion is newer
	 */
	public static boolean isNewerVersion(String oldVersion, String newVersion){

		// Remove v
		if (oldVersion.startsWith("v"))
			oldVersion = oldVersion.substring(1);
		if (newVersion.startsWith("v"))
			newVersion = newVersion.substring(1);

		// Split version at dots
		String[] oldV = oldVersion.split("\\.");
		String[] newV = newVersion.split("\\.");

		for (int i = 0; i < Math.max(oldV.length, newV.length); i++) {

			// If newVersion has a version part we don't have, it's newer
			if (i == oldV.length && i < newV.length)
				return true;

			if (i < oldV.length && i < newV.length){
				int o = Integer.parseInt(oldV[i]);
				int n = Integer.parseInt(newV[i]);
				if (n > o)
					return true;
				else if(n < o)
					return false;
			}

		}

		return false;

	}
	
}
