package com.haselkern.java.arbiprint;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PrefWindow extends Stage {

	@FXML
	private TextField printerCommand;
	@FXML
	private TextField printerHost;

	public PrefWindow() {
		
		// Set icon
		getIcons().add(new Image("/ic_print.png"));
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/prefs.fxml"));
		loader.setController(this);
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

	@FXML
	public void initialize(){
		printerCommand.setText(Prefs.getPrintCommand());
		printerHost.setText(Prefs.getHost());
	}

	@FXML
	public void save(){
		Prefs.setPrintCommand(printerCommand.getText());
		Prefs.setHost(printerHost.getText());
		close();
	}

	@FXML
	public void reset(){
		Prefs.revert();
		close();
	}

	@FXML
	public void cancel(){
		close();
	}

	
}
