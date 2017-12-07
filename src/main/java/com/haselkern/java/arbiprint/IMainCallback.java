package com.haselkern.java.arbiprint;

import java.io.File;

/**
 * Several callback functions for the main window.
 */
public interface IMainCallback {

    /**
     * Set the printButton to be enabled or disabled
     */
    void setPrintButtonEnabled(boolean enabled);

    /**
     * Removes the given file from the list
     * @param f The file to remove from the list
     */
    void removeFileFromList(File f);

    /**
     * Notifies the main window, that an update is available
     */
    void updateAvailable();

    /**
     * Clears all the input fields
     */
    void clearFields();

    /**
     * Starts the server reset.
     */
    void resetServer();
}
