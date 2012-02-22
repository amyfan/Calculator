package com.fanster.calculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreditCalculator extends Activity {

  private TextView editTextBalance;
  private TextView editTextInterest;
  private TextView editTextPayment;
  private TextView editTextMonths;
  private Button calculateButton;
  private Button clearButton;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      init();
    } catch (Exception e) {
      Log.e("CreditCalculator", e.getMessage());
    }
  }

  private void init() {
    setContentView(R.layout.credit_calculator);

    editTextBalance = (TextView) findViewById(R.id.editTextBalance);
    editTextInterest = (TextView) findViewById(R.id.editTextInterest);
    editTextPayment = (TextView) findViewById(R.id.editTextPayment);
    editTextMonths = (TextView) findViewById(R.id.editTextMonths);

    calculateButton = (Button) findViewById(R.id.calculateButton);
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (editTextBalance.getText() != null && editTextInterest.getText() != null
              && (editTextPayment.getText() != null || editTextMonths.getText() != null)) {
            BigDecimal balance = new BigDecimal(editTextBalance.getText().toString());
            BigDecimal interestPercent = new BigDecimal(editTextInterest.getText().toString());
            BigDecimal interest = MathUtils.divide(interestPercent, BigDecimal.valueOf(100));
            BigDecimal payment = null;
            Integer months = null;
            if (editTextPayment.getText() != null) {
              payment = new BigDecimal(editTextPayment.getText().toString());
            } else {
              months = new Integer(editTextMonths.getText().toString());
            }

            if (payment == null) {
              // payment = [balance(1+interest)^months * r] / [(1+interest)^months-1]
              BigDecimal top = MathUtils.multiply(
                  MathUtils.multiply(balance,
                      MathUtils.add(BigDecimal.valueOf(1), interest).pow(months)), interest);
              BigDecimal bottom = MathUtils.subtract(MathUtils.add(BigDecimal.valueOf(1), interest)
                  .pow(months), BigDecimal.valueOf(1));
              payment = MathUtils.divide(top, bottom);
              payment = payment.setScale(2, BigDecimal.ROUND_HALF_UP);

              editTextPayment.setText(payment.toString());
            }
          }
        } catch (Exception e) {
          editTextPayment.setText(e.getMessage());
        }
      }
    });

    clearButton = (Button) findViewById(R.id.clearButton);
    clearButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        editTextMonths.setText("");
        editTextPayment.setText("");
      }
    });
  }

}