package com.lazerycode.selenium.graphs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

public class ColumnChart extends HighCharts {

    public ColumnChart(WebDriver driver, WebElement chart) {
        super(driver, chart);
    }

    public void hoverOverPrimarySeriesAtXAxisLabel(String xAxisLabel) {
        hoverOverColumnOrBarChartSeriesAtXAxisPosition(1, xAxisLabel);
    }

    public void hoverOverSecondarySeriesAtXAxisLabel(String xAxisLabel) {
        hoverOverColumnOrBarChartSeriesAtXAxisPosition(2, xAxisLabel);
    }

    public Color getPrimarySeriesColourForXAxisLabel(String xAxisLabelValue) {
        return getSeriesColorAtXAxisPosition(1, xAxisLabelValue);
    }

    public Color getSecondarySeriesColourForXAxisLabel(String xAxisLabelValue) {
        return getSeriesColorAtXAxisPosition(2, xAxisLabelValue);
    }
}
