package com.haselkern.java.arbiprint;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Dialog {

	public static void missingCredentials(){
		showError("Unvollständige Anmeldedaten", "Gib bitte Nutzername, Passwort und den Druckernamen ein.");
	}
	
	public static void loginFailed(){
		showError("Anmeldung fehlgeschlagen", "Überprüfe bitte deinen Benutzernamen und das Passwort.");
	}
	
	public static void hostUnreachable(){
		showError("Host nicht erreichbar", "Überprüfe bitte deine Internetverbindung und die Einstellungen.");
	}

	public static void updaterFailed(){
		showError("Update fehlgeschlagen", "Der Updateprozess konnte nicht gestartet werden. Lade die neueste Version manuell herunter:\n\nhaselkern.com/arbiprint");
	}

	private static void showError(String title, String msg){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Fehler");
		alert.setHeaderText(title);
		alert.setContentText(msg);

		alert.showAndWait();
	}

}
