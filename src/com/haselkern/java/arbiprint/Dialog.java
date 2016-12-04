package com.haselkern.java.arbiprint;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Dialog {

	public static void missingCredentials(){
		showError("Missing credentials", "Please enter username, password and printername.");
	}
	
	public static void loginFailed(){
		showError("Login failed", "Wrong password or username.");
	}
	
	public static void hostUnreachable(){
		showError("Host unreachable", "Check your internet connection or enter a different host in settings.");
	}
	
	private static void showError(String title, String msg){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(title);
		alert.setContentText(msg);

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
