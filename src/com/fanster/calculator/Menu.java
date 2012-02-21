package com.fanster.calculator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final String[] calculatorTypes = getResources().getStringArray(R.array.calculators_array);

    setListAdapter(new ArrayAdapter<String>(this, R.layout.menu_item, calculatorTypes));

    ListView lv = getListView();
    lv.setTextFilterEnabled(true);

    lv.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String calculatorType = calculatorTypes[position];
        Intent showCalculator = new Intent(getApplicationContext(),
            getCalculatorClass(calculatorType));
        startActivity(showCalculator);
      }
    });
  }

  private Class<?> getCalculatorClass(String calculatorType) {
    if (calculatorType != null) {
      switch (CalculatorType.fromValue(calculatorType)) {
      case MORTGAGE:
        return MortgageCalculator.class;
      case ROI:
        return ROICalculator.class;
      case TIP:
        return TipCalculator.class;
      case TVM:
        return TVMCalculator.class;
      }

    }
    return null;
  }
}
