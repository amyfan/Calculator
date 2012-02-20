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
          if (editTextPrincipal.getText() != null && editTextAnnuity.getText() != null
              && editTextYears.getText() != null && editTextInterest.getText() != null) {
            BigDecimal principal = new BigDecimal(editTextPrincipal.getText().toString());
            BigDecimal annuity = new BigDecimal(editTextAnnuity.getText().toString());
            Integer years = new Integer(editTextYears.getText().toString());
            BigDecimal interestPercent = new BigDecimal(editTextInterest.getText().toString());
            BigDecimal interest = MathUtils.divide(interestPercent, BigDecimal.valueOf(100));

            // calculate PV = A/R - A/[R*(1+R)^N]
            // for now, if no compound interest, R = i
            BigDecimal secondPart = MathUtils.divide(annuity,
                MathUtils.multiply(interest, (interest.add(BigDecimal.valueOf(1))).pow(years)));
            BigDecimal pv = MathUtils.add(principal,
                MathUtils.subtract(MathUtils.divide(annuity, interest), secondPart));

            // calculate FV = PV(1+i)^N
            BigDecimal fv = MathUtils.multiply(pv, MathUtils.add(interest, (BigDecimal.valueOf(1)))
                .pow(years));
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
        editTextFV.setText("");
      }
    });
  }

}