package com.haselkern.java.arbiprint;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main Window for changing settings.
 */
public class PrefWindow extends Stage {

	public PrefWindow(IMainCallback callback) {
		
		// Set icon
		getIcons().add(new Image("/ic_print.png"));
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/prefs.fxml"));
		loader.setController(new PrefWindowController(this, callback));
		Parent content = null;
		try {
			content = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set scene and title
		Scene scene = new Scene(content);
		setScene(scene);
		setTitle("Einstellungen");
		
	}

}
