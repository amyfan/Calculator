package com.fanster.calculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class ROICalculator extends Activity {

  private TextView editTextInvestment;
  private TextView editTextReturn;
  private DatePicker datePickerStart;
  private DatePicker datePickerEnd;
  private TextView editTextGain;
  private TextView editTextROI;
  private TextView editTextAnnualized;
  private TextView editTextTerm;
  private Button calculateButton;
  private Button clearButton;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      init();
    } catch (Exception e) {
      Log.e("ROICalculator", e.getMessage());
    }
  }

  private void init() {
    setContentView(R.layout.roi_calculator);

    editTextInvestment = (TextView) findViewById(R.id.editTextInvestment);
    editTextReturn = (TextView) findViewById(R.id.editTextReturn);
    datePickerStart = (DatePicker) findViewById(R.id.datePickerStart);
    datePickerEnd = (DatePicker) findViewById(R.id.datePickerEnd);
    editTextGain = (TextView) findViewById(R.id.textViewGain);
    editTextROI = (TextView) findViewById(R.id.textViewROI);
    editTextAnnualized = (TextView) findViewById(R.id.textViewAnnualized);
    editTextTerm = (TextView) findViewById(R.id.textViewTerm);

    calculateButton = (Button) findViewById(R.id.calculateButton);
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextInvestment.getText().toString())
              && StringUtils.notEmpty(editTextReturn.getText().toString())
              && datePickerStart.getYear() > 0 && datePickerStart.getMonth() > 0
              && datePickerStart.getDayOfMonth() > 0 && datePickerEnd.getYear() > 0
              && datePickerEnd.getMonth() > 0 && datePickerEnd.getDayOfMonth() > 0) {
            BigDecimal investment = new BigDecimal(editTextInvestment.getText().toString());
            BigDecimal returnAmount = new BigDecimal(editTextReturn.getText().toString());

            // Date startDate = new Date();
            // startDate.setDate(datePickerStart.getDayOfMonth());
            // startDate.setMonth(datePickerStart.getMonth());
            // startDate.setYear(datePickerStart.getYear());
            //
            // Date endDate = new Date();
            // endDate.setDate(datePickerEnd.getDayOfMonth());
            // endDate.setMonth(datePickerEnd.getMonth());
            // endDate.setYear(datePickerEnd.getYear());

            // Long term = endDate.getTime() - startDate.getTime();

            int years = datePickerEnd.getYear() - datePickerStart.getYear();
            int months = datePickerEnd.getMonth() - datePickerStart.getMonth();
            int days = datePickerEnd.getDayOfMonth() - datePickerStart.getDayOfMonth();

            if (years < 0) {
              // reverse start & end dates for the sake of calculating something
              // valid
              years *= -1;
              months *= datePickerStart.getMonth() - datePickerEnd.getMonth();
              days *= datePickerStart.getDayOfMonth() - datePickerEnd.getDayOfMonth();
            }

            int termInMonths = (years * 12) + months + (days / 15);
            BigDecimal termInYears = MathUtils.divide(BigDecimal.valueOf(termInMonths),
                BigDecimal.valueOf(12));
            termInYears = termInYears.setScale(1, BigDecimal.ROUND_HALF_UP);

            BigDecimal gain = MathUtils.subtract(returnAmount, investment);
            gain = gain.setScale(2, BigDecimal.ROUND_HALF_UP);

            BigDecimal roi = MathUtils.divide(gain, investment);
            roi = MathUtils.multiply(roi, BigDecimal.valueOf(100));
            roi = roi.setScale(3, BigDecimal.ROUND_HALF_UP);

            // CAGR = (return/investment) ^ (1/years) - 1
            BigDecimal base = MathUtils.divide(returnAmount, investment);
            BigDecimal exponent = MathUtils.divide(BigDecimal.valueOf(1), termInYears);
            BigDecimal annualized = MathUtils.subtract(
                BigDecimal.valueOf(Math.pow(base.doubleValue(), exponent.doubleValue())),
                BigDecimal.valueOf(1));

            annualized = MathUtils.multiply(annualized, BigDecimal.valueOf(100));
            annualized = annualized.setScale(3, BigDecimal.ROUND_HALF_UP);

            editTextGain.setText("$" + gain.toString());
            editTextROI.setText(roi.toString() + "%");
            editTextAnnualized.setText(annualized.toString() + "%");
            editTextTerm.setText("" + termInYears);
          }
        } catch (Exception e) {
          editTextROI.setText(e.getMessage());
        }
      }
    });

    clearButton = (Button) findViewById(R.id.clearButton);
    clearButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        editTextInvestment.setText("");
        editTextReturn.setText("");
        editTextGain.setText("");
        editTextROI.setText("");
        editTextAnnualized.setText("");
        editTextTerm.setText("");
      }
    });
  }
}