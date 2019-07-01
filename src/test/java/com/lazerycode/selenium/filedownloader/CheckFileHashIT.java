/*
 * Copyright (c) 2010-2012 Lazery Attack - http://www.lazeryattack.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lazerycode.selenium.filedownloader;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import static com.lazerycode.selenium.filedownloader.TypeOfHash.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CheckFileHashIT {

    private final URL testFile = this.getClass().getResource("/download.zip");

    @Test
    public void checkValidMD5Hash() throws Exception {
        CheckFileHash fileToCheck = new CheckFileHash();
        fileToCheck.fileToCheck(new File(testFile.toURI()));
        fileToCheck.hashDetails("def3a66650822363f9e0ae6b9fbdbd6f", MD5);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(true)));
    }

    @Test
    public void checkValidSHA1Hash() throws Exception {
        CheckFileHash fileToCheck = new CheckFileHash();
        fileToCheck.fileToCheck(new File(testFile.toURI()));
        fileToCheck.hashDetails("638213e8a5290cd4d227d57459d92655e8fb1f17", SHA1);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(true)));
    }

    @Test
    public void checkInvalidMD5Hash() throws Exception {
        CheckFileHash fileToCheck = new CheckFileHash();
        fileToCheck.fileToCheck(new File(testFile.toURI()));
        fileToCheck.hashDetails("foo", MD5);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(false)));
    }

    @Test
    public void checkInvalidSHA1Hash() throws Exception {
        CheckFileHash fileToCheck = new CheckFileHash();
        fileToCheck.fileToCheck(new File(testFile.toURI()));
        fileToCheck.hashDetails("bar", SHA1);
        assertThat(fileToCheck.hasAValidHash(), is(equalTo(false)));
    }

    @Test(expected = FileNotFoundException.class)
    public void fileNotSet() throws Exception {
        CheckFileHash fileToCheck = new CheckFileHash();
        fileToCheck.hasAValidHash();
    }

    @Test(expected = NullPointerException.class)
    public void hashDetailsNotSet() throws Exception {
        CheckFileHash fileToCheck = new CheckFileHash();
        fileToCheck.fileToCheck(new File(testFile.toURI()));
        fileToCheck.hasAValidHash();
    }
}
