package com.haselkern.java.arbiprint;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Provides methods for handling the update process.
 * <p>
 * When the update process is started, the following happens:
 * - download the newet JAR to a temporary location (say: temp.jar)
 * - Get the path to the current jar (say: current.jar)
 * - Launch _java -jar temp.jar --path current.jar_
 * - exit
 * The temp.jar can now override current.jar, as it's no longer in use.
 * After that, temp.jar will launch (the now updated) current.jar.
 */
public class Updater {

    // Prevent instancing
    private Updater(){}

    /**
     * @return A stage, that shows an indefinite loading bar.
     */
    public static Stage getLoadingStage() {

        // Create progressbar
        ProgressBar pb = new ProgressBar();
        pb.setPrefWidth(400);
        pb.setPrefHeight(50);

        // Create label
        Label l = new Label("aktualisiere...");

        // Stack label on progressbar
        StackPane pane = new StackPane(pb, l);

        // Set scene
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);

        // Set icon
        stage.getIcons().add(new Image("/ic_print.png"));

        // Setup stage
        stage.setTitle("ARBIprint " + Version.getVersionString());
        stage.setResizable(false);

        return stage;
    }

    /**
     * Can we run the update process?
     * @return true, if we can somehow access java, so that we can autoUpdate
     */
    public static boolean canAutoUpdate() {
        return javaExe() != null;
    }

    /**
     * Returns the path to java, or tries to run "java".
     * https://stackoverflow.com/a/46852384/1456971
     * @return The full path to the java executable, or just "java" if java is in the system path.
     */
    private static String javaExe() {
        final String javaHome = System.getProperty("java.home");
        final File javaBin = new File(javaHome, "bin");
        File javaExe = new File(javaBin, "java");

        if (!javaExe.exists()) {
            // We might be on Windows, which needs an javaExe extension
            javaExe = new File(javaBin, "java.javaExe");
        }

        if (javaExe.exists()) {
            return javaExe.getAbsolutePath();
        }

        try {
            // Just try invoking java from the system path; this of course
            // assumes "java[.javaExe]" is /actually/ Java
            final String NAKED_JAVA = "java";
            new ProcessBuilder(NAKED_JAVA).start();

            return NAKED_JAVA;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Launches the update process
     *
     * @param primaryStage The primaryStage, that can be hidden.
     */
    public static void update(Stage primaryStage) {

        InputStream updaterJarInputStream = null;
        try {
            // If autoupdate doesn't work, the user has to manually download
            if (!Updater.canAutoUpdate()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(Path.RELEASE_WEBSITE));
                }
                // If we cannot open the website, the user has to open it manually
                else {
                    Dialog.updaterFailed();
                }
                return;
            }

            Platform.runLater(() -> {
                // Hide primaryStage
                primaryStage.hide();
                // Show loading window
                getLoadingStage().show();
            });

            // Download updater
            System.out.println("Downloading updater...");
            String updaterJar = Path.getTemporaryJarPath();

            URL updaterURL = new URL(Path.getNewestJarURL());
            updaterJarInputStream = updaterURL.openStream();
            Files.copy(updaterJarInputStream, Paths.get(updaterJar), StandardCopyOption.REPLACE_EXISTING);

            // Get path to currently running jar
            File jar = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile();

            // For Debugging purposes, if the path is not a jar, modify it, so that updating works
            if (!jar.isFile()) {
                jar = new File(jar, "arbiprint.jar");
            }

            // Run updater
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", updaterJar, "--path=" + jar.getAbsolutePath());
            pb.start();

            // Exit
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            Dialog.updaterFailed();
        }
        finally {
            if(updaterJarInputStream != null){
                try {
                    updaterJarInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
