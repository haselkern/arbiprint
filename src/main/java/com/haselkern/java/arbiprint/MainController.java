package com.haselkern.java.arbiprint;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainController implements IGUICallback {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ComboBox<String> printername;
    @FXML
    private Button printButton;
    @FXML
    private StackPane dragDropPane;
    @FXML
    private Label dragDropLabel;

    private List<File> files;
    private Stage primaryStage;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        files = new ArrayList<>();
    }

    @FXML
    public void initialize(){

        // Setup printernames
        Printer.getPrinters().keySet().forEach(s -> printername.getItems().add(s));

        // Setup dragDrop
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

    }

    @FXML
    public void print(){
        // Check if all data has been entered
        if(username.getText().length() == 0 || password.getText().length() == 0 || printername.getValue() == null || printername.getValue().length() == 0){
            Dialog.missingCredentials();
        }
        // All data was entered correctly
        else{

            // Get actual printername from displayname
            String realPrinterName = Printer.getPrinters().get(printername.getValue());

            // Start printing
            Printer printer = new Printer(
                files,
                username.getText(),
                password.getText(),
                realPrinterName,
                this
            );
            new Thread(printer).start();

            // Save new prefs
            Prefs.setUser(username.getText());
        }
    }

    @FXML
    public void quit(){
        Platform.exit();
    }

    @FXML
    public void settings(){
        PrefWindow prefWindow = new PrefWindow();
        prefWindow.initOwner(primaryStage);
        prefWindow.initModality(Modality.WINDOW_MODAL);
        prefWindow.show();
    }

    @Override
    public void setPrintButtonEnabled(boolean enabled) {
        Platform.runLater(() -> {
            printButton.setDisable(!enabled);
        });
    }

    @Override
    public void removeFileFromList(File f) {
        // TODO
    }
}
