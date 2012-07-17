package com.lazerycode.selenium.filedownloader;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import static com.lazerycode.selenium.filedownloader.HashCheckFile.hashType.MD5;
import static com.lazerycode.selenium.filedownloader.HashCheckFile.hashType.SHA1;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class HashCheckFileTest {

    private final URL testFile = this.getClass().getResource("/download.zip");

    @Test
    public void checkValidMD5Hash() throws Exception{
        HashCheckFile fileToCheck = new HashCheckFile(new File(testFile.toURI()), "def3a66650822363f9e0ae6b9fbdbd6f", MD5);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(true)));
    }

    @Test
    public void checkValidSHA1Hash() throws Exception{
        HashCheckFile fileToCheck = new HashCheckFile(new File(testFile.toURI()), "638213e8a5290cd4d227d57459d92655e8fb1f17", SHA1);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(true)));
    }

    @Test
    public void checkInvalidMD5Hash() throws Exception{
        HashCheckFile fileToCheck = new HashCheckFile(new File(testFile.toURI()), "foo", MD5);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(false)));
    }

    @Test
    public void checkInvalidSHA1Hash() throws Exception{
        HashCheckFile fileToCheck = new HashCheckFile(new File(testFile.toURI()), "bar", SHA1);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(false)));
    }
}
