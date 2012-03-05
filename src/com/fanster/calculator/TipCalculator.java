package com.fanster.calculator;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TipCalculator extends Activity {

  private TextView editTextBill;
  private TextView editTextTax;
  private TextView editTextTip;
  private TextView editTextSplit;
  private TextView editTextTotal;
  private Button incTipButton;
  private Button decTipButton;
  private Button incSplitButton;
  private Button decSplitButton;
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
    setContentView(R.layout.tip_calculator);

    editTextBill = (TextView) findViewById(R.id.editTextBill);
    editTextTax = (TextView) findViewById(R.id.editTextTax);
    editTextTip = (TextView) findViewById(R.id.editTextTip);
    editTextSplit = (TextView) findViewById(R.id.editTextSplit);
    editTextTotal = (TextView) findViewById(R.id.textViewTotal);

    incTipButton = (Button) findViewById(R.id.buttonIncTip);
    incTipButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextTip.getText().toString())) {
            Integer tip = new Integer(editTextTip.getText().toString());
            tip += 1;
            editTextTip.setText(tip.toString());
          }
        } catch (Exception e) {
          editTextTip.setText(e.getMessage());
        }
      }
    });

    decTipButton = (Button) findViewById(R.id.buttonDecTip);
    decTipButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextTip.getText().toString())) {
            Integer tip = new Integer(editTextTip.getText().toString());
            if (tip > 0) {
              tip -= 1;
            }
            editTextTip.setText(tip.toString());
          }
        } catch (Exception e) {
          editTextTip.setText(e.getMessage());
        }
      }
    });

    incSplitButton = (Button) findViewById(R.id.buttonIncSplit);
    incSplitButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextSplit.getText().toString())) {
            Integer tip = new Integer(editTextSplit.getText().toString());
            tip += 1;
            editTextSplit.setText(tip.toString());
          }
        } catch (Exception e) {
          editTextSplit.setText(e.getMessage());
        }
      }
    });

    decSplitButton = (Button) findViewById(R.id.buttonDecSplit);
    decSplitButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextSplit.getText().toString())) {
            Integer tip = new Integer(editTextSplit.getText().toString());
            if (tip > 1) {
              tip -= 1;
            }
            editTextSplit.setText(tip.toString());
          }
        } catch (Exception e) {
          editTextSplit.setText(e.getMessage());
        }
      }
    });

    calculateButton = (Button) findViewById(R.id.calculateButton);
    calculateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        try {
          if (StringUtils.notEmpty(editTextBill.getText().toString())
              && StringUtils.notEmpty(editTextTax.getText().toString())
              && StringUtils.notEmpty(editTextTip.getText().toString())
              && StringUtils.notEmpty(editTextSplit.getText().toString())) {
            BigDecimal bill = new BigDecimal(editTextBill.getText().toString());
            BigDecimal tax = new BigDecimal(editTextTax.getText().toString());
            BigDecimal taxPercent = MathUtils.divide(tax, BigDecimal.valueOf(100));
            BigDecimal tip = new BigDecimal(editTextTip.getText().toString());
            BigDecimal tipPercent = MathUtils.divide(tip, BigDecimal.valueOf(100));
            BigDecimal split = new BigDecimal(editTextSplit.getText().toString());

            BigDecimal taxedAmount = MathUtils.multiply(bill, taxPercent);
            BigDecimal tippedAmount = MathUtils.multiply(bill, tipPercent);
            BigDecimal total = MathUtils.add(bill, taxedAmount, tippedAmount);
            if (split.intValue() > 1) {
              total = MathUtils.divide(total, split);
            }

            total = total.setScale(2, BigDecimal.ROUND_UP);

            editTextTotal.setText("$" + total.toString());
          }
        } catch (Exception e) {
          editTextTotal.setText(e.getMessage());
        }
      }
    });

    clearButton = (Button) findViewById(R.id.clearButton);
    clearButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        editTextBill.setText("");
        editTextTax.setText("8.75");
        editTextTip.setText("18");
        editTextSplit.setText("1");
        editTextTotal.setText("");
      }
    });
  }
}
