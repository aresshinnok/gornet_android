package com.alinistratescu.android.gornet.db;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import android.content.Context;

import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Singleton database manager class providing the database connection helper.
 */
public class DatabaseManager {

    /**
     * The context to use
     */
    private Context context;

    /**
     * Static instance of this class
     */
    private static DatabaseManager sInstance;

    /**
     * SQLite database open helper to help manage when the application needs to access
     * database tables for CRUD operations.
     */
    private DatabaseHelper helper;

    /**
     * Create an instance of this manager
     *
     * @param context the context to use
     * @return an instance of this class
     */
    public static DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    /**
     * Constructor
     *
     * @param context the context to use
     */
    private DatabaseManager(Context context) {
        this.context = context;
        this.helper = new DatabaseHelper(context);
    }

    /**
     * Getters
     */

    public DatabaseHelper getHelper() {
        return this.helper;
    }

    /**
     * Table utils
     */

    public void clearTable(Class<?> dataClass) {
        try {
            TableUtils.clearTable(getHelper().getConnectionSource(), dataClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
