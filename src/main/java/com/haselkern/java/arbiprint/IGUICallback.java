package com.haselkern.java.arbiprint;

import java.io.File;

public interface IGUICallback {

    void setPrintButtonEnabled(boolean enabled);

    void removeFileFromList(File f);

    void updateAvailable();
}
