package com.fanster.calculator;

import java.util.EnumSet;

public enum CalculatorType {
  AUTO("Auto Loan"), CREDIT_CARD("Credit Card Payoff"), LOAN("Loan"), MORTGAGE("Monthly Mortgage"), RETIREMENT(
      "Retirement/401(k)"), ROI("Return on Investment"), TVM("Time Value of Money (Nest Egg)"), TIP(
      "Tip");

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
