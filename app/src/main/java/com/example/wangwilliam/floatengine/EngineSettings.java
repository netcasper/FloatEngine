package com.example.wangwilliam.floatengine;

import android.content.SharedPreferences;

/**
 * Created by wangwilliam on 3/6/16.
 */
public class EngineSettings {
    private SharedPreferences sp = null;

    public int positionX = EngineConstants.defaultPositionX;
    public int positionY = EngineConstants.defaultPositionY;
    public String itemSeparator = EngineConstants.defaultItemSeparartor;
    public String priceSeparator = EngineConstants.defaultPriceSeparator;
    public boolean simpleMode = EngineConstants.defaultSimpleMode;
    public int interval = EngineConstants.defaultInterval;
    public String floatList = EngineConstants.defaultFloatList;
    public String chartList = EngineConstants.defaultChartList;
    public boolean chartMinute = EngineConstants.defaultChartMinute;
    public boolean charDay90 = EngineConstants.defaultChartDay90;
    public boolean charDay180 = EngineConstants.defaultChartDay180;

    public EngineSettings(SharedPreferences sp){
        this.sp = sp;
    }

    public void Load(){
        if (sp != null) {
            positionX = sp.getInt(EngineConstants.keyPositionX, EngineConstants.defaultPositionX);
            positionY = sp.getInt(EngineConstants.keyPositionY, EngineConstants.defaultPositionY);
            itemSeparator = sp.getString(EngineConstants.keyItemSeparator, EngineConstants.defaultItemSeparartor);
            priceSeparator = sp.getString(EngineConstants.keyPriceSeparator, EngineConstants.defaultItemSeparartor);
            simpleMode = sp.getBoolean(EngineConstants.keySimpleMode, EngineConstants.defaultSimpleMode);
            interval = sp.getInt(EngineConstants.keyInterval, EngineConstants.defaultInterval);
            floatList = sp.getString(EngineConstants.keyFloatList, EngineConstants.defaultFloatList);
            chartList = sp.getString(EngineConstants.keyChartList, EngineConstants.defaultChartList);
            chartMinute = sp.getBoolean(EngineConstants.keyChartMinute, EngineConstants.defaultChartMinute);
            charDay90 = sp.getBoolean(EngineConstants.keyChartDay90, EngineConstants.defaultChartDay90);
            charDay180 = sp.getBoolean(EngineConstants.keyChartDay180, EngineConstants.defaultChartDay180);
        }
    }

    public void Save(){
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(EngineConstants.keyPositionX, positionX);
            editor.putInt(EngineConstants.keyPositionY, positionY);
            editor.putString(EngineConstants.keyItemSeparator, itemSeparator);
            editor.putString(EngineConstants.keyPriceSeparator, priceSeparator);
            editor.putBoolean(EngineConstants.keySimpleMode, simpleMode);
            editor.putInt(EngineConstants.keyInterval, interval);
            editor.putString(EngineConstants.keyFloatList, floatList);
            editor.putString(EngineConstants.keyChartList, chartList);
            editor.putBoolean(EngineConstants.keyChartMinute, chartMinute);
            editor.putBoolean(EngineConstants.keyChartDay90, charDay90);
            editor.putBoolean(EngineConstants.keyChartDay180, charDay180);
            editor.commit();
        }
    }

    public void Reset() {
        positionX = EngineConstants.defaultPositionX;
        positionY = EngineConstants.defaultPositionY;
        itemSeparator = EngineConstants.defaultItemSeparartor;
        priceSeparator = EngineConstants.defaultPriceSeparator;
        simpleMode = EngineConstants.defaultSimpleMode;
        interval = EngineConstants.defaultInterval;
        floatList = EngineConstants.defaultFloatList;
        chartList = EngineConstants.defaultChartList;
        chartMinute = EngineConstants.defaultChartMinute;
        charDay90 = EngineConstants.defaultChartDay90;
        charDay180 = EngineConstants.defaultChartDay180;
    }
}
