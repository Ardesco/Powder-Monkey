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

package com.lazerycode.selenium.urlstatuschecker;

import java.io.IOException;
import java.net.*;

public class URLStatusChecker {

    private URL linkToCheck;
    private String httpRequestMethod = "GET";

    public URLStatusChecker(String linkToCheck) throws MalformedURLException, URISyntaxException {
        updateURLToCheck(linkToCheck);
    }

    public URLStatusChecker(URI linkToCheck) throws MalformedURLException, URISyntaxException {
        updateURLToCheck(linkToCheck);
    }

    public URLStatusChecker(URL linkToCheck) throws MalformedURLException, URISyntaxException {
        updateURLToCheck(linkToCheck);
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon
     *
     * @param linkToCheck
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public void updateURLToCheck(String linkToCheck) throws MalformedURLException, URISyntaxException {
        this.linkToCheck = new URI(linkToCheck).toURL();
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon
     *
     * @param linkToCheck
     * @throws MalformedURLException
     */
    public void updateURLToCheck(URI linkToCheck) throws MalformedURLException {
        this.linkToCheck = linkToCheck.toURL();
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon
     *
     * @param linkToCheck
     */
    public void updateURLToCheck(URL linkToCheck) {
        this.linkToCheck = linkToCheck;
    }

    /**
     * Set the HTTP Request Method (Defaults to 'GET')
     *
     * @param requestMethod
     */
    public void setHTTPRequestMethod(RequestMethod requestMethod) {
        this.httpRequestMethod = requestMethod.toString();
    }

    /**
     * Perform an HTTP Status check and return the response code
     *
     * @return
     * @throws IOException
     */
    public int getHTTPStatusCode() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) this.linkToCheck.openConnection();
        connection.setRequestMethod(this.httpRequestMethod);
        connection.connect();

        return connection.getResponseCode();
    }
}
