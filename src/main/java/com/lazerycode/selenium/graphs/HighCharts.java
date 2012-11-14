package com.lazerycode.selenium.graphs;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.seleniumemulation.JavascriptLibrary;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

abstract class HighCharts {

  private int waitTimeoutInSeconds = 15;
  protected WebDriver driver;
  protected WebElement chart;
  protected WebDriverWait wait;
  protected Actions performAction;
  protected JavascriptLibrary javascript = new JavascriptLibrary();

  @FindBy(how = How.CSS, using = "g.highcharts-tooltip")
  private WebElement toolTip;
  @FindBy(how = How.CSS, using = "g.highcharts-legend")
  private WebElement legend;
  @FindBy(how = How.CSS, using = "g.highcharts-axis")
  private List<WebElement> axisLabels;

  public HighCharts(WebDriver driver, WebElement chart) {
    PageFactory.initElements(new DefaultElementLocatorFactory(chart), this);
    this.driver = driver;
    this.chart = chart;

    wait = new WebDriverWait(driver, waitTimeoutInSeconds, 100);
    performAction = new Actions(driver);
  }

  public boolean isChartDisplayed() {
    return wait.until(visibilityOf(this.chart)) != null;
  }

  public boolean isLegendDisplayed() {
    return legend.isDisplayed();
  }

  public boolean isTooltipDisplayed() {
    return wait.until(visibilityOf(toolTip)) != null;
  }

  public String getToolTipLine(int lineNo) throws NoSuchElementException {
    List<String> lines = new ArrayList<String>();
    List<WebElement> toolTipLines = toolTip.findElements(By.cssSelector("text tspan"));
    for (WebElement toolTipLine : toolTipLines) {
      lines.add(toolTipLine.getText());
    }
    if (lineNo > lines.size()) {
      throw new NoSuchElementException("There is no line " + lineNo + "! There are only " + lines.size() + " lines in the tool tip");
    }
    //We return line - 1 because the lines Array starts a 0 not 1
    return lines.get(lineNo - 1);
  }

  protected WebElement getXAxisLabels() {
    return axisLabels.get(0);
  }

  public List<String> getXAxisLabelsText() {
    List<String> labels = new ArrayList<String>();
    List<WebElement> xAxisLabels = getXAxisLabels().findElements(By.cssSelector("text"));
    for (WebElement xAxisLabel : xAxisLabels) {
      labels.add(xAxisLabel.getText());
    }
    return labels;
  }

  public String[] getXAxisLabelsAsArray() {
    List<String> xAxisLabels = getXAxisLabelsText();
    return xAxisLabels.toArray(new String[xAxisLabels.size()]);
  }

  protected int extractXAttributeAsInteger(WebElement xAxisLabel) {
    Double xAttribute = Double.parseDouble(xAxisLabel.getAttribute("x"));
    return xAttribute.intValue();
  }

  protected Color getSeriesColorAtXAxisPosition(int series, String xAxisLabelValue) {
    //The series can vary depending on the structure of the chart, by default it is fine but if this doesn't work you may need to tweak the series
    int barNumber = getXAxisLabelsText().indexOf(xAxisLabelValue);
    //The below varies depending on the structure of the chart, by default we need to multiply by 4
    barNumber = barNumber * 4;
    return Color.fromString(chart.findElement(By.cssSelector(".highcharts-series-group > g:nth-of-type(" + series + ") > rect:nth-of-type(" + barNumber + ")")).getAttribute("fill"));
  }

  protected WebElement getYAxisLabels() {
    return axisLabels.get(1);
  }

  public List<String> getYAxisLabelsText() {
    List<String> labels = new ArrayList<String>();
    List<WebElement> yAxisLabels = getYAxisLabels().findElements(By.cssSelector("text"));
    for (WebElement yAxisLabel : yAxisLabels) {
      labels.add(yAxisLabel.getText());
    }
    return labels;
  }

  public String[] getYAxisLabelsAsArray() {
    List<String> yAxisLabels = getYAxisLabelsText();
    return yAxisLabels.toArray(new String[yAxisLabels.size()]);
  }

  protected void hoverOverColumnOrBarChartSeriesAtXAxisPosition(int series, String xAxisLabel) {
    int barNumber = getXAxisLabelsText().indexOf(xAxisLabel);
    WebElement pointToHoverOver = chart.findElements(By.cssSelector("g.highcharts-tracker > g:nth-of-type(" + series + ") > rect")).get(barNumber);

    //For browsers not supporting native events
    javascript.callEmbeddedSelenium(driver, "triggerEvent", pointToHoverOver, "mouseover");
    //For browsers supporting native events
    performAction.moveToElement(pointToHoverOver).perform();
  }

  private static ExpectedCondition<Boolean> attributeIsEqualTo(final WebElement element, final String attribute, final String attributeValue) {
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        return element.getAttribute(attribute).equals(attributeValue);
      }
    };
  }
}
