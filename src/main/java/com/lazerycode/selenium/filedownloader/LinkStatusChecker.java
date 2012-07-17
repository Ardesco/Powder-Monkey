package com.lazerycode.selenium.filedownloader;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class LinkStatusChecker {

    private URL linkToCheck;

    public LinkStatusChecker(String linkToCheck) throws MalformedURLException, URISyntaxException {
        updateLinkToCheck(linkToCheck);
    }

    public void updateLinkToCheck(String linkToCheck) throws MalformedURLException, URISyntaxException {
        this.linkToCheck = new URI(linkToCheck).toURL();
    }

    public int getHTTPStatusCode() {
        //TODO connect to server and get HTTP status code
        return 200;
    }
}
