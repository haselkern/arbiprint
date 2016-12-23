package com.haselkern.java.arbiprint;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrefWindow extends Stage {

	public PrefWindow(){
		
		// Set icon
		getIcons().add(new Image("resources/ic_print.png"));
		
		// Components
		Label l1 = new Label("Print command:");
		TextField printCommandField = new TextField(Prefs.getPrintCommand());
		printCommandField.setPrefWidth(500);
		
		Label l2 = new Label("Host:");
		TextField hostField = new TextField(Prefs.getHost());

		// Cancel button
		Button buttonCancel = new Button("Cancel");
		buttonCancel.setOnAction(event -> {
			close();
		});
		
		// Save button
		Button buttonSave = new Button("Save");
		buttonSave.setOnAction(event -> {
			Prefs.setPrintCommand(printCommandField.getText());
			Prefs.setHost(hostField.getText());
			close();
		});
		
		// Revert button
		Button buttonRevert = new Button("Restore defaults");
		buttonRevert.setOnAction(event -> {
			Prefs.revert();
			close();
		});
		
		// Make button row
		HBox buttons = new HBox(buttonSave, buttonRevert, buttonCancel);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(5);
		
		// Create layout
		VBox content = new VBox(l1, printCommandField, l2, hostField, buttons);
		content.setPadding(new Insets(5, 5, 5, 5));
		content.setSpacing(5);
		
		// Set scene and title
		Scene scene = new Scene(content);
		setScene(scene);
		setTitle("Preferences");
		
	}
	
}
