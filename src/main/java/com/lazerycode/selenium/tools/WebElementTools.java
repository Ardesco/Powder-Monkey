package com.lazerycode.selenium.tools;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.HashMap;

public class WebElementTools {

  static public HashMap<String, String> getWebElementLocator(WebElement element) {
    HashMap<String, String> locator = new HashMap<String, String>();
    Boolean webElementLocated = true;
    String[] componentParts = null;
    try {
      componentParts = element.toString().split("->");
    } catch (NoSuchElementException e) {
      webElementLocated = false;
      componentParts = e.getLocalizedMessage().split("\\{|\\}")[1].split("\"");
    }
    if (webElementLocated) {
      String part = componentParts[componentParts.length - 1];
      String[] parts = part.substring(0, part.length() - 1).split(":");
      locator.put("By", parts[0]);
      locator.put("Locator", parts[1]);
    } else {
      locator.put("By", componentParts[3]);
      locator.put("Locator", componentParts[7]);
    }

    return locator;
  }

}
