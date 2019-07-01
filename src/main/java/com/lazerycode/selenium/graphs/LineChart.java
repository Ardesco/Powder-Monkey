package com.lazerycode.selenium.graphs;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.math.BigDecimal;
import java.util.HashMap;

public class LineChart extends HighCharts {

    @FindBy(how = How.CSS, using = "g.highcharts-series-group > g:nth-child(1)")
    WebElement plotContainer;
    @FindBy(how = How.CSS, using = "g.highcharts-series-group > g:nth-child(1) > path")
    WebElement plotLine;
    @FindBy(how = How.CSS, using = "g.highcharts-tracker > g > path")
    WebElement elementToHoverOver;
    @FindBy(how = How.CSS, using = "rect:nth-child(2)")
    WebElement rectElement;

    public LineChart(WebDriver driver, WebElement chart) {
        super(driver, chart);
    }

    public void hoverOverPointOfGraphAtXAxisLabel(String xAxisLabelValue) {
        int pointNumber = getXAxisLabelsText().indexOf(xAxisLabelValue);
        hoverOverGraphPointAtXAxisPosition(pointNumber);
    }

    public void hoverOverGraphPointAtXAxisPosition(int pointNumber) {
        int xRect = ((Locatable) rectElement).getCoordinates().inViewPort().getX();
        int yRect = ((Locatable) rectElement).getCoordinates().inViewPort().getY();

        int xHoverPoint = xRect + getPlotOffset().getX() + getPlotPoint(pointNumber).getX();
        int yHoverPoint = yRect + getPlotOffset().getY() + getPlotPoint(pointNumber).getY();

        //For browsers not supporting native events
        javascript.callEmbeddedSelenium(driver, "triggerEvent", elementToHoverOver, "mouseover");
        //For browsers supporting native events
        xHoverPoint = xHoverPoint - ((Locatable) elementToHoverOver).getCoordinates().inViewPort().getX();
        yHoverPoint = yHoverPoint - ((Locatable) elementToHoverOver).getCoordinates().inViewPort().getY();
        performAction.moveToElement(plotLine).moveToElement(elementToHoverOver, xHoverPoint, yHoverPoint).perform();
    }

    private PlotPoint getPlotPoint(int point) throws ElementNotVisibleException {
        if (point < 0) {
            throw new ElementNotVisibleException("Plot point ${point} not found");
        }
        return getPlotPoints().get(point);
    }

    private HashMap<Integer, PlotPoint> getPlotPoints() {
        HashMap<Integer, PlotPoint> plotPoints = new HashMap<Integer, PlotPoint>();
        String[] plotPointsArray = plotLine.getAttribute("d").replaceAll("M", "").split("L|C");
        for (int plotPoint = 0; plotPoint < plotPointsArray.length; plotPoint++) {
            String[] pointData = plotPointsArray[plotPoint].trim().split(" ");
            plotPoints.put(plotPoint, new PlotPoint(BigDecimal.valueOf(Double.valueOf(pointData[pointData.length - 2])), BigDecimal.valueOf(Double.valueOf(pointData[pointData.length - 1]))));
        }
        return plotPoints;
    }

    private PlotPoint getPlotOffset() {
        String[] points = plotContainer.getAttribute("transform").split(",");
        BigDecimal xOffset = BigDecimal.valueOf(Integer.valueOf(points[0].replaceAll("[^\\d]", "")).doubleValue());
        BigDecimal yOffset = BigDecimal.valueOf(Integer.valueOf(points[1].replaceAll("[^\\d]", "")).doubleValue() - 1);
        return new PlotPoint(xOffset, yOffset);
    }
}

