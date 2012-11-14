package com.lazerycode.selenium.graphs;

import java.math.BigDecimal;
import java.math.RoundingMode;

class PlotPoint {

  private final BigDecimal x;
  private final BigDecimal y;

  PlotPoint(BigDecimal x, BigDecimal y) {
    this.x = x.setScale(0, RoundingMode.HALF_UP);
    this.y = y.setScale(0, RoundingMode.HALF_UP);
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }

    if (!(other instanceof PlotPoint)) {
      return false;
    }

    return (this.x.equals(((PlotPoint) other).getX()) && this.y.equals(((PlotPoint) other).getY()));
  }

  @Override
  public int hashCode() {
    return this.x.hashCode() + this.y.hashCode();
  }

  public int getX() {
    return x.intValue();
  }

  public int getY() {
    return y.intValue();
  }
}
