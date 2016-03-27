package com.example.wangwilliam.floatengine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

/**
 * Created by wangwilliam on 3/5/16.
 */
public class EngineSettingsActivity extends AppCompatActivity{
    private EngineSettings engineSettings = null;
    private EditText editTextPositionX = null;
    private EditText editTextPositionY = null;
    private EditText editTextItemSeparator = null;
    private EditText editTextPriceSeparator = null;
    private ToggleButton toggleButtonSimpleMode = null;
    private EditText editTextInterval = null;
    private EditText editTextFloatList = null;
    private EditText editTextChartList = null;
    private CheckBox cbxMiute = null;
    private CheckBox cbxDay90 = null;
    private CheckBox cbxDay180 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnExit = (Button)findViewById(R.id.btn_exit);
        Button btnSave = (Button)findViewById(R.id.btn_save);
        Button btnReset = (Button)findViewById(R.id.btn_reset);
        editTextPositionX = (EditText)findViewById(R.id.editText_posX);
        editTextPositionY = (EditText)findViewById(R.id.editText_posY);
        editTextItemSeparator = (EditText)findViewById(R.id.editText_itemSep);
        editTextPriceSeparator = (EditText)findViewById(R.id.editText_priceSep);
        toggleButtonSimpleMode = (ToggleButton)findViewById(R.id.toggleButton_simpleMode);
        editTextInterval = (EditText)findViewById(R.id.editText_interval);
        editTextFloatList = (EditText)findViewById(R.id.editText_floatList);
        editTextChartList = (EditText)findViewById(R.id.editText_chartList);
        cbxMiute = (CheckBox)findViewById(R.id.cbx_minute);
        cbxDay90 = (CheckBox)findViewById(R.id.cbx_day90);
        cbxDay180 = (CheckBox)findViewById(R.id.cbx_day180);

        final SharedPreferences sp = getSharedPreferences(EngineConstants.spFileName, MODE_PRIVATE);
        engineSettings = new EngineSettings(sp);
        engineSettings.Load();
        UpdateUI();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrieveDataFromeUI();
                engineSettings.Save();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engineSettings.Reset();
                UpdateUI();
            }
        });
    }

    private void UpdateUI() {
        if (engineSettings != null) {
            editTextPositionX.setText(String.valueOf(engineSettings.positionX));
            editTextPositionY.setText(String.valueOf(engineSettings.positionY));
            editTextItemSeparator.setText(engineSettings.itemSeparator);
            editTextPriceSeparator.setText(engineSettings.priceSeparator);
            toggleButtonSimpleMode.setChecked(engineSettings.simpleMode);
            editTextInterval.setText(String.valueOf(engineSettings.interval));
            editTextFloatList.setText(engineSettings.floatList);
            editTextChartList.setText(engineSettings.chartList);
            cbxMiute.setChecked(engineSettings.chartMinute);
            cbxDay90.setChecked(engineSettings.charDay90);
            cbxDay180.setChecked(engineSettings.charDay180);
        }
    }

    private void RetrieveDataFromeUI() {
        if (engineSettings != null) {
            engineSettings.positionX = Integer.parseInt(editTextPositionX.getText().toString());
            engineSettings.positionY = Integer.parseInt(editTextPositionY.getText().toString());
            engineSettings.itemSeparator = editTextItemSeparator.getText().toString();
            engineSettings.priceSeparator = editTextPriceSeparator.getText().toString();
            engineSettings.simpleMode = toggleButtonSimpleMode.isChecked();
            engineSettings.interval = Integer.parseInt(editTextInterval.getText().toString());
            engineSettings.floatList = editTextFloatList.getText().toString();
            engineSettings.chartList = editTextChartList.getText().toString();
            engineSettings.chartMinute = cbxMiute.isChecked();
            engineSettings.charDay90 = cbxDay90.isChecked();
            engineSettings.charDay180 = cbxDay180.isChecked();
        }
    }
}

