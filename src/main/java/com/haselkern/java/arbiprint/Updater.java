package com.haselkern.java.arbiprint;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
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
}
