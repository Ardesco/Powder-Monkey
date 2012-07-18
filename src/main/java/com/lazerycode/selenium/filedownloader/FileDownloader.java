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

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.*;
import java.net.URL;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.log4j.Logger;

public class FileDownloader {

    private static final Logger LOG = Logger.getLogger(FileDownloader.class);
    private WebDriver driver;
    private String downloadPath = System.getProperty("java.io.tmpdir");

    public FileDownloader(WebDriver driverObject) {
        this.driver = driverObject;
    }

    /**
     * Get the current location that files will be downloaded to.
     *
     * @return The filepath that the file will be downloaded to.
     */
    public String getDownloadPath() {
        return this.downloadPath;
    }

    /**
     * Set the path that files will be downloaded to.
     *
     * @param filePath The filepath that the file will be downloaded to.
     */
    public void setDownloadPath(String filePath) {
        this.downloadPath = filePath;
    }


    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet
     * @return
     */
    private HttpState mimicCookieState(Set<org.openqa.selenium.Cookie> seleniumCookieSet) {
        HttpState mimicWebDriverCookieState = new HttpState();
        for (org.openqa.selenium.Cookie seleniumCookie : seleniumCookieSet) {
            Cookie httpClientCookie = new Cookie(seleniumCookie.getDomain(), seleniumCookie.getName(), seleniumCookie.getValue(), seleniumCookie.getPath(), seleniumCookie.getExpiry(), seleniumCookie.isSecure());
            mimicWebDriverCookieState.addCookie(httpClientCookie);
        }
        return mimicWebDriverCookieState;
    }

    /**
     * Mimic the WebDriver host configuration
     *
     * @param hostURL
     * @return
     */
    private HostConfiguration mimicHostConfiguration(String hostURL, int hostPort) {
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(hostURL, hostPort);
        return hostConfig;
    }

    public String fileDownloader(WebElement element) throws Exception {
        return downloader(element, "href");
    }

    public String imageDownloader(WebElement element) throws Exception {
        return downloader(element, "src");
    }

    public String downloader(WebElement element, String attribute) throws Exception {
        //Assuming that getAttribute does some magic to return a fully qualified URL
        String downloadLocation = element.getAttribute(attribute);
        if (downloadLocation.trim().equals("")) {
            throw new Exception("The element you have specified does not link to anything!");
        }
        URL downloadURL = new URL(downloadLocation);
        HttpClient client = new HttpClient();
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
        client.setHostConfiguration(mimicHostConfiguration(downloadURL.getHost(), downloadURL.getPort()));
        client.setState(mimicCookieState(driver.manage().getCookies()));
        HttpMethod getRequest = new GetMethod(downloadURL.getPath());
        File fileToDownload = new File(downloadPath + downloadURL.getFile().replaceFirst("/|\\\\", ""));
        if (fileToDownload.canWrite() == false) {
            fileToDownload.setWritable(true);
        }
        OutputStream foo = new FileOutputStream(fileToDownload);
        try {
            int status = client.executeMethod(getRequest);
            LOG.info("HTTP Status " + status + " when getting '" + downloadURL.toExternalForm() + "'");
            BufferedInputStream in = new BufferedInputStream(getRequest.getResponseBodyAsStream());
            int offset = 0;
            int len = 4096;
            int bytes = 0;
            byte[] block = new byte[len];
            while ((bytes = in.read(block, offset, len)) > -1) {
                foo.write(block, 0, bytes);
            }
            foo.close();
            in.close();
            LOG.info("File downloaded to '" + fileToDownload.getAbsolutePath() + "'");
        } catch (Exception Ex) {
            LOG.error("Download failed: {}", Ex);
            throw new Exception("Download failed!");
        } finally {
            getRequest.releaseConnection();
        }
        return fileToDownload.getAbsolutePath();
    }

}
