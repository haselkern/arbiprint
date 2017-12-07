package com.haselkern.java.arbiprint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Path {

    public static final String RELEASE_WEBSITE = "https://github.com/haselkern/arbiprint/releases/latest";
    public static final String RELEASE_INFO_JSON = "https://api.github.com/repos/haselkern/arbiprint/releases/latest";
    public static final String MAIN_WEBSITE = "http://haselkern.com/arbiprint";

    public static String getNewestJarURL() throws IOException {

        URL url = new URL(Path.RELEASE_INFO_JSON);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        JsonObject releaseInfo = new JsonParser().parse(reader).getAsJsonObject();
        String jarURL = releaseInfo.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();

        reader.close();

        return jarURL;
    }

    public static String getTemporaryJarPath() {
        return Prefs.getFolder() + "arbiprint.jar";
    }

}
