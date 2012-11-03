package com.lazerycode.selenium.charts;

import com.lazerycode.selenium.JettyServer;
import com.lazerycode.selenium.graphs.ColumnChart;
import com.lazerycode.selenium.graphs.LineChart;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.Color;

import java.awt.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class HighChartsTest {

  private static WebDriver driver;
  private static JettyServer localWebServer;
  private static int webServerPort = 9081;
  private String webServerURL = "http://localhost";

  @BeforeClass
  public static void start() throws Exception {
    //Move mouse out of the way so it can't mess about with hover events
    Robot mouseRemover = new Robot();
    mouseRemover.mouseMove(1200, 0);
    localWebServer = new JettyServer(webServerPort);
    driver = new FirefoxDriver();
    driver.manage().window().setSize(new Dimension(1024, 768));
  }

  @AfterClass
  public static void stop() throws Exception {
    localWebServer.stopJettyServer();
    driver.close();
  }

  @Test
  public void validateColumnChart() {
    driver.get(webServerURL + ":" + webServerPort + "/highcharts.html");

    WebElement highChartSVGElement = driver.findElement(By.id("columnchart"));
    ColumnChart chartObject = new ColumnChart(driver, highChartSVGElement);

    assertThat(chartObject.isChartDisplayed(), is(equalTo(true)));
    assertThat(chartObject.isLegendDisplayed(), is(equalTo(true)));

    chartObject.hoverOverPrimarySeriesAtXAxisLabel("Bananas");
    assertThat(chartObject.getPrimarySeriesColourForXAxisLabel("Bananas").asHex(), is(equalTo(Color.fromString("#4572A7").asHex())));

    assertThat(chartObject.isTooltipDisplayed(), is(equalTo(true)));
    assertThat(chartObject.getToolTipLine(1), is(equalTo("Bananas")));
    assertThat(chartObject.getToolTipLine(2), is(equalTo("Jane")));
    assertThat(chartObject.getToolTipLine(3), is(equalTo(":")));
    assertThat(chartObject.getToolTipLine(4), is(equalTo("0")));

    chartObject.hoverOverSecondarySeriesAtXAxisLabel("Bananas");
    assertThat(chartObject.getSecondarySeriesColourForXAxisLabel("Bananas").asHex(), is(equalTo(Color.fromString("#AA4643").asHex())));

    assertThat(chartObject.isTooltipDisplayed(), is(equalTo(true)));
    assertThat(chartObject.getToolTipLine(1), is(equalTo("Bananas")));
    assertThat(chartObject.getToolTipLine(2), is(equalTo("John")));
    assertThat(chartObject.getToolTipLine(3), is(equalTo(":")));
    assertThat(chartObject.getToolTipLine(4), is(equalTo("7")));

    String[] EXPECTED_X_AXIS_LABELS = {"Apples", "Bananas", "Oranges"};
    String[] EXPECTED_Y_AXIS_LABELS = {"0", "2.5", "5", "7.5", "Fruit eaten"};

    assertThat(chartObject.getXAxisLabelsAsArray(), is(equalTo(EXPECTED_X_AXIS_LABELS)));
    assertThat(chartObject.getYAxisLabelsAsArray(), is(equalTo(EXPECTED_Y_AXIS_LABELS)));

  }

  @Test
  public void validateLineChart() {
    driver.get(webServerURL + ":" + webServerPort + "/highcharts.html");

    WebElement highChartSVGElement = driver.findElement(By.id("linechart"));
    LineChart chartObject = new LineChart(driver, highChartSVGElement);

    assertThat(chartObject.isChartDisplayed(), is(equalTo(true)));
    assertThat(chartObject.isLegendDisplayed(), is(equalTo(true)));

    chartObject.hoverOverMiddleOfGraphAtXAxisLabel("Bananas");

    assertThat(chartObject.isTooltipDisplayed(), is(equalTo(true)));
    assertThat(chartObject.getToolTipLine(1), is(equalTo("Bananas")));
    assertThat(chartObject.getToolTipLine(2), is(equalTo("Jane")));
    assertThat(chartObject.getToolTipLine(3), is(equalTo(":")));
    assertThat(chartObject.getToolTipLine(4), is(equalTo("0")));
    assertThat(chartObject.getToolTipLine(5), is(equalTo("John")));
    assertThat(chartObject.getToolTipLine(6), is(equalTo(":")));
    assertThat(chartObject.getToolTipLine(7), is(equalTo("7")));

    String[] EXPECTED_X_AXIS_LABELS = {"Apples", "Bananas", "Oranges"};
    String[] EXPECTED_Y_AXIS_LABELS = {"0", "-2.5", "2.5", "5", "7.5", "Fruit eaten"};

    assertThat(chartObject.getXAxisLabelsAsArray(), is(equalTo(EXPECTED_X_AXIS_LABELS)));
    assertThat(chartObject.getYAxisLabelsAsArray(), is(equalTo(EXPECTED_Y_AXIS_LABELS)));
  }
}
