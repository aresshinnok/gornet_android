package com.alinistratescu.android.gornet.utils;

import com.alinistratescu.android.gornet.BuildConfig;

/**
 * Created by Alin on 5/25/2015.
 */
public class Constants {
    // Package name
    public static final String BASE_PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    public static final int MENU_COUNT = 4;

    public static final int MENU_IDX_SERVICES = 0;
    public static final int MENU_IDX_NEAR_LOCATIONS = 1;
    public static final int MENU_IDX_MAP = 2;
    public static final int MENU_IDX_CONTACT = 3;

    // Application log tag
    public static final String TAG = "ZEBRA";
    // Application log tag for Google Analytics
    public static final String GA_TAG = TAG + ".GA";

    public static final String CONTACT_PHONE_NUMBER = "0314329996";
    public static final String CONTACT_CALL_CENTER_PHONE_NUMBER = "0314394099";
    public static final String CONTACT_EMAIL = "contact@zebrapay.ro";
    public static final String CONTACT_SUBJECT = "Solicitare contact";

    public static final Logger.ENVIRONMENT logEnvironment = Logger.ENVIRONMENT.DEVELOPMENT;

    public static final String PREFS_BASE = "ZEBRA_PAY";

    public static final int TIME_BETWEEN_CLICKS = 2000;

    public static final String PREFS_SETTINGS_LAST_TITLE = "LAST_TITLE";

    public static final double BUCHAREST_FAKE_KM_0_LATITUDE = 44.436415;
    public static final double BUCHAREST_FAKE_KM_0_LONGITUDE = 26.102482;
    public static final int DEFAULT_ZOOM_LEVEL = 15;
    public static final int CUSTOM_ZOOM_LEVEL = 7;

    public static final String ARG_NEW_REQUEST_CLIENT_LOCATION = BASE_PACKAGE_NAME + ".ARG_NEW_REQUEST_CLIENT_LOCATION";

    public static final String PREFS_SETTINGS_DOWNLOADED_DB_TIME = BASE_PACKAGE_NAME + ".PREFS_SETTINGS_DOWNLOADED_DB_TIME";

    public static final long ONE_DAY_TIME_IN_MILIS = 24 * 60 * 60 * 1000;

    public static final String LOCATION_URL = "http://";
    public static final String FEED_URL = "https://";
    public static final String BASE_PICTURE_URL = "https://legacymobileapp.zebrapay.ro/";
}
