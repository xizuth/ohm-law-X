package com.xizuth.ohmlawcalcu;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import ohm.Current;
import ohm.Resistance;
import ohm.Voltage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int VOLTAGE = 0;
    private static final int CURRENT = 1;
    private static final int RESISTANCE = 2;

    private RadioButton radioVoltage;
    private RadioButton radioCurrent;
    private RadioButton radioResistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadToolbar();
        loadListenerRadio();
        configDefault();
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
                setTypeValue(VOLTAGE);
                voltageSelected();
                break;
            case R.id.radio_current:
                setTypeValue(CURRENT);
                currentSelected();
                break;
            case R.id.radio_ohm:
                setTypeValue(RESISTANCE);
                resistanceSelected();
                break;
        }
    }

    private double getFirstValue() /*throws Exception */ {
        EditText first = (EditText) findViewById(R.id.first_value);
        return Double.parseDouble(first.getText().toString());
    }

    private double getSecondValue() /*throws Exception*/ {
        TextView second = (TextView) findViewById(R.id.second_value);
        return Double.parseDouble(second.getText().toString());
    }

    private void setTypeValue(int type) {
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

    }

    private void voltageSelected() {
        radioSelectedStatus(VOLTAGE);
        double current = getFirstValue();
        double resistance = getSecondValue();
        double result = Voltage.calculateVoltage(current, resistance);

        setResultOperation(result, VOLTAGE);
    }

    private void currentSelected() {
        radioSelectedStatus(CURRENT);

        double voltage = getFirstValue();
        double resistance = getSecondValue();
        double result = Current.calculateCurrent(voltage, resistance);

        setResultOperation(result, CURRENT);
    }

    private void resistanceSelected() {
        radioSelectedStatus(RESISTANCE);

        double voltage = getFirstValue();
        double current = getSecondValue();
        double result = Resistance.calculateResistance(voltage, current);

        setResultOperation(result, RESISTANCE);
    }

    private void setResultOperation(double result, int type) {
        TextView resultView = (TextView) findViewById(R.id.result);

        resultView.setText(String.format("%.2f %s", result, getUnit(type)));
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

    private void loadToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private String getUnit(int type) {
        String[] voltage = getResources().getStringArray(R.array.voltage_list);
        String[] current = getResources().getStringArray(R.array.current_list);
        String[] resistance = getResources().getStringArray(R.array.resistance_list);

        if (type == CURRENT) {
            return current[3];
        }
        if (type == RESISTANCE) {
            return resistance[3];
        } else {
            return voltage[3];
        }
    }

    private void configDefault() {
        setTypeValue(VOLTAGE);

    }

}
