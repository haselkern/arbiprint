package com.haselkern.java.arbiprint.updater;

import com.haselkern.java.arbiprint.Dialog;
import com.haselkern.java.arbiprint.Version;
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
public class Updater extends Application {

	// URL of the updater.jar file
	public static final String url = "https://github.com/haselkern/arbiprint/raw/master/docs/updater.jar";

	// URL of the arbiprint.jar file
	public static final String arbiprintURL = "https://github.com/haselkern/arbiprint/raw/master/docs/arbiprint.jar";

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Get parameter
		final String arbiprintJar = getParameters().getNamed().get("path");

		// Exit, if no paramater was provided
		if(arbiprintJar == null){
			System.out.println("no path provided");
			Platform.exit();
			return;
		}

		// Show window
		getLoadingStage().show();

		// Download and run new version
		new Thread(() -> {

			try{

				// Download
				URL arbiprintURL = new URL(Updater.arbiprintURL);
				InputStream in = arbiprintURL.openStream();
				Files.copy(in, Paths.get(arbiprintJar), StandardCopyOption.REPLACE_EXISTING);

				// Run new jar
				ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", arbiprintJar);
				processBuilder.start();

			} catch(Exception e){
				e.printStackTrace();
				// Show error dialog
				Platform.runLater(() -> {
					Dialog.updaterFailed();
				});
			}

			// Exit in any case
			Platform.exit();

		}).start();

	}

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
		stage.getIcons().add(new Image("resources/ic_print.png"));

		// Setup stage
		stage.setTitle("ARBIprint " + Version.getString());
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

			return true;

		} catch (IOException e) {
			// This exception occurs, when 'java' is not in PATH
			return false;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
