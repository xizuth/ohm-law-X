package com.xizuth.ohmlawcalcu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.xizuth.ohmlawcalcu.admob.AdMob;
import com.xizuth.ohmlawcalcu.admob.Key;
import com.xizuth.ohmlawcalcu.util.Unit;
import com.xizuth.ohmlawcalcu.util.Unity;

import ohm.Current;
import ohm.Resistance;
import ohm.Voltage;

import static com.xizuth.ohmlawcalcu.util.Unit.CURRENT;
import static com.xizuth.ohmlawcalcu.util.Unit.RESISTANCE;
import static com.xizuth.ohmlawcalcu.util.Unit.VOLTAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int FIRST = -1;
    private static final int SECOND = -2;
    private AdMob adMob;
    private RadioButton radioVoltage;
    private RadioButton radioCurrent;
    private RadioButton radioResistance;
    private AdapterView.OnItemSelectedListener onItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            calculate();
            hideKeyBoard();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            calculate();
        }
    };
    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                calculate();
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadToolbar();
        loadListenerRadio();
        configDefault();
        adMob = new AdMob(this, Key.MAIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_formula:
                startActivity(new Intent(this, FormulaActivity.class));
                break;
            case R.id.action_about:
                break;
        }
        return true;
    }

    private void loadToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void loadListenerRadio() {
        radioVoltage = (RadioButton) findViewById(R.id.radio_voltage);
        radioCurrent = (RadioButton) findViewById(R.id.radio_current);
        radioResistance = (RadioButton) findViewById(R.id.radio_ohm);
        radioVoltage.setOnClickListener(this);
        radioCurrent.setOnClickListener(this);
        radioResistance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.radio_voltage:
                setTypeValueEditText(VOLTAGE);
                voltageSelected();
                break;
            case R.id.radio_current:
                setTypeValueEditText(CURRENT);
                currentSelected();
                break;
            case R.id.radio_ohm:
                setTypeValueEditText(RESISTANCE);
                resistanceSelected();
                break;
        }
    }

    private double getFirstValue() {
        EditText first = (EditText) findViewById(R.id.first_value);
        first.setOnEditorActionListener(onEditorActionListener);
        String result = first.getText().toString();

        if (result.isEmpty())
            return 0.0;

        return Double.parseDouble(result);
    }

    private double getSecondValue() {
        TextView second = (TextView) findViewById(R.id.second_value);
        second.setOnEditorActionListener(onEditorActionListener);
        String result = second.getText().toString();

        if (result.isEmpty())
            return 0.0;

        return Double.parseDouble(result);
    }

    private void setTypeValueEditText(int type) {
        TextInputLayout first = (TextInputLayout) findViewById(R.id.first_value_main);
        TextInputLayout second = (TextInputLayout) findViewById(R.id.second_value_main);


        int firstValue = R.string.current;
        int secondValue = R.string.resistance;

        if (type == VOLTAGE) {
            firstValue = R.string.current;
            secondValue = R.string.resistance;
        }
        if (type == CURRENT) {
            firstValue = R.string.voltage;
            secondValue = R.string.resistance;
        }
        if (type == RESISTANCE) {
            firstValue = R.string.voltage;
            secondValue = R.string.current;
        }

        setListTypeSpinner(type);

        first.setHint(getString(firstValue));
        second.setHint(getString(secondValue));
    }

    private void setListTypeSpinner(int type) {
        String[] voltage = getResources().getStringArray(R.array.voltage_list);
        String[] current = getResources().getStringArray(R.array.current_list);
        String[] resistance = getResources().getStringArray(R.array.resistance_list);

        Spinner firstSpinner = (Spinner) findViewById(R.id.spinner_first_value);
        Spinner secondSpinner = (Spinner) findViewById(R.id.spinner_second_value);

        ArrayAdapter<String> firstAdapter = null;
        ArrayAdapter<String> secondAdapter = null;

        if (type == VOLTAGE) {
            firstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, current);
            secondAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, resistance);
        }
        if (type == CURRENT) {
            firstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, voltage);
            secondAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, resistance);
        }
        if (type == RESISTANCE) {
            firstAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, voltage);
            secondAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, current);
        }

        firstSpinner.setAdapter(firstAdapter);
        secondSpinner.setAdapter(secondAdapter);

        firstSpinner.setSelection(3);
        secondSpinner.setSelection(3);

        firstSpinner.setOnItemSelectedListener(onItemClickListener);
        secondSpinner.setOnItemSelectedListener(onItemClickListener);

    }

    private void voltageSelected() {
        radioSelectedStatus(VOLTAGE);

        double current = getFirstValue();
        current *= getUnit(FIRST);
        double resistance = getSecondValue();
        resistance *= getUnit(SECOND);
        double result = Voltage.calculateVoltage(current, resistance);

        setResultOperation(result, VOLTAGE);
    }

    private void currentSelected() {
        radioSelectedStatus(CURRENT);

        double voltage = getFirstValue();
        voltage *= getUnit(FIRST);
        double resistance = getSecondValue();
        resistance *= getUnit(SECOND);
        double result = Current.calculateCurrent(voltage, resistance);

        setResultOperation(result, CURRENT);
    }

    private void resistanceSelected() {
        radioSelectedStatus(RESISTANCE);

        double voltage = getFirstValue();
        voltage *= getUnit(FIRST);
        double current = getSecondValue();
        current *= getUnit(SECOND);
        double result = Resistance.calculateResistance(voltage, current);

        setResultOperation(result, RESISTANCE);
    }

    private void setResultOperation(double result, int type) {
        TextView resultView = (TextView) findViewById(R.id.result);
        String r = String.valueOf(result);

        if (r.equalsIgnoreCase("NaN")) {
            resultView.setText(getText(R.string.infinite));
        } else {
            Unity unity = new Unity(this, result, type);
            resultView.setText(String.format("%.2f %s", unity.getValue(), unity.getUnity()));
        }
    }

    private void radioSelectedStatus(int selected) {

        if (selected == VOLTAGE) {
            radioCurrent.setChecked(false);
            radioResistance.setChecked(false);
        }

        if (selected == CURRENT) {
            radioVoltage.setChecked(false);
            radioResistance.setChecked(false);
        }

        if (selected == RESISTANCE) {
            radioCurrent.setChecked(false);
            radioVoltage.setChecked(false);
        }
    }

    private void configDefault() {
        setTypeValueEditText(VOLTAGE);
        setListenerEditTest();
    }

    private void setListenerEditTest() {
        EditText first = (EditText) findViewById(R.id.first_value);
        TextView second = (TextView) findViewById(R.id.second_value);

        first.addTextChangedListener(textWatcher);
        second.addTextChangedListener(textWatcher);
    }

    private double getUnit(int position) {
        int unit;

        if (position == FIRST) {
            unit = ((Spinner) findViewById(R.id.spinner_first_value)).getSelectedItemPosition();
        } else {
            unit = ((Spinner) findViewById(R.id.spinner_second_value)).getSelectedItemPosition();
        }

        return Unit.units()[unit];
    }

    private void calculate() {
        if (radioVoltage.isChecked()) {
            voltageSelected();
        }
        if (radioCurrent.isChecked()) {
            currentSelected();
        }
        if (radioResistance.isChecked()) {
            resistanceSelected();
        }
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        adMob.pauseAdMob();
        super.onPause();
    }

    @Override
    protected void onResume() {
        adMob.resumeAdMob();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        adMob.destroyAdMob();
        super.onDestroy();
    }
}
