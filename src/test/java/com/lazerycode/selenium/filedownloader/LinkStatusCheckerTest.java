package com.lazerycode.selenium.filedownloader;

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

import com.lazerycode.selenium.JettyServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class LinkStatusCheckerTest {

    private static JettyServer localWebServer;
    private static int webServerPort = 9081;
    private String webServerURL = "http://localhost";

    @BeforeClass
    public static void start() throws Exception {
        localWebServer = new JettyServer(webServerPort);
    }

    @AfterClass
    public static void stop() throws Exception {
        localWebServer.stopJettyServer();
    }

    @Test
    public void statusCode200() throws Exception {
        LinkStatusChecker linkChecker = new LinkStatusChecker(webServerURL + ":" + webServerPort + "/downloadTest.html");
        assertThat(linkChecker.getHTTPStatusCode(), is(equalTo(200)));
    }

    @Test
    public void statusCode404() throws Exception {
        LinkStatusChecker linkChecker = new LinkStatusChecker(webServerURL + ":" + webServerPort + "/doesNotExist.html");
        assertThat(linkChecker.getHTTPStatusCode(), is(equalTo(404)));
    }
}
