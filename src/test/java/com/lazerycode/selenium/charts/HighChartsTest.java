package com.lazerycode.selenium.charts;

import com.lazerycode.selenium.JettyServer;
import com.lazerycode.selenium.graphs.HighCharts;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

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
    localWebServer = new JettyServer(webServerPort);
    driver = new FirefoxDriver();
  }

  @AfterClass
  public static void stop() throws Exception {
    localWebServer.stopJettyServer();
  }

  @After
  public void closeWebDriver() {
    driver.close();
  }

  @Test
  public void validateColumnChart(){
    driver.get(webServerURL + ":" + webServerPort + "/highcharts.html");

    WebElement highChartSVGElement = driver.findElement(By.id("columnchart"));
    HighCharts chartObject = new HighCharts(driver, highChartSVGElement);

    assertThat(chartObject.isChartDisplayed(), is(equalTo(true)));
    assertThat(chartObject.isLegendDisplayed(), is(equalTo(true)));

    chartObject.hoverOverColumnChartSeriesAtXAxisPosition(1, "Bananas");

    assertThat(chartObject.isTooltipDisplayed(), is(equalTo(true)));
    assertThat(chartObject.getToolTipLine(1), is(equalTo("Bananas")));
    assertThat(chartObject.getToolTipLine(2), is(equalTo("Jane")));
    assertThat(chartObject.getToolTipLine(3), is(equalTo(":")));
    assertThat(chartObject.getToolTipLine(4), is(equalTo("0")));

    chartObject.hoverOverColumnChartSeriesAtXAxisPosition(2, "Bananas");

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
  public void validateLineChart(){
    driver.get(webServerURL + ":" + webServerPort + "/highcharts.html");

    WebElement highChartSVGElement = driver.findElement(By.id("linechart"));
    HighCharts chartObject = new HighCharts(driver, highChartSVGElement);

    assertThat(chartObject.isChartDisplayed(), is(equalTo(true)));
    assertThat(chartObject.isLegendDisplayed(), is(equalTo(true)));

    chartObject.hoverOverPointAtXAxisPositionForLineChart("Bananas");

    assertThat(chartObject.isTooltipDisplayed(), is(equalTo(true)));
    assertThat(chartObject.getToolTipLine(1), is(equalTo("Bananas")));
    assertThat(chartObject.getToolTipLine(2), is(equalTo("Jane")));
    assertThat(chartObject.getToolTipLine(3), is(equalTo(":")));
    assertThat(chartObject.getToolTipLine(4), is(equalTo("0")));
    assertThat(chartObject.getToolTipLine(5), is(equalTo("John")));
    assertThat(chartObject.getToolTipLine(6), is(equalTo(":")));
    assertThat(chartObject.getToolTipLine(7), is(equalTo("7")));

    String[] EXPECTED_X_AXIS_LABELS = {"Apples", "Bananas", "Oranges"};
    String[] EXPECTED_Y_AXIS_LABELS = {"-2.5", "0", "2.5", "5", "7.5", "Fruit eaten"};

    assertThat(chartObject.getXAxisLabelsAsArray(), is(equalTo(EXPECTED_X_AXIS_LABELS)));
    assertThat(chartObject.getYAxisLabelsAsArray(), is(equalTo(EXPECTED_Y_AXIS_LABELS)));
  }
}
