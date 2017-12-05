package com.haselkern.java.arbiprint;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Dialog {

	public static void missingCredentials(){
		showDialog(AlertType.ERROR,"Unvollständige Anmeldedaten", "Gib bitte Nutzername, Passwort und den Druckernamen ein.");
	}
	
	public static void loginFailed(){
		showDialog(AlertType.ERROR,"Anmeldung fehlgeschlagen", "Überprüfe bitte deinen Benutzernamen und das Passwort.");
	}
	
	public static void hostUnreachable(){
		showDialog(AlertType.ERROR,"Host nicht erreichbar", "Überprüfe bitte deine Internetverbindung und die Einstellungen.");
	}

	public static void updaterFailed(){
		showDialog(AlertType.ERROR,"Update fehlgeschlagen", "Der Updateprozess konnte nicht gestartet werden. Lade die neueste Version manuell herunter:\n\nhaselkern.com/arbiprint");
	}

	public static void cannotOpenWebsite(){
		showDialog(AlertType.INFORMATION, "Website", "ARBIprint findest du im Internet auf:\n\n" + Path.MAIN_WEBSITE);
	}

	private static void showDialog(AlertType alertType, String title, String msg){
		Platform.runLater(() -> {
			Alert alert = new Alert(alertType);
			alert.setTitle("Fehler");
			alert.setHeaderText(title);
			alert.setContentText(msg);

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("/ic_print.png"));

			alert.showAndWait();
		});
	}

}
