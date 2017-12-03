package com.haselkern.java.arbiprint;

import org.junit.Test;

public class VersionTest {
    @Test
    public void isNewerVersion() throws Exception {
        assert Version.isNewerVersion("v1.0", "v1.1");
        assert Version.isNewerVersion("1.0", "v1.1");
        assert Version.isNewerVersion("v1.0", "1.1");
        assert Version.isNewerVersion("v0.0.9", "v1.1");
        assert !Version.isNewerVersion("v1.1", "v0.0.9");
        assert !Version.isNewerVersion("v1", "v0.0.9");
        assert !Version.isNewerVersion("v1.10", "v1.9");
        assert !Version.isNewerVersion("v1.10", "v1.9.9");
        assert Version.isNewerVersion("v1.9.9", "v1.10");
        assert Version.isNewerVersion("1.9.9", "1.10");
    }

}