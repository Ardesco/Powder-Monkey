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

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

public class FileDownloader {

    private static final Logger LOG = Logger.getLogger(FileDownloader.class);
    private WebDriver driver;
    private String localDownloadPath = System.getProperty("java.io.tmpdir");
    private boolean followRedirects = true;
    private boolean mimicWebDriverCookieState = true;
    private int httpStatusOfLastDownloadAttempt = 0;
    private static BasicCookieStore mimicWebDriverCookieStore = new BasicCookieStore();
    private boolean doVerifyDomain = false;
    private String nameOfCookieToVerify = "";
    private String cookieDomainToVerify = "";

    public FileDownloader(WebDriver driverObject) {
        this.driver = driverObject;
    }

    /**
     * Specify if the FileDownloader class should follow redirects when trying to download a file
     *
     * @param value
     */
    public void followRedirectsWhenDownloading(boolean value) {
        this.followRedirects = value;
    }

    /**
     * Get the current location that files will be downloaded to.
     *
     * @return The filepath that the file will be downloaded to.
     */
    public String localDownloadPath() {
        return this.localDownloadPath;
    }

    /**
     * Set the path that files will be downloaded to.
     *
     * @param filePath The filepath that the file will be downloaded to.
     */
    public void localDownloadPath(String filePath) {
        this.localDownloadPath = filePath;
    }
    
    /**
     * Set values necessary to trigger a cookie domain verification.
     *
     * @param cookieName Name of the cookie to check.
     * @param domainValueToVerify Expected domain for checked cookie.
     */
    public void verifyCookieDomainWhenDownloading(String cookieName, String domainValueToVerify) {
    	this.doVerifyDomain = true;
    	this.nameOfCookieToVerify = cookieName;
    	this.cookieDomainToVerify = domainValueToVerify;
    }

    /**
     * Download the file specified in the href attribute of a WebElement
     *
     * @param element
     * @return
     * @throws Exception
     */
    public String downloadFile(WebElement element) throws Exception {
        return downloader(element, "href");
    }

    /**
     * Download the image specified in the src attribute of a WebElement
     *
     * @param element
     * @return
     * @throws Exception
     */
    public String downloadImage(WebElement element) throws Exception {
        return downloader(element, "src");
    }

    /**
     * Gets the HTTP status code of the last download file attempt
     *
     * @return
     */
    public int getHTTPStatusOfLastDownloadAttempt() {
        return this.httpStatusOfLastDownloadAttempt;
    }

    /**
     * Mimic the cookie state of WebDriver (Defaults to true)
     * This will enable you to access files that are only available when logged in.
     * If set to false the connection will be made as an anonymouse user
     *
     * @param value
     */
    public void mimicWebDriverCookieState(boolean value) {
        this.mimicWebDriverCookieState = value;
    }

    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet
     * @return
     */
    private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
        for (Cookie seleniumCookie : seleniumCookieSet) {
            BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
            String dupName = duplicateCookie.getName();
            if ((doVerifyDomain) && (nameOfCookieToVerify.equals(dupName))){
            	LOG.info("Verifying and setting domain " + cookieDomainToVerify + " for cookie " + nameOfCookieToVerify);
            	if (duplicateCookie.getDomain() != cookieDomainToVerify ) {
            		duplicateCookie.setDomain(cookieDomainToVerify);
            	}
            } else {
            	duplicateCookie.setDomain(seleniumCookie.getDomain());
            }
            duplicateCookie.setSecure(seleniumCookie.isSecure());
            duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
            duplicateCookie.setPath(seleniumCookie.getPath());
            mimicWebDriverCookieStore.addCookie(duplicateCookie);
        }

        return mimicWebDriverCookieStore;
    }

    /**
     * Perform the file/image download.
     *
     * @param element
     * @param attribute
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    private String downloader(WebElement element, String attribute) throws IOException, NullPointerException, URISyntaxException {
        String fileToDownloadLocation = element.getAttribute(attribute);
        if (fileToDownloadLocation.trim().equals("")) throw new NullPointerException("The element you have specified does not link to anything!");

        URL fileToDownload = new URL(fileToDownloadLocation);
        File downloadedFile = new File(this.localDownloadPath + fileToDownload.getFile().replaceFirst("/|\\\\", ""));
        if (downloadedFile.canWrite() == false) downloadedFile.setWritable(true);

        HttpClient client = new DefaultHttpClient();
        BasicHttpContext localContext = new BasicHttpContext();

        LOG.info("Mimic WebDriver cookie state: " + this.mimicWebDriverCookieState);
        if (this.mimicWebDriverCookieState) {
            localContext.setAttribute(ClientContext.COOKIE_STORE, mimicCookieState(this.driver.manage().getCookies()));
        }

        //TODO provide the ability to do different HTTP Requests?
        HttpGet httpget = new HttpGet(fileToDownload.toURI());
        HttpParams httpRequestParameters = httpget.getParams();
        httpRequestParameters.setParameter(ClientPNames.HANDLE_REDIRECTS, this.followRedirects);
        httpget.setParams(httpRequestParameters);

        LOG.info("Sending GET request for: " + httpget.getURI());
        HttpResponse response = client.execute(httpget, localContext);
        this.httpStatusOfLastDownloadAttempt = response.getStatusLine().getStatusCode();
        LOG.info("HTTP GET request status: " + this.httpStatusOfLastDownloadAttempt);
        LOG.info("Downloading file: " + downloadedFile.getName());
        FileUtils.copyInputStreamToFile(response.getEntity().getContent(), downloadedFile);
        response.getEntity().getContent().close();

        String downloadedFileAbsolutePath = downloadedFile.getAbsolutePath();
        LOG.info("File downloaded to '" + downloadedFileAbsolutePath + "'");

        return downloadedFileAbsolutePath;
    }

}
