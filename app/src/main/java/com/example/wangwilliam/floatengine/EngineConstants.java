package com.example.wangwilliam.floatengine;

/**
 * Created by wangwilliam on 3/6/16.
 */
public class EngineConstants {
    // settings
    public static final String spFileName = "engine";
    public static final String keyPositionX = "PositionX";
    public static final String keyPositionY = "PositionY";
    public static final String keyItemSeparator = "ItemSeparator";
    public static final String keyPriceSeparator = "PriceSeparator";
    public static final String keySimpleMode = "SimpleMode";
    public static final String keyInterval = "Interval";
    public static final String keyFloatList = "FloatList";
    public static final String keyChartList = "ChartList";
    public static final String keyChartMinute = "ChartMinute";
    public static final String keyChartDay90 = "ChartDay90";
    public static final String keyChartDay180 = "ChartDay180";

    public static final int defaultPositionX = 200;
    public static final int defaultPositionY = 10;
    public static final String defaultItemSeparartor = "|";
    public static final String defaultPriceSeparator = "/";
    public static final boolean defaultSimpleMode = false;
    public static final int defaultInterval = 1000;
    public static final String defaultFloatList = "159915 000001 510050";
    public static final String defaultChartList = "159915 000001 510050";
    public static final boolean defaultChartMinute = true;
    public static final boolean defaultChartDay90 = true;
    public static final boolean defaultChartDay180 = true;

    // HTTPDataProvider
    public static final String szzs = "000001";
    public static final String cybETF = "159915";
    public static final String sinaURL = "http://hq.sinajs.cn/list=";
    public static final String sinaPrefixShortSH = "s_sh";
    public static final String sinaPrefixShortSZ = "s_sz";
    public static final String sinaPrefixLongSZ = "sz";

    // HttpPicProvider
    public static final String http126ChartMinutePrefix = "http://img1.money.126.net/chart/hs/time/540x360/";
    public static final String http126Chart90DayPrefix = "http://img1.money.126.net/chart/hs/kline/day/90/";
    public static final String http126Chart180DayPrefix = "http://img1.money.126.net/chart/hs/kline/day/180/";
    public static final String http126ChartSuffix = ".png";

    // view pager title
    public static final String tabTitile_Minute = "Minute";
    public static final String tabTitle_Day90 = "Day 90";
    public static final String tabTitle_Day180 = "Day 180";
}
