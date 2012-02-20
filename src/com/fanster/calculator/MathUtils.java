package com.fanster.calculator;

import java.math.BigDecimal;

public class MathUtils {

  public static final BigDecimal add(BigDecimal... values) {
    BigDecimal total = new BigDecimal(0);
    for (BigDecimal value : values) {
      total = total.add(value);
    }
    return total;
  }

  public static final BigDecimal subtract(BigDecimal val1, BigDecimal val2) {
    return val1.subtract(val2);
  }

  public static final BigDecimal multiply(BigDecimal val1, BigDecimal val2) {
    return val1.multiply(val2);
  }

  public static final BigDecimal divide(BigDecimal val1, BigDecimal val2) {
    return val1.divide(val2, 5, BigDecimal.ROUND_HALF_UP);
  }

}
