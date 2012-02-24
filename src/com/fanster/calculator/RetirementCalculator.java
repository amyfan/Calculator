package com.fanster.calculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This calculator is a slightly more specific variation on the TVM calculator
 * 
 * @author Amy
 * 
 */
public class RetirementCalculator extends Activity {

  private TextView editTextPrincipal;
  private TextView editTextSalary;
  private TextView editTextContribution;
  private TextView editTextMatch;
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
      Log.e("RetirementCalculator", e.getMessage());
    }
  }

  private void init() {
    setContentView(R.layout.retirement_calculator);

    editTextPrincipal = (TextView) findViewById(R.id.editTextPrincipal);
    editTextSalary = (TextView) findViewById(R.id.editTextSalary);
    editTextContribution = (TextView) findViewById(R.id.editTextContribution);
    editTextMatch = (TextView) findViewById(R.id.editTextMatch);
    editTextYears = (TextView) findViewById(R.id.editTextYears);
    editTextInterest = (TextView) findViewById(R.id.editTextInterest);
    editTextFV = (TextView) findViewById(R.id.textViewFV);

    calculateButton = (Button) findViewById(R.id.calculateButton);
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextPrincipal.getText().toString())
              && StringUtils.notEmpty(editTextSalary.getText().toString())
              && StringUtils.notEmpty(editTextYears.getText().toString())
              && StringUtils.notEmpty(editTextContribution.getText().toString())
              && StringUtils.notEmpty(editTextMatch.getText().toString())
              && StringUtils.notEmpty(editTextInterest.getText().toString())) {
            BigDecimal principal = new BigDecimal(editTextPrincipal.getText().toString());
            BigDecimal salary = new BigDecimal(editTextSalary.getText().toString());
            BigDecimal contributionPercent = new BigDecimal(editTextContribution.getText()
                .toString());
            BigDecimal contribution = MathUtils.divide(contributionPercent, BigDecimal.valueOf(100));
            BigDecimal matchPercent = new BigDecimal(editTextMatch.getText().toString());
            BigDecimal match = MathUtils.divide(matchPercent, BigDecimal.valueOf(100));
            Integer years = new Integer(editTextYears.getText().toString());
            BigDecimal interestPercent = new BigDecimal(editTextInterest.getText().toString());
            BigDecimal interest = MathUtils.divide(interestPercent, BigDecimal.valueOf(100));

            BigDecimal monthlySalary = MathUtils.divide(salary, BigDecimal.valueOf(12));
            BigDecimal contributionAmount = MathUtils.multiply(monthlySalary, contribution);
            if (match.doubleValue() > 0 && match.doubleValue() > contribution.doubleValue()) {
              match = contribution;
              editTextMatch.setText(contributionPercent.toString());
            }
            BigDecimal matchAmount = MathUtils.multiply(monthlySalary, match);
            BigDecimal annuity = MathUtils.add(contributionAmount, matchAmount);

            // calculate PV = A/R - A/[R*(1+R)^N]
            // A = annuity, R = interest/12, N = years*12
            BigDecimal r = MathUtils.divide(interest, BigDecimal.valueOf(12));
            Integer n = years * 12;
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
        editTextSalary.setText("");
        editTextContribution.setText("");
        editTextMatch.setText("");
        editTextYears.setText("");
        editTextInterest.setText("");
        editTextFV.setText("");
      }
    });
  }

}