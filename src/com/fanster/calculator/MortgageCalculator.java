package com.fanster.calculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MortgageCalculator extends Activity {

  private TextView editTextPrice;
  private TextView editTextDownPayment;
  private TextView editTextYears;
  private TextView editTextInterest;
  private TextView editTextPropertyTax;
  private TextView editTextAppraised;
  private TextView editTextInsurance;
  private TextView editTextTotal;
  private Button calculateButton;
  private Button clearButton;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      init();
    } catch (Exception e) {
      Log.e("MortageCalculator", e.getMessage());
    }
  }

  private void init() {
    setContentView(R.layout.mortgage_calculator);

    editTextPrice = (TextView) findViewById(R.id.editTextPrice);
    editTextDownPayment = (TextView) findViewById(R.id.editTextDownPayment);
    editTextYears = (TextView) findViewById(R.id.editTextYears);
    editTextInterest = (TextView) findViewById(R.id.editTextInterest);
    editTextPropertyTax = (TextView) findViewById(R.id.editTextPropertyTax);
    editTextAppraised = (TextView) findViewById(R.id.editTextAppraised);
    editTextInsurance = (TextView) findViewById(R.id.editTextInsurance);
    editTextTotal = (TextView) findViewById(R.id.textViewTotal);

    calculateButton = (Button) findViewById(R.id.calculateButton);
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextPrice.getText().toString()) && StringUtils.notEmpty(editTextDownPayment.getText().toString())
              && StringUtils.notEmpty(editTextYears.getText().toString()) && StringUtils.notEmpty(editTextInterest.getText().toString())
              && StringUtils.notEmpty(editTextPropertyTax.getText().toString()) && StringUtils.notEmpty(editTextAppraised.getText().toString())
              && StringUtils.notEmpty(editTextInsurance.getText().toString())) {
            BigDecimal price = new BigDecimal(editTextPrice.getText().toString());
            BigDecimal downPaymentPercent = new BigDecimal(editTextDownPayment.getText().toString());
            Integer years = new Integer(editTextYears.getText().toString());
            BigDecimal interestPercent = new BigDecimal(editTextInterest.getText().toString());

            BigDecimal downPayment = MathUtils.multiply(price,
                MathUtils.divide(downPaymentPercent, BigDecimal.valueOf(100)));
            BigDecimal priceAfterDown = MathUtils.subtract(price, downPayment);
            BigDecimal monthlyInterest = MathUtils.divide(
                MathUtils.divide(interestPercent, BigDecimal.valueOf(100)), BigDecimal.valueOf(12));
            Integer months = years * 12;

            // mortgage = loan[i(1+i)^n]/[(1+i)^n-1]
            BigDecimal dividend = MathUtils.multiply(
                priceAfterDown,
                MathUtils.multiply(monthlyInterest,
                    MathUtils.add(BigDecimal.valueOf(1), monthlyInterest).pow(months)));
            BigDecimal divisor = MathUtils.subtract(
                MathUtils.add(BigDecimal.valueOf(1), monthlyInterest).pow(months),
                BigDecimal.valueOf(1));
            BigDecimal mortgagePayment = MathUtils.divide(dividend, divisor);

            // add property tax + insurance
            BigDecimal propertyTaxPercent = new BigDecimal(editTextPropertyTax.getText().toString());
            BigDecimal appraisedValue = new BigDecimal(editTextAppraised.getText().toString());
            BigDecimal monthlyInsurance = new BigDecimal(editTextInsurance.getText().toString());
            BigDecimal monthlyPropertyTax = MathUtils.divide(
                MathUtils.multiply(appraisedValue, propertyTaxPercent), BigDecimal.valueOf(1200));

            BigDecimal totalPayment = MathUtils.add(mortgagePayment,
                MathUtils.add(monthlyPropertyTax, monthlyInsurance));
            totalPayment = totalPayment.setScale(2, BigDecimal.ROUND_HALF_UP);

            editTextTotal.setText("$" + totalPayment.toString());
          }
        } catch (Exception e) {
          editTextTotal.setText(e.getMessage());
        }
      }
    });

    clearButton = (Button) findViewById(R.id.clearButton);
    clearButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        editTextPrice.setText("");
        editTextDownPayment.setText("");
        editTextYears.setText("");
        editTextInterest.setText("");
        editTextPropertyTax.setText("");
        editTextAppraised.setText("");
        editTextInsurance.setText("");
        editTextTotal.setText("");
      }
    });

    editTextPrice.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        editTextAppraised.setText(editTextPrice.getText().toString());

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

      }
    });
  }
}