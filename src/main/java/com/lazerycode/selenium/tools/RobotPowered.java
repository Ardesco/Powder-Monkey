package com.lazerycode.selenium.tools;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Locatable;

import java.awt.*;
import java.awt.event.InputEvent;

public class RobotPowered {

  private final Robot mouseObject;
  private final WebDriver driver;
  private final JavascriptExecutor executor;

  public RobotPowered(WebDriver driver) throws AWTException {
    this.mouseObject = new Robot();
    this.driver = driver;
    this.executor = (JavascriptExecutor) driver;
  }

  public void robotPoweredMoveMouseToWebElementCoordinates(WebElement element) {
    //Get Browser dimensions
    int browserWidth = driver.manage().window().getSize().width;
    int browserHeight = driver.manage().window().getSize().height;

    //Get dimensions of the window displaying the web page
    int pageWidth = Integer.parseInt(executor.executeScript("return document.documentElement.clientWidth").toString());
    int pageHeight = Integer.parseInt(executor.executeScript("return document.documentElement.clientHeight").toString());

    //Calculate the space the browser is using for toolbars
    int browserFurnitureOffsetX = browserWidth - pageWidth;
    int browserFurnitureOffsetY = browserHeight - pageHeight;

    //Get the coordinates of the WebElement on the page and calculate the centre point
    int webElementX = ((Locatable) element).getCoordinates().inViewPort().x + Math.round(element.getSize().width / 2);
    int webElementY = ((Locatable) element).getCoordinates().inViewPort().y + Math.round(element.getSize().height / 2);

    //Calculate the correct X/Y coordinates based upon the browser furniture offset and the position of the browser on the desktop
    int xPosition = driver.manage().window().getPosition().x + browserFurnitureOffsetX + webElementX;
    int yPosition = driver.manage().window().getPosition().y + browserFurnitureOffsetY + webElementY;

    //Move the mouse to the calculated X/Y coordinates
    mouseObject.mouseMove(xPosition, yPosition);
    mouseObject.waitForIdle();
  }

  public void robotPoweredMoveMouseToCoordinatesOnPage(int xCoordinates, int yCoordinates) {
    //Get Browser dimensions
    int browserWidth = driver.manage().window().getSize().width;
    int browserHeight = driver.manage().window().getSize().height;

    //Get dimensions of the window displaying the web page
    int pageWidth = Integer.parseInt(executor.executeScript("return document.documentElement.clientWidth").toString());
    int pageHeight = Integer.parseInt(executor.executeScript("return document.documentElement.clientHeight").toString());

    //Calculate the space the browser is using for toolbars
    int browserFurnitureOffsetX = browserWidth - pageWidth;
    int browserFurnitureOffsetY = browserHeight - pageHeight;

    //Calculate the correct X/Y coordinates based upon the browser furniture offset and the position of the browser on the desktop
    int xPosition = driver.manage().window().getPosition().x + browserFurnitureOffsetX + xCoordinates;
    int yPosition = driver.manage().window().getPosition().y + browserFurnitureOffsetY + yCoordinates;

    //Move the mouse to the calculated X/Y coordinates
    mouseObject.mouseMove(xPosition, yPosition);
    mouseObject.waitForIdle();
  }

  public void robotPoweredMoveMouseToAbsoluteCoordinates(int xCoordinates, int yCoordinates) {
    mouseObject.mouseMove(xCoordinates, yCoordinates);
    mouseObject.waitForIdle();
  }

  public void robotPoweredMouseDown() {
    mouseObject.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    mouseObject.waitForIdle();
  }

  public void robotPoweredMouseUp() {
    mouseObject.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    mouseObject.waitForIdle();
  }

  public void robotPoweredClick() {
    mouseObject.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    mouseObject.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    mouseObject.waitForIdle();
  }
}
