package com.lazerycode.selenium.graphs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LineChart extends HighCharts {

  public LineChart(WebDriver driver, WebElement chart) {
    super(driver, chart);
  }

  public void hoverOverMiddleOfGraphAtXAxisLabel(String xAxisLabelValue) {
    int pointNumber = getXAxisLabelsText().indexOf(xAxisLabelValue);
    hoverOverMiddleOfGraphAtXAxisPosition(pointNumber);
  }

  public void hoverOverMiddleOfGraphAtXAxisPosition(int pointNumber) {
    //Find x position of chart label
    WebElement xAxisLabel = getXAxisLabels().findElements(By.cssSelector("text")).get(pointNumber);
    int xPositionOfLabel = extractXAttributeAsInteger(xAxisLabel);

    //Get left most point of line on graph
    WebElement firstItemInDataSeries = getYAxisLabels().findElement(By.cssSelector("text"));
    int dataSeriesLeftMostPoint = extractXAttributeAsInteger(firstItemInDataSeries);
    int hoverPoint = xPositionOfLabel - dataSeriesLeftMostPoint;

    WebElement elementToHoverOver = chart.findElement(By.cssSelector("g.highcharts-tracker > g > path"));

    //For browsers not supporting native events
    javascript.callEmbeddedSelenium(driver, "triggerEvent", elementToHoverOver, "mouseover");
    //For browsers supporting native events
    performAction.moveToElement(elementToHoverOver).moveByOffset(hoverPoint, 0).perform();
  }
}

