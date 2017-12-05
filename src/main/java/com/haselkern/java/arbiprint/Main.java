package com.haselkern.java.arbiprint;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main extends Application {

	private ObservableList<File> files;
	private Button printButton;
	private Label dragDropLabel;
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

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

//		// Remove list entries with [DEL]
//		list.setOnKeyReleased(event -> {
//			if(event.getCode() == KeyCode.DELETE){
//				int i = list.getSelectionModel().getSelectedIndex();
//				if(i > -1)
//					files.remove(i);
//			}
//		});
//
//		// Enable drag drop
//		dragDropPane.setOnDragDropped(event -> {
//			Dragboard board = event.getDragboard();
//			if (board.hasFiles()) {
//				for (File f : board.getFiles()) {
//					files.add(f);
//					dragDropLabel.setVisible(false);
//				}
//			}
//		});
//
//		dragDropPane.setOnDragOver(event -> {
//			event.acceptTransferModes(TransferMode.COPY);
//		});
//
//		// Load possible printers
//		ObservableList<String> printers = FXCollections.observableArrayList(Prefs.getPrinters());
//
//		// Textfield for username, password and printername
//		TextField username = new TextField(Prefs.getUser());
//		username.setPromptText("ARBI Nutzername");
//
//		PasswordField password = new PasswordField();
//		password.setPromptText("Passwort");
//
//		ComboBox<String> printername = new ComboBox<>(printers);
//		printername.setPrefWidth(Double.MAX_VALUE);
//		printername.setEditable(true);
//		printername.setPromptText("Druckername");
//
//		// Jump to the next field
//		username.setOnAction((event) -> {
//			password.requestFocus();
//		});
//		password.setOnAction((event) -> {
//			printername.requestFocus();
//		});
//
//		// Print button and settings button
//		printButton = new Button("Drucken");
//		printButton.setPrefWidth(300);
//		Button settingsButton = new Button("Einstellungen");
//		settingsButton.setPrefWidth(100);
//
//		// Open preference window
//		settingsButton.setOnAction(event -> {
//
//			PrefWindow prefWindow = new PrefWindow();
//			prefWindow.initOwner(primaryStage);
//			prefWindow.initModality(Modality.WINDOW_MODAL);
//			prefWindow.show();
//
//		});
//
//		printButton.setOnAction(event -> {
//			// Check if all data has been entered
//			if(username.getText().length() == 0 || password.getText().length() == 0 || printername.getValue() == null || printername.getValue().length() == 0){
//				Dialog.missingCredentials();
//			}
//			else{
//				// Start printing
//				Printer printer = new Printer(
//						files,
//						username.getText(),
//						password.getText(),
//						printername.getValue(),
//						this
//					);
//				new Thread(printer).start();
//
//				// Save new prefs
//				Prefs.setUser(username.getText());
//				Prefs.addPrinter(printername.getValue());
//				// Set printers in gui
//				printers.setAll(Prefs.getPrinters());
//			}
//
//		});
//
//		HBox buttons = new HBox(printButton, settingsButton);
//		buttons.setSpacing(5);
//
//		// Hyperlink to install updates
//		Hyperlink updateLink = new Hyperlink("Update verfügbar");
//		updateLink.setManaged(false);
//		updateLink.setOnAction(event -> {
//
//			// Download updater
//			new Thread(this::runUpdater).start();
//
//		});
//
//		// Show hyperlink, if an update could be found
//		new Thread(() -> {
//			if(Version.updateAvailable()){
//				Platform.runLater(() -> {
//					updateLink.setManaged(true);
//				});
//			}
//		}).start();
//
//		// Create layout
//		VBox box = new VBox(updateLink, dragDropPane, username, password, printername, buttons);
//		box.setPadding(new Insets(5, 5, 5, 5));
//		box.setSpacing(5);


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
