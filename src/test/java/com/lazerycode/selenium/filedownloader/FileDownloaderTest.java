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

import com.lazerycode.selenium.JettyServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FileDownloaderTest {

    private static WebDriver driver;
    private static JettyServer localWebServer;
    private static int webServerPort = 9081;
    private String webServerURL = "http://localhost";

    @BeforeClass
    public static void start() throws Exception {
        localWebServer = new JettyServer(webServerPort);
        driver = new HtmlUnitDriver();
    }

    @AfterClass
    public static void stop() throws Exception {
        localWebServer.stopJettyServer();
    }

    @After
    public void closeWebDriver() {
        driver.close();
    }

    @Test
    public void downloadAFile() throws Exception {
        FileDownloader downloadTestFile = new FileDownloader(driver);
        driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
        WebElement downloadLink = driver.findElement(By.id("fileToDownload"));
        String downloadedFileAbsoluteLocation = downloadTestFile.downloadFile(downloadLink);

        assertThat(new File(downloadedFileAbsoluteLocation).exists(), is(equalTo(true)));
    }

    @Test
    public void downloadAnImage() throws Exception {
        FileDownloader downloadTestFile = new FileDownloader(driver);
        driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
        WebElement image = driver.findElement(By.id("ebselenImage"));
        String downloadedImageAbsoluteLocation = downloadTestFile.downloadImage(image);

        assertThat(new File(downloadedImageAbsoluteLocation).exists(), is(equalTo(true)));
    }
}
