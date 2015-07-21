package com.alinistratescu.android.gornet.utils;

import com.alinistratescu.android.gornet.BuildConfig;

/**
 * Created by Alin on 5/25/2015.
 */
public class Constants {
    // Package name
    public static final String BASE_PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    public static final int MENU_COUNT = 16;

    public static final int MENU_IDX_INFO = 0;
    public static final int MENU_IDX_NOTIFICATION = 1;
    public static final int MENU_IDX_NEWS = 2;
    public static final int MENU_IDX_BUY_SELL = 3;
    public static final int MENU_IDX_MAP = 4;
    public static final int MENU_IDX_PRODUCT_OF_THE_DAY = 5;
    public static final int MENU_IDX_EVENTS = 6;
    public static final int MENU_IDX_GALLERY = 7;
    public static final int MENU_IDX_OTHER_GORNET = 8;
    public static final int MENU_IDX_CONTACT = 9;
    public static final int MENU_IDX_PLACE_TO_SEE = 10;
    public static final int MENU_IDX_SOCCER_TEAM = 11;
    public static final int MENU_IDX_WEATHER = 12;
    public static final int MENU_IDX_TRANSPORT = 13;
    public static final int MENU_IDX_HOLLYDAY = 14;
    public static final int MENU_IDX_PROFILE = 15;

    // Application log tag
    public static final String TAG = "GORNET";
    // Application log tag for Google Analytics
    public static final String GA_TAG = TAG + ".GA";

    public static final String CONTACT_PHONE_NUMBER = "0314329996";
    public static final String CONTACT_CALL_CENTER_PHONE_NUMBER = "0314394099";
    public static final String CONTACT_EMAIL = "alinistratescu@gmail.com";
    public static final String CONTACT_SUBJECT = "Solicitare contact";

    public static final Logger.ENVIRONMENT logEnvironment = Logger.ENVIRONMENT.DEVELOPMENT;

    public static final String PREFS_BASE = "GORNET_PREF";

    public static final int TIME_BETWEEN_CLICKS = 2000;

    public static final String PREFS_SETTINGS_LAST_TITLE = "LAST_TITLE";

    public static final double BUCHAREST_FAKE_KM_0_LATITUDE = 45.118625;
    public static final double BUCHAREST_FAKE_KM_0_LONGITUDE = 26.078104;
    public static final int DEFAULT_ZOOM_LEVEL = 15;
    public static final int CUSTOM_ZOOM_LEVEL = 7;

    public static final String ARG_NEW_REQUEST_CLIENT_LOCATION = BASE_PACKAGE_NAME + ".ARG_NEW_REQUEST_CLIENT_LOCATION";

    public static final String PREFS_SETTINGS_DOWNLOADED_DB_TIME = BASE_PACKAGE_NAME + ".PREFS_SETTINGS_DOWNLOADED_DB_TIME";

    public static final long ONE_DAY_TIME_IN_MILIS = 24 * 60 * 60 * 1000;

    public static final String LOCATION_URL = "http://alinistratescu.com";
    public static final String FEED_URL = "https://";
    public static final String BASE_PICTURE_URL = "http://alinistratescu.com/";

    public static final String TRANSPORT_URL_INTORS = "http://www.autogari.ro/Transport/Ploiesti-Gornet";
    public static final String TRANSPORT_URL_DUS = "http://www.autogari.ro/Transport/Gornet-Ploiesti";
}
