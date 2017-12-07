package com.haselkern.java.arbiprint;

import com.jcraft.jsch.JSchException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * The MainController handles all the interactions in the main window.
 */
public class MainController implements IMainCallback {

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
    @FXML
    private CheckBox savePassword;
    @FXML
    private Hyperlink updateLink;
    @FXML
    private ListView<File> fileList;

    private ObservableList<File> files;
    private Stage primaryStage;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        files = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize(){

        // Setup printernames
        Printer.getPrinters().keySet().forEach(s -> printername.getItems().add(s));

        // Setup dragDrop
        dragDropPane.setOnDragDropped(event -> {
			Dragboard board = event.getDragboard();
			if (board.hasFiles()) {
                files.addAll(board.getFiles());
			}
		});
		dragDropPane.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.COPY);
		});

		// Setup file list
        fileList.setItems(files);
        // Hide/Show hint to drop files, if there are files
        files.addListener((ListChangeListener<? super File>) c -> {
            dragDropLabel.setVisible(files.isEmpty());
        });
        // Setup cellFactory
        fileList.setCellFactory(listView -> {
            ListCell<File> cell = new ListCell<>();

            // Setup contextmenu
            MenuItem deleteItem = new MenuItem("Entfernen");
            deleteItem.setOnAction(event -> {
                removeFileFromList(cell.getItem());
            });
            MenuItem deleteAllItem = new MenuItem("Alle löschen");
            deleteAllItem.setOnAction(event -> {
                files.clear();
            });
            ContextMenu contextMenu = new ContextMenu(deleteItem, deleteAllItem);

            // Setup listener to set text and contextmenu
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                    cell.setText("");
                } else {
                    cell.setContextMenu(contextMenu);
                    cell.setText(cell.getItem().getName());
                }
            });

            return cell;
        });

        // Remove list entries with [DEL]
		fileList.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.DELETE){
                for (File file : fileList.getSelectionModel().getSelectedItems()) {
                    removeFileFromList(file);
                }
			}
		});

		// Set username and Password
        username.setText(Prefs.getUser());
        password.setText(Prefs.getPassword());
        // If password is saved, keep the checkbox to save it checked
        savePassword.setSelected(password.getText().length() > 0);

        // Hide updateLink
        updateLink.setManaged(false);

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
            // Save password, if check is set
            if(savePassword.isSelected()) {
                Prefs.setPassword(password.getText());
            }
            // Reset password, if it shouldn't be saved
            else{
                Prefs.setPassword("");
            }
        }
    }

    @FXML
    public void quit(){
        Platform.exit();
    }

    @FXML
    public void settings(){
        PrefWindow prefWindow = new PrefWindow(this);
        prefWindow.initOwner(primaryStage);
        prefWindow.initModality(Modality.WINDOW_MODAL);
        prefWindow.show();
    }

    @FXML
    public void update(){

        new Thread(() -> Updater.update(primaryStage)).start();

    }

    @FXML
    public void website(){

        try {
            Desktop.getDesktop().browse(new URI(Path.MAIN_WEBSITE));
        } catch (IOException | URISyntaxException e) {
            Dialog.cannotOpenWebsite();
        }

    }

    @Override
    public void setPrintButtonEnabled(boolean enabled) {
        Platform.runLater(() -> {
            printButton.setDisable(!enabled);
        });
    }

    @Override
    public void removeFileFromList(File f) {
        files.remove(f);
    }

    @Override
    public void updateAvailable() {
        Platform.runLater(() -> {
            updateLink.setManaged(true);
        });
    }

    @Override
    public void clearFields() {
        username.clear();
        password.clear();
        savePassword.setSelected(false);
        printername.getSelectionModel().clearSelection();
    }

    @Override
    public void resetServer() {
        Printer printer = new Printer(Arrays.asList(), username.getText(), password.getText(), null, this);
        try {
            printer.clearServerFiles();
        } catch (JSchException e) {
            e.printStackTrace();
            Dialog.serverResetFailed();
        }
    }
}
