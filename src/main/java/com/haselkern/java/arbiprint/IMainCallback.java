package com.haselkern.java.arbiprint;

import java.io.File;

public interface IMainCallback {

    void setPrintButtonEnabled(boolean enabled);

    void removeFileFromList(File f);

    void updateAvailable();

    void clearFields();

    void resetServer();
}
