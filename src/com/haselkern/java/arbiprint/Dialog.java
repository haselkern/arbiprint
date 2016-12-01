package com.haselkern.java.arbiprint;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Dialog {

	public static void missingCredentials(){
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Missing credentials");
		alert.setContentText("Please enter username, password and printername.");

		alert.showAndWait();
		
	}
	
	public static void desktopMissing(){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Preferences");
		alert.setHeaderText("Couldn't open file");
	
		alert.setContentText("Preferences can be edited manually in the file:\n" + Prefs.getFile());

		alert.showAndWait();
	}
	
}
