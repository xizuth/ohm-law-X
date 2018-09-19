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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.xizuth.ohmlawcalcu.util.FormatValue;
import com.xizuth.ohmlawcalcu.util.OpenURL;
import com.xizuth.ohmlawcalcu.util.URL;
import com.xizuth.ohmlawcalcu.util.Unit;
import com.xizuth.ohmlawcalcu.util.Unity;

import ohm.Current;
import ohm.Power;
import ohm.Resistance;
import ohm.Voltage;

import static com.xizuth.ohmlawcalcu.util.Unit.CURRENT;
import static com.xizuth.ohmlawcalcu.util.Unit.POWER;
import static com.xizuth.ohmlawcalcu.util.Unit.RESISTANCE;
import static com.xizuth.ohmlawcalcu.util.Unit.VOLTAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = MainActivity.class.getSimpleName();
    private static final int FIRST = -1;
    private static final int SECOND = -2;
    private RadioButton radioVoltage;
    private RadioButton radioCurrent;
    private RadioButton radioResistance;
    private Switch switchPower;
    private Switch switchGeneral;
    private boolean powerActivate = false;
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
            if (i == EditorInfo.IME_ACTION_NEXT) {
                calculate();
            }

            if (i == EditorInfo.IME_ACTION_DONE) {
                hideKeyBoard();
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
        loadListenerSwitchPower();
        loadListenerRadio();
        configDefault();
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
                OpenURL.open(this, URL.XIZUTH_WEB);
                break;
            case R.id.action_open_web:
                OpenURL.open(this, URL.APP_WEB);
                break;
            case R.id.action_clear:
                clearUI();
                break;
            case R.id.action_more_apps:
                OpenURL.open(this, URL.APPS);
                break;
        }
        return true;
    }

    private void clearUI() {
        ((EditText) findViewById(R.id.first_value)).setText("");
        ((EditText) findViewById(R.id.second_value)).setText("");
        ((TextView) findViewById(R.id.result)).setText("-");
        ((TextView) findViewById(R.id.result_power)).setText("-");
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
        switchGeneral.setOnClickListener(this);
        switchPower.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.radio_voltage:
                radioSelectedStatus(VOLTAGE);
                setTypeValueEditText(VOLTAGE);
                setListTypeSpinner(VOLTAGE);
                loadNameSwitchGeneral();
                voltageSelected();
                break;
            case R.id.radio_current:
                radioSelectedStatus(CURRENT);
                setTypeValueEditText(CURRENT);
                setListTypeSpinner(CURRENT);
                loadNameSwitchGeneral();
                currentSelected();
                break;
            case R.id.radio_ohm:
                radioSelectedStatus(RESISTANCE);
                setTypeValueEditText(RESISTANCE);
                setListTypeSpinner(RESISTANCE);
                loadNameSwitchGeneral();
                resistanceSelected();
                break;
            case R.id.switch_power:
                calculate();
                break;
            case R.id.switch_general:
                loadNameSwitchGeneral();
                setTypeValueEditText(POWER);
                setListTypeSpinner(POWER);
                calculate();
                break;
        }

    }

    private void loadListenerSwitchPower() {
        switchPower = findViewById(R.id.switch_power);
        switchGeneral = findViewById(R.id.switch_general);
        switchPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                powerActivate = status;
                if (status) {
                    findViewById(R.id.layout_switch).setVisibility(View.VISIBLE);
                    loadNameSwitchGeneral();
                    setTypeValueEditText(POWER);
                    setListTypeSpinner(POWER);
                    findViewById(R.id.result_power).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.result_power).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_switch).setVisibility(View.GONE);
                    int type = radioVoltage.isChecked() ? VOLTAGE : radioCurrent.isChecked() ? CURRENT : RESISTANCE;
                    setTypeValueEditText(type);
                    setListTypeSpinner(type);
                }
            }
        });

    }

    private double getValueFromUI(int position) {
        String result;

        if (position == FIRST) {
            EditText first = (EditText) findViewById(R.id.first_value);
            first.setOnEditorActionListener(onEditorActionListener);
            result = first.getText().toString();
        } else {
            TextView second = (TextView) findViewById(R.id.second_value);
            second.setOnEditorActionListener(onEditorActionListener);
            result = second.getText().toString();
        }

        if (result.isEmpty() || (result.length() == 1 && result.startsWith("-")))
            return 0.0;


        return Double.parseDouble(result);
    }

    private void setTypeValueEditText(int type) {
        TextInputLayout first = (TextInputLayout) findViewById(R.id.first_value_main);
        TextInputLayout second = (TextInputLayout) findViewById(R.id.second_value_main);

        int firstValue = R.string.current;
        int secondValue = R.string.resistance;

        if (!powerActivate) {
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
        } else {
            firstValue = R.string.power;
            if (radioVoltage.isChecked() || type == VOLTAGE) {
                secondValue = !switchGeneral.isChecked() ? R.string.current : R.string.resistance;

            }

            if (radioCurrent.isChecked() || type == CURRENT) {
                secondValue = !switchGeneral.isChecked() ? R.string.resistance : R.string.voltage;

            }
            if (radioResistance.isChecked() || type == RESISTANCE) {
                secondValue = !switchGeneral.isChecked() ? R.string.voltage : R.string.current;
            }
        }

        first.setHint(getString(firstValue));
        second.setHint(getString(secondValue));
    }

    private void setListTypeSpinner(int type) {
        String[] voltage = Unit.getListUnitType(this, VOLTAGE);
        String[] current = Unit.getListUnitType(this, CURRENT);
        String[] resistance = Unit.getListUnitType(this, RESISTANCE);
        String[] power = Unit.getListUnitType(this, POWER);

        Spinner firstSpinner = (Spinner) findViewById(R.id.spinner_first_value);
        Spinner secondSpinner = (Spinner) findViewById(R.id.spinner_second_value);

        ArrayAdapter<String> firstAdapter = null;
        ArrayAdapter<String> secondAdapter = null;

        if (type == POWER || powerActivate) {
            firstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, power);

            String[] v = null;

            if (type == RESISTANCE || radioResistance.isChecked()) {
                v = !switchGeneral.isChecked() ? voltage : current;
            }

            if (type == CURRENT || radioCurrent.isChecked()) {
                v = !switchGeneral.isChecked() ? resistance : voltage;
            }

            if (type == VOLTAGE || radioVoltage.isChecked()) {
                v = !switchGeneral.isChecked() ? current : resistance;
            }

            secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, v);
        } else {
            if (type == VOLTAGE) {
                firstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, current);
                secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, resistance);
            }
            if (type == CURRENT) {
                firstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, voltage);
                secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, resistance);
            }
            if (type == RESISTANCE) {
                firstAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, voltage);
                secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, current);
            }
        }

        firstSpinner.setAdapter(firstAdapter);
        secondSpinner.setAdapter(secondAdapter);

        firstSpinner.setSelection(4);
        secondSpinner.setSelection(4);

        firstSpinner.setOnItemSelectedListener(onItemClickListener);
        secondSpinner.setOnItemSelectedListener(onItemClickListener);

    }

    private void voltageSelected() {
        double current = getValueFromUI(FIRST);
        current *= getUnit(FIRST);
        double resistance = getValueFromUI(SECOND);
        resistance *= getUnit(SECOND);
        double result = Voltage.calculateVoltage(current, resistance);
        double power = getResultPower(VOLTAGE, current, resistance);
        setResultOperation(result, power, VOLTAGE);
    }

    private double getResultPower(int type, double value1, double value2) {
        double power = 0.0;
        if (type == VOLTAGE) {
            power = Power.powerForVoltage(value1, value2);
        } else if (type == CURRENT) {
            power = Power.powerForCurrent(value1, value2);
        } else if (type == RESISTANCE) {
            power = Power.powerForResistance(value1, value2);
        }
        return power;
    }

    private void currentSelected() {
        double voltage = getValueFromUI(FIRST);
        voltage *= getUnit(FIRST);
        double resistance = getValueFromUI(SECOND);
        resistance *= getUnit(SECOND);
        double result = Current.calculateCurrent(voltage, resistance);
        double power = getResultPower(CURRENT, voltage, resistance);
        setResultOperation(result, power, CURRENT);
    }

    private void resistanceSelected() {
        double voltage = getValueFromUI(FIRST);
        voltage *= getUnit(FIRST);
        double current = getValueFromUI(SECOND);
        current *= getUnit(SECOND);
        double result = Resistance.calculateResistance(voltage, current);
        double power = getResultPower(RESISTANCE, voltage, current);
        setResultOperation(result, power, RESISTANCE);
    }

    private void setResultOperation(double resultBasic, double resultPowerData, int type) {
        TextView resultView = (TextView) findViewById(R.id.result);
        TextView resultPower = findViewById(R.id.result_power);
        String r = String.valueOf(resultBasic);

        if (r.equalsIgnoreCase("NaN")) {
            resultView.setText(getText(R.string.infinite));
            resultPower.setText(getString(R.string.infinite));
        } else {
            Unity unity = new Unity(this, resultBasic, type);
            Unity unityPower = new Unity(this, resultPowerData, POWER);
            resultView.setText(String.format("%s %s", FormatValue.roundFormat(unity.getValue()), unity.getUnity()));
            resultPower.setText(String.format("%s %s", FormatValue.roundFormat(unityPower.getValue()), unityPower.getUnity()));
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
        setListTypeSpinner(VOLTAGE);
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
        return Unit.notation()[unit];
    }

    private void calculate() {

        if (radioVoltage.isChecked()) {
            if (!powerActivate) voltageSelected();
            else powerSelected(VOLTAGE);
        } else if (radioCurrent.isChecked()) {
            if (!powerActivate) currentSelected();
            else powerSelected(CURRENT);
        } else if (radioResistance.isChecked()) {
            if (!powerActivate) resistanceSelected();
            else powerSelected(RESISTANCE);
        }
    }

    private void powerSelected(int type) {
        double power = getValueFromUI(FIRST);
        power *= getUnit(FIRST);
        double secondValue = getValueFromUI(SECOND);
        secondValue *= getUnit(SECOND);
        double result = 0.0;
        if (type == VOLTAGE) {
            result = !switchGeneral.isChecked() ?
                    Voltage.calculateVoltagePowerCurrent(power, secondValue) :
                    Voltage.calculateVoltagePowerResitance(power, secondValue);
        }
        if (type == CURRENT) {
            result = !switchGeneral.isChecked() ?
                    Current.calculateCurrentPowerResistance(power, secondValue) :
                    Current.calculateCurrentPowerVoltage(power, secondValue);
        }
        if (type == RESISTANCE) {
            result = !switchGeneral.isChecked() ?
                    Resistance.calculateResitancePowerVoltage(power, secondValue) :
                    Resistance.calculateResitancePowerCurrent(power, secondValue);
        }
        setResultOperation(result, 0, type);
    }

    private void loadNameSwitchGeneral() {
        if (switchPower.isChecked()) {
            int value = 0;
            if (radioVoltage.isChecked() && switchPower.isChecked()) {
                value = switchGeneral.isChecked() ? R.string.current : R.string.resistance;
            }
            if (radioCurrent.isChecked() && switchPower.isChecked()) {
                value = switchGeneral.isChecked() ? R.string.resistance : R.string.voltage;
            }
            if (radioResistance.isChecked() && switchPower.isChecked()) {
                value = switchGeneral.isChecked() ? R.string.voltage : R.string.current;
            }

            ((Switch) findViewById(R.id.switch_general)).setText(value);
        }
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
