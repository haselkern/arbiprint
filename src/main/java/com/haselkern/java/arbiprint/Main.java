package com.haselkern.java.arbiprint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Main entry point for the program. Launches the GUI and checks for updates.
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Check if we are currently in update mode
		// Get parameter
		final String arbiprintJar = getParameters().getNamed().get("path");

		// If we are in update mode
		if(arbiprintJar != null){
			// Copy jar to new location
			Files.copy(Paths.get(Path.getTemporaryJarPath()), Paths.get(arbiprintJar), StandardCopyOption.REPLACE_EXISTING);

			// Run new jar
			ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", arbiprintJar);
			processBuilder.start();
			System.exit(0);
		}

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
		MainController controller = new MainController(primaryStage);
		fxmlLoader.setController(controller);
		Parent layout = fxmlLoader.load();

		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("/ic_print.png"));

		primaryStage.setTitle("ARBIprint " + Version.getVersionString());
		primaryStage.setResizable(false);
		primaryStage.show();

		// Check for updates
		new Thread(() -> {
			if(Version.updateAvailable()) {
				controller.updateAvailable();
			}
		}).start();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
