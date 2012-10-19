package com.lazerycode.selenium.charts;

import com.lazerycode.selenium.JettyServer;
import com.lazerycode.selenium.graphs.HighCharts;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

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
    driver = new HtmlUnitDriver();
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
  @Ignore
  public void validateBarChart(){
    //TODO create highcharts.html
    driver.get(webServerURL + ":" + webServerPort + "/highcharts.html");

    WebElement highChartSVGElement = driver.findElement(By.id("chart"));
    HighCharts chartObject = new HighCharts(driver, highChartSVGElement);

    assertThat(chartObject.isChartDisplayed(), is(equalTo(true)));
    assertThat(chartObject.isLegendDisplayed(), is(equalTo(true)));

    chartObject.hoverOverBarChartSeriesAtXAxisPosition(1, "foo");

    assertThat(chartObject.isTooltipDisplayed(), is(equalTo(true)));
    assertThat(chartObject.getToolTipLine(1), is(equalTo("Tooltip Text 1")));
    assertThat(chartObject.getToolTipLine(2), is(equalTo("Tooltip Text 2")));
  }

  @Test
  @Ignore
  public void validateLineChart(){
    //TODO create highcharts.html
    driver.get(webServerURL + ":" + webServerPort + "/highcharts.html");

    WebElement highChartSVGElement = driver.findElement(By.id("chart"));
    HighCharts chartObject = new HighCharts(driver, highChartSVGElement);

    assertThat(chartObject.isChartDisplayed(), is(equalTo(true)));
    assertThat(chartObject.isLegendDisplayed(), is(equalTo(true)));

    chartObject.hoverOverPointAtXAxisPositionForLineChart("bar");

    assertThat(chartObject.isTooltipDisplayed(), is(equalTo(true)));
    assertThat(chartObject.getToolTipLine(1), is(equalTo("Tooltip Text 1")));
    assertThat(chartObject.getToolTipLine(2), is(equalTo("Tooltip Text 2")));
  }
}
