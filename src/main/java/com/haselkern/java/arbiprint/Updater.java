package com.haselkern.java.arbiprint;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Lars on 19.01.2017.
 */
public class Updater {

	public static Stage getLoadingStage(){

		// Create progressbar
		ProgressBar pb = new ProgressBar();
		pb.setPrefWidth(400);
		pb.setPrefHeight(50);

		// Create label
		Label l = new Label("aktualisiere...");

		// Stack label on progressbar
		StackPane pane = new StackPane(pb, l);

		// Set scene
		Scene scene = new Scene(pane);
		Stage stage = new Stage();
		stage.setScene(scene);

		// Set icon
		stage.getIcons().add(new Image("/ic_print.png"));

		// Setup stage
		stage.setTitle("ARBIprint " + Version.getVersionString());
		stage.setResizable(false);

		return stage;
	}

	/**
	 * Can we run the update process?
	 */
	public static boolean canAutoUpdate(){
		try {

			// If this doesn't throw an exception, then we can start jar files
			ProcessBuilder pb = new ProcessBuilder("java", "-version");
			Process p = pb.start();
			p.destroy();

			return true;

		} catch (IOException e) {
			// This exception occurs, when 'java' is not in PATH
			return false;
		}
	}

	public static void update(Stage primaryStage){

		try {
			// If autoupdate doesn't work, the user has to manually download
			if(!Updater.canAutoUpdate()){
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().browse(new URI(Path.RELEASE_WEBSITE));
				}
				// If we cannot open the website, the user has to open it manually
				else{
					Dialog.updaterFailed();
				}
				return;
			}

			Platform.runLater(() -> {
				// Hide primaryStage
				primaryStage.hide();
				// Show loading window
				getLoadingStage().show();
			});

			// Download updater
			System.out.println("Downloading updater...");
			String updaterJar = Path.getTemporaryJarPath();

			URL updaterURL = new URL(Path.getNewestJarURL());
			InputStream in = updaterURL.openStream();
			Files.copy(in, Paths.get(updaterJar), StandardCopyOption.REPLACE_EXISTING);

			// Get path to currently running jar
			File jar = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();

			// For Debugging purposes, if the path is not a jar, modify it, so that updating works
			if(!jar.isFile()){
				jar = new File(jar, "arbiprint.jar");
			}

			// Run updater
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", updaterJar, "--path="+jar.getAbsolutePath());
			pb.start();

			// Exit
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
			Dialog.updaterFailed();
		}

	}
}
