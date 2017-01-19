package com.haselkern.java.arbiprint;

import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

	private ObservableList<File> files;
	private Button printButton;
	private Label dragDropLabel;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		
		// Setup drag And Drop pane with image
		Image image = new Image("resources/ic_copy.png");
		ImageView imageView = new ImageView(image);
		dragDropLabel = new Label("Dateien hierhin ziehen", imageView);
		dragDropLabel.setContentDisplay(ContentDisplay.TOP);
		// Create drag and drop list
		ListView<File> list = new ListView<File>();
		files = FXCollections.observableArrayList();
		list.setItems(files);
		
		StackPane dragDropPane = new StackPane(list, dragDropLabel);

		// Remove list entries with [DEL]
		list.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.DELETE){
				int i = list.getSelectionModel().getSelectedIndex();
				if(i > -1)
					files.remove(i);	
			}
		});
		
		// Enable drag drop
		dragDropPane.setOnDragDropped(event -> {
			Dragboard board = event.getDragboard();
			if (board.hasFiles()) {
				for (File f : board.getFiles()) {
					files.add(f);
					dragDropLabel.setVisible(false);
				}
			}
		});

		dragDropPane.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.COPY);
		});
		
		// Load possible printers
		ObservableList<String> printers = FXCollections.observableArrayList(Prefs.getPrinters());

		// Textfield for username, password and printername
		TextField username = new TextField(Prefs.getUser());
		username.setPromptText("ARBI Nutzername");
		
		PasswordField password = new PasswordField();
		password.setPromptText("Passwort");
		
		ComboBox<String> printername = new ComboBox<String>(printers);
		printername.setPrefWidth(Double.MAX_VALUE);
		printername.setEditable(true);
		printername.setPromptText("Druckername");

		// Print button and settings button
		printButton = new Button("Drucken");
		printButton.setPrefWidth(300);
		Button settingsButton = new Button("Einstellungen");
		settingsButton.setPrefWidth(100);
		
		HBox buttons = new HBox(printButton, settingsButton);
		buttons.setSpacing(5);

		// Open preference window
		settingsButton.setOnAction(event -> {
			
			PrefWindow prefWindow = new PrefWindow();
			prefWindow.initOwner(primaryStage);
			prefWindow.initModality(Modality.WINDOW_MODAL);
			prefWindow.show();
			
		});
		
		printButton.setOnAction(event -> {
			// Check if all data has been entered
			if(username.getText().length() == 0 || password.getText().length() == 0 || printername.getValue() == null){
				Dialog.missingCredentials();
			}
			else{
				// Start printing
				Printer printer = new Printer(
						files,
						username.getText(),
						password.getText(),
						printername.getValue(),
						this
					);
				new Thread(printer).start();
				
				// Save new prefs
				Prefs.setUser(username.getText());
				Prefs.addPrinter(printername.getValue());
				// Set printers in gui
				printers.setAll(Prefs.getPrinters());
			}
			
		});

		// Create layout
		VBox box = new VBox(dragDropPane, username, password, printername, buttons);
		box.setPadding(new Insets(5, 5, 5, 5));
		box.setSpacing(5);

		Scene scene = new Scene(box, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("resources/ic_print.png"));

		primaryStage.setTitle("ARBIprint " + Version.getString());
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	public void setEnablePrintButton(boolean enabled){
		printButton.setDisable(!enabled);
	}
	
	public void removeFileFromList(File f){
		files.remove(f);
		if(files.isEmpty()){
			dragDropLabel.setVisible(true);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
