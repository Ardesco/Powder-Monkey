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

import com.lazerycode.selenium.JettyServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class URLStatusCheckerTest {

    private static JettyServer localWebServer;
    private static int webServerPort = 9081;
    private static String webServerURL = "http://localhost";
    private static URI downloadURI200;
    private static URI downloadURI404;

    @BeforeClass
    public static void start() throws Exception {
        localWebServer = new JettyServer(webServerPort);
        downloadURI200 = new URI(webServerURL + ":" + webServerPort + "/downloadTest.html");
        downloadURI404 = new URI(webServerURL + ":" + webServerPort + "/doesNotExist.html");
    }

    @AfterClass
    public static void stop() throws Exception {
        localWebServer.stopJettyServer();
    }

    @Test
    public void statusCode200FromString() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(webServerURL + ":" + webServerPort + "/downloadTest.html");
        URLChecker.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromString() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(webServerURL + ":" + webServerPort + "/doesNotExist.html");
        URLChecker.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(404)));
    }

    @Test
    public void statusCode200FromURI() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(this.downloadURI200);
        URLChecker.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromURI() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(this.downloadURI404);
        URLChecker.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(404)));
    }

    @Test
    public void statusCode200FromURL() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(this.downloadURI200.toURL());
        URLChecker.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromURL() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(this.downloadURI404.toURL());
        URLChecker.setHTTPRequestMethod(RequestMethod.GET);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(404)));
    }

    @Test
    public void statusCode200FromURLUsingHead() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(this.downloadURI200.toURL());
        URLChecker.setHTTPRequestMethod(RequestMethod.HEAD);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(200)));
    }

    @Test
    public void statusCode404FromURLUsingHead() throws Exception {
        URLStatusChecker URLChecker = new URLStatusChecker(this.downloadURI404.toURL());
        URLChecker.setHTTPRequestMethod(RequestMethod.HEAD);
        assertThat(URLChecker.getHTTPStatusCode(), is(equalTo(404)));
    }
}
