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

import java.io.IOException;
import java.net.*;

public class LinkStatusChecker {

    private URL linkToCheck;
    private String httpRequestMethod = "GET";

    public LinkStatusChecker(String linkToCheck) throws MalformedURLException, URISyntaxException {
        updateLinkToCheck(linkToCheck);
    }

    public void updateLinkToCheck(String linkToCheck) throws MalformedURLException, URISyntaxException {
        this.linkToCheck = new URI(linkToCheck).toURL();
    }

    /**
     * Set the HTTP Request Method (Defaults to 'GET')
     *
     * @param requestMethod
     */
    public void setHTTPRequestMethod(String requestMethod) {
        //Possible somebody may wish to use POST or PUT or something else.
        this.httpRequestMethod = requestMethod;
    }

    public int getHTTPStatusCode() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) this.linkToCheck.openConnection();
        connection.setRequestMethod(this.httpRequestMethod);
        connection.connect();

        return connection.getResponseCode();
    }
}
