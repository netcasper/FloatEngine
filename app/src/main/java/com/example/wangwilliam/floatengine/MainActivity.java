package com.example.wangwilliam.floatengine;

import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerTabStrip;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private SharedPreferences sp = null;
    private EngineSettings engineSettings = null;
    private SharedPreferences.OnSharedPreferenceChangeListener spListener = null;

    ChartListFragment fragmentMin = null;
    ChartListFragment fragmentDay90 = null;
    ChartListFragment fragmentDay180 = null;
    private List<ChartListFragment> fragmentList = new ArrayList<ChartListFragment>();
    private ChartListFragmentPagerAdapter fragmentVPA = null;
    private ViewPager vp = null;
    private List<String> titleList = new ArrayList<String>();
    private PagerTabStrip pagerTabStrip = null;
    private boolean mChartMinute = EngineConstants.defaultChartMinute;
    private boolean mChartDay90 = EngineConstants.defaultChartDay90;
    private boolean mChartDay180 = EngineConstants.defaultChartDay180;
    private String mChartList = EngineConstants.defaultChartList;
    private Bitmap mBitmapEmpty = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button)findViewById(R.id.btn_start);
        final Button btnStop = (Button)findViewById(R.id.btn_stop);
        final Button btnSettings = (Button)findViewById(R.id.btn_settings);
        final Button btnRefresh = (Button)findViewById(R.id.btn_refresh);
        final Button btnClear = (Button)findViewById(R.id.btn_clear);

        // shared preferences
        sp = getSharedPreferences(EngineConstants.spFileName, MODE_PRIVATE);
        spListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                engineSettings.Load();
                if (mChartMinute != engineSettings.chartMinute
                        || mChartDay90 != engineSettings.charDay90
                        || mChartDay180 != engineSettings.charDay180
                        || mChartList.compareTo(engineSettings.chartList) != 0){
                    clearAll();
                    initData();
                }
            }
        };
        sp.registerOnSharedPreferenceChangeListener(spListener);
        engineSettings = new EngineSettings(sp);
        engineSettings.Load();

        mBitmapEmpty = BitmapFactory.decodeResource(getResources(), R.drawable.empty540x360);
        initData();

       //绑定监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatEngineService.class);
                startService(intent);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatEngineService.class);
                stopService(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EngineSettingsActivity.class);
                startActivity(intent);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked", ((Button) v).getText().toString());
                StringBuilder sb = new StringBuilder();
                String[] charts = engineSettings.chartList.split(" ");
                if (charts.length == 0 || fragmentList.size() == 0) return;

                if (engineSettings.chartMinute) sb.append(ConvertCodeToURL(ChartType.MINUTE, charts));
                if (engineSettings.charDay90) sb.append(ConvertCodeToURL(ChartType.DAY90, charts));
                if (engineSettings.charDay180) sb.append(ConvertCodeToURL(ChartType.DAY180, charts));

                btnRefresh.setEnabled(false);
                HttpPicProvider picProvider = new HttpPicProvider(btnRefresh, fragmentList);
                picProvider.execute(sb.toString().trim());
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked", ((Button) v).getText().toString());
                clearAll();
            }
        });
    }

    private void clearAll() {
        List<ChartItem> chartList = new ArrayList<ChartItem>();
        for (int i=0; i<engineSettings.chartList.trim().split(" ").length; i++) {
            ChartItem item = new ChartItem();
            item.bitmap = mBitmapEmpty;
            chartList.add(item);
        }
        for (int i=0; i<fragmentList.size(); i++) {
            ((ChartListFragment)fragmentList.get(i)).setChartList(chartList);
            ((ChartListFragment)fragmentList.get(i)).Clear();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if (sp != null) {
            sp.unregisterOnSharedPreferenceChangeListener(spListener);
        }
        //Toast.makeText(getApplicationContext(), "onDestroy is called.", Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        // save current settings
        mChartMinute = engineSettings.chartMinute;
        mChartDay90 = engineSettings.charDay90;
        mChartDay180 = engineSettings.charDay180;
        mChartList = engineSettings.chartList;

        // setup ViewPager
        List<ChartItem> chartList = new ArrayList<ChartItem>();
        for (int i=0; i<engineSettings.chartList.trim().split(" ").length; i++) {
            ChartItem item = new ChartItem();
            item.bitmap = mBitmapEmpty;
            chartList.add(item);
        }

        //fragmentMin = new ChartListFragment(ChartType.MINUTE, chartList);
        //fragmentDay90 = new ChartListFragment(ChartType.DAY90, chartList);
        //fragmentDay180 = new ChartListFragment(ChartType.DAY180, chartList);
        fragmentMin = new ChartListFragment();
        fragmentMin.setChartType(ChartType.MINUTE);
        fragmentMin.setChartList(chartList);
        fragmentDay90 = new ChartListFragment();
        fragmentDay90.setChartType(ChartType.DAY90);
        fragmentDay90.setChartList(chartList);
        fragmentDay180 = new ChartListFragment();
        fragmentDay180.setChartType(ChartType.DAY180);
        fragmentDay180.setChartList(chartList);

        titleList = new ArrayList<String>();
        fragmentList = new ArrayList<ChartListFragment>();
        if (engineSettings.chartMinute) {
            titleList.add(EngineConstants.tabTitile_Minute);
            fragmentList.add(fragmentMin);
        }
        if (engineSettings.charDay90) {
            titleList.add(EngineConstants.tabTitle_Day90);
            fragmentList.add(fragmentDay90);
        }
        if (engineSettings.charDay180) {
            titleList.add(EngineConstants.tabTitle_Day180);
            fragmentList.add(fragmentDay180);
        }

        fragmentVPA = new ChartListFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);

        vp = (ViewPager)findViewById(R.id.view_pager);
        vp.setAdapter(fragmentVPA);
        vp.setCurrentItem(0);
    }

    @Override
    public void onPageSelected(int position) {
        vp.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private String ConvertCodeToURL(ChartType chartType, String[] stringList){
        if (stringList == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<stringList.length; i++) {
            if (stringList[i].length() != 6) continue;

            switch (chartType) {
                case MINUTE:
                    sb.append(EngineConstants.http126ChartMinutePrefix);
                    break;
                case DAY90:
                    sb.append(EngineConstants.http126Chart90DayPrefix);
                    break;
                case DAY180:
                    sb.append(EngineConstants.http126Chart180DayPrefix);
                    break;
            }

            if (stringList[i].equals(EngineConstants.szzs)
                    || stringList[i].charAt(0) == '5'
                    || stringList[i].charAt(0) == '6'){
                sb.append('0');
            } else {
                sb.append('1');
            }

            sb.append(stringList[i]);
            sb.append(EngineConstants.http126ChartSuffix);
            sb.append(" "); // separator for each url item
        }

        return sb.toString();
    }
}
