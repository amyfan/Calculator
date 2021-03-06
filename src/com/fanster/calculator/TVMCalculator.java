package com.fanster.calculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TVMCalculator extends Activity {

  private TextView editTextPrincipal;
  private TextView editTextAnnuity;
  private TextView editTextYears;
  private TextView editTextInterest;
  private TextView editTextFV;
  private Button calculateButton;
  private Button clearButton;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      init();
    } catch (Exception e) {
      Log.e("TVMCalculator", e.getMessage());
    }
  }

  private void init() {
    setContentView(R.layout.tvm_calculator);

    editTextPrincipal = (TextView) findViewById(R.id.editTextPrincipal);
    editTextAnnuity = (TextView) findViewById(R.id.editTextAnnuity);
    editTextYears = (TextView) findViewById(R.id.editTextYears);
    editTextInterest = (TextView) findViewById(R.id.editTextInterest);
    editTextFV = (TextView) findViewById(R.id.textViewFV);

    calculateButton = (Button) findViewById(R.id.calculateButton);
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextPrincipal.getText().toString())
              && StringUtils.notEmpty(editTextAnnuity.getText().toString())
              && StringUtils.notEmpty(editTextYears.getText().toString())
              && StringUtils.notEmpty(editTextInterest.getText().toString())) {
            BigDecimal principal = new BigDecimal(editTextPrincipal.getText().toString());
            BigDecimal annuity = new BigDecimal(editTextAnnuity.getText().toString());
            Integer years = new Integer(editTextYears.getText().toString());
            BigDecimal interestPercent = new BigDecimal(editTextInterest.getText().toString());
            BigDecimal interest = MathUtils.divide(interestPercent, BigDecimal.valueOf(100));

            int compounded = 1;
            // calculate PV = A/R - A/[R*(1+R)^N]
            // A = annuity, R = interest/(# compounded), N = years*(#
            // compounded)
            BigDecimal r = MathUtils.divide(interest, BigDecimal.valueOf(compounded));
            Integer n = years * compounded;
            BigDecimal firstPart = MathUtils.divide(annuity, r);
            BigDecimal secondPart = MathUtils.divide(annuity,
                MathUtils.multiply(r, (r.add(BigDecimal.valueOf(1))).pow(n)));
            BigDecimal pv = MathUtils.add(principal, MathUtils.subtract(firstPart, secondPart));

            // calculate FV = PV(1+R)^N
            BigDecimal fv = MathUtils.multiply(pv, MathUtils.add(r, (BigDecimal.valueOf(1))).pow(n));
            fv = fv.setScale(2, BigDecimal.ROUND_HALF_UP);

            editTextFV.setText("$" + fv.toString());
          }
        } catch (Exception e) {
          editTextFV.setText(e.getMessage());
        }
      }
    });

    clearButton = (Button) findViewById(R.id.clearButton);
    clearButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        editTextPrincipal.setText("");
        editTextAnnuity.setText("");
        editTextYears.setText("");
        editTextInterest.setText("");
        editTextFV.setText("");
      }
    });
  }

}