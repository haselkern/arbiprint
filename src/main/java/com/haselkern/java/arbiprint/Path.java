package com.haselkern.java.arbiprint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A class for providing all paths for files and websites.
 */
public class Path {

    public static final String RELEASE_WEBSITE = "https://github.com/haselkern/arbiprint/releases/latest";
    public static final String RELEASE_INFO_JSON = "https://api.github.com/repos/haselkern/arbiprint/releases/latest";
    public static final String MAIN_WEBSITE = "http://haselkern.com/arbiprint";

    /**
     * Checks github for the link to the latest jar file of ARBIprint
     * @return The URL to the latest jar
     * @throws IOException If something went wrong.
     */
    public static String getNewestJarURL() throws IOException {

        URL url = new URL(Path.RELEASE_INFO_JSON);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        JsonObject releaseInfo = new JsonParser().parse(reader).getAsJsonObject();
        String jarURL = releaseInfo.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();

        reader.close();

        return jarURL;
    }

    /**
     * @return The path to a temporary JAR location, used for updating.
     */
    public static String getTemporaryJarPath() {
        return getFolder() + "arbiprint.jar";
    }

    /**
     * @return The path to the saved preferences.
     */
    public static String getPropertyPath(){
        return getFolder() + "config.txt";
    }

    /**
     * @return The path to a folder we can save files in.
     */
    public static String getFolder(){
        return System.getProperty("user.home") + "/.haselkern/arbiprint/";
    }

}
