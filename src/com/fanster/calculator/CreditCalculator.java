package com.fanster.calculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
    editTextPayment.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        // if (editTextPayment.didTouchFocusSelect()) {
        editTextMonths.setText("");
        // }
        return false;
      }
    });

    editTextMonths = (TextView) findViewById(R.id.editTextMonths);
    editTextMonths.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        // if (editTextPayment.didTouchFocusSelect()) {
        editTextPayment.setText("");
        // }
        return false;
      }
    });

    calculateButton = (Button) findViewById(R.id.calculateButton);
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextBalance.getText().toString())
              && StringUtils.notEmpty(editTextInterest.getText().toString())
              && (StringUtils.notEmpty(editTextPayment.getText().toString()) || StringUtils
                  .notEmpty(editTextMonths.getText().toString()))) {
            BigDecimal balance = new BigDecimal(editTextBalance.getText().toString());
            BigDecimal interestPercent = new BigDecimal(editTextInterest.getText().toString());
            BigDecimal interest = MathUtils.divide(interestPercent, BigDecimal.valueOf(100));
            BigDecimal monthlyInterest = MathUtils.divide(interest, BigDecimal.valueOf(12));

            BigDecimal payment = null;
            Integer months = null;
            if (StringUtils.notEmpty(editTextPayment.getText().toString())) {
              payment = new BigDecimal(editTextPayment.getText().toString());
            } else {
              months = new Integer(editTextMonths.getText().toString());
            }

            if (payment == null) {
              // payment = [balance(1+interest)^months * r] /
              // [(1+interest)^months-1]
              BigDecimal top = MathUtils.multiply(MathUtils.multiply(balance,
                  MathUtils.add(BigDecimal.valueOf(1), monthlyInterest).pow(months)),
                  monthlyInterest);
              BigDecimal bottom = MathUtils.subtract(
                  MathUtils.add(BigDecimal.valueOf(1), monthlyInterest).pow(months),
                  BigDecimal.valueOf(1));
              payment = MathUtils.divide(top, bottom);
              payment = payment.setScale(2, BigDecimal.ROUND_HALF_UP);

              editTextPayment.setText(payment.toString());
            } else {
              // months = [(balance * interest) + balance] / payment
              // BigDecimal top = MathUtils.add(MathUtils.multiply(balance,
              // interest), balance);
              // BigDecimal monthsDecimal = MathUtils.divide(top, payment);
              // months = monthsDecimal.intValue();
              // if (monthsDecimal.doubleValue() > months) {
              // months += 1;
              // }

              // N = -log(1-(interest * balance)/payment) / log(1+interest)
              BigDecimal topUnlogged = MathUtils.subtract(BigDecimal.valueOf(1),
                  MathUtils.divide(MathUtils.multiply(balance, monthlyInterest), payment));
              BigDecimal bottomUnlogged = MathUtils.add(BigDecimal.valueOf(1), monthlyInterest);

              // easiest way to calculate log is to use Math.log(double)
              // unfortunately
              BigDecimal top = MathUtils.multiply(BigDecimal.valueOf(-1),
                  BigDecimal.valueOf(Math.log(topUnlogged.doubleValue())));
              BigDecimal bottom = BigDecimal.valueOf(Math.log(bottomUnlogged.doubleValue()));

              BigDecimal monthsDecimal = MathUtils.divide(top, bottom);
              months = monthsDecimal.intValue();
              if (monthsDecimal.doubleValue() > months) {
                months += 1;
              }

              editTextMonths.setText(months.toString());
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
        editTextBalance.setText("");
        editTextInterest.setText("");
        editTextMonths.setText("");
        editTextPayment.setText("");
      }
    });
  }
}