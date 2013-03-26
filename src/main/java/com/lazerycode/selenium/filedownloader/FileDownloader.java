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
import org.apache.http.client.methods.HttpRequestBase;
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

public class FileDownloader {

  private static final Logger LOG = Logger.getLogger(FileDownloader.class);
  private WebDriver driver;
  private boolean followRedirects = true;
  private boolean mimicWebDriverCookieState = true;
  private RequestMethod httpRequestMethod = RequestMethod.GET;
  private URI fileURI;

  public FileDownloader(WebDriver driverObject) {
    this.driver = driverObject;
  }

  /**
   * Specify if the FileDownloader class should follow redirects when trying to download a file
   * Default: true
   *
   * @param followRedirects boolean
   */
  public void followRedirectsWhenDownloading(boolean followRedirects) {
    this.followRedirects = followRedirects;
  }

  /**
   * Mimic the cookie state of WebDriver (Defaults to true)
   * This will enable you to access files that are only available when logged in.
   * If set to false the connection will be made as an anonymouse user
   *
   * @param mimicWebDriverCookies boolean
   */
  public void mimicWebDriverCookieState(boolean mimicWebDriverCookies) {
    mimicWebDriverCookieState = mimicWebDriverCookies;
  }

  /**
   * Set the HTTP Request Method
   * Default: GET
   *
   * @param requestType RequestMethod
   */
  public void setHTTPRequestMethod(RequestMethod requestType) {
    httpRequestMethod = requestType;
  }

  /**
   * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
   *
   * @param linkToFile String
   * @throws MalformedURLException
   * @throws URISyntaxException
   */
  public void setURI(String linkToFile) throws MalformedURLException, URISyntaxException {
    fileURI = new URI(linkToFile);
  }

  /**
   * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
   *
   * @param linkToFile URI
   * @throws MalformedURLException
   */
  public void setURI(URI linkToFile) throws MalformedURLException {
    fileURI = linkToFile;
  }

  /**
   * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
   *
   * @param linkToFile URL
   */
  public void setURI(URL linkToFile) throws URISyntaxException {
    fileURI = linkToFile.toURI();
  }

  /**
   * Perform an HTTP Status Check upon/Download the file specified in the href attribute of a WebElement
   *
   * @param anchorElement Selenium WebElement
   * @throws Exception
   */
  public void setURISpecifiedInAnchorElement(WebElement anchorElement) throws Exception {
    if (anchorElement.getTagName().equals("a")) {
      fileURI = new URI(anchorElement.getAttribute("href"));
    } else {
      throw new Exception("You have not specified an <a> element!");
    }
  }

  /**
   * Perform an HTTP Status Check upon/Download the image specified in the src attribute of a WebElement
   *
   * @param imageElement Selenium WebElement
   * @throws Exception
   */
  public void setURISpecifiedInImageElement(WebElement imageElement) throws Exception {
    if (imageElement.getTagName().equals("img")) {
      fileURI = new URI(imageElement.getAttribute("src"));
    } else {
      throw new Exception("You have not specified an <img> element!");
    }
  }

  /**
   * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
   *
   * @param seleniumCookieSet Set&lt;Cookie&gt;
   */
  private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
    BasicCookieStore copyOfWebDriverCookieStore = new BasicCookieStore();
    for (Cookie seleniumCookie : seleniumCookieSet) {
      BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
      duplicateCookie.setDomain(seleniumCookie.getDomain());
      duplicateCookie.setSecure(seleniumCookie.isSecure());
      duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
      duplicateCookie.setPath(seleniumCookie.getPath());
      copyOfWebDriverCookieStore.addCookie(duplicateCookie);
    }

    return copyOfWebDriverCookieStore;
  }

  private HttpResponse getHTTPResponse() throws IOException, NullPointerException {
    if (fileURI == null) throw new NullPointerException("No file URI specified");

    HttpClient client = new DefaultHttpClient();
    BasicHttpContext localContext = new BasicHttpContext();

    //Clear down the local cookie store every time to make sure we don't have any left over cookies influencing the test
    localContext.setAttribute(ClientContext.COOKIE_STORE, null);

    LOG.info("Mimic WebDriver cookie state: " + mimicWebDriverCookieState);
    if (mimicWebDriverCookieState) {
      localContext.setAttribute(ClientContext.COOKIE_STORE, mimicCookieState(driver.manage().getCookies()));
    }

    HttpRequestBase requestMethod = httpRequestMethod.getRequestMethod();
    requestMethod.setURI(fileURI);
    HttpParams httpRequestParameters = requestMethod.getParams();
    httpRequestParameters.setParameter(ClientPNames.HANDLE_REDIRECTS, followRedirects);
    requestMethod.setParams(httpRequestParameters);
    //TODO if post send map of variables, also need to add a post map setter

    LOG.info("Sending " + httpRequestMethod.toString() + " request for: " + fileURI);
    return client.execute(requestMethod, localContext);
  }


  /**
   * Gets the HTTP status code returned when trying to access the specified URI
   *
   * @return File
   * @throws Exception
   */
  public int getLinkHTTPStatus() throws Exception {
    HttpResponse fileToDownload = getHTTPResponse();
    int httpStatusCode;
    try {
      httpStatusCode = fileToDownload.getStatusLine().getStatusCode();
    } finally {
      fileToDownload.getEntity().getContent().close();
    }

    return httpStatusCode;
  }

  /**
   * Download a file from the specified URI
   *
   * @return File
   * @throws Exception
   */
  public File downloadFile() throws Exception {
    File downloadedFile = File.createTempFile("download", ".tmp");
    HttpResponse fileToDownload = getHTTPResponse();
    try {
      FileUtils.copyInputStreamToFile(fileToDownload.getEntity().getContent(), downloadedFile);
    } finally {
      fileToDownload.getEntity().getContent().close();
    }

    return downloadedFile;
  }
}
