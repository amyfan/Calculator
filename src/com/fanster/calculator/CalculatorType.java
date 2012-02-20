package com.fanster.calculator;

import java.util.EnumSet;

public enum CalculatorType {
  DEBT ("Debt Payoff"), MORTGAGE("Monthly Mortgage"), TVM("Nest Egg (Time Value of Money)"), TIP("Tip");

  private String name;

  private CalculatorType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static CalculatorType fromValue(String name) {
    for (final CalculatorType element : EnumSet.allOf(CalculatorType.class)) {
      if (element.getName().equals(name)) {
        return element;
      }
    }
    throw new IllegalArgumentException("Cannot be parsed into an enum element : '" + name + "'");
  }
}
