package com.alinistratescu.android.gornet.db;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.db.models.FeedItemModel;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;

/**
 * SQLite database open helper to help manage when the application needs to create
 * or upgrade its database.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DB_NAME = "zebrapay.db3";
    private static final int DB_VERSION = 1;

    // cache
    private RuntimeExceptionDao<FeedItemModel, Integer> feedItemRuntimeDao;
    private RuntimeExceptionDao<StoreLocationModel, Integer> storeLocationRuntimeDao;




    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, FeedItemModel.class);
            TableUtils.createTable(connectionSource, StoreLocationModel.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // cache
            TableUtils.dropTable(connectionSource, FeedItemModel.class, false);
            TableUtils.dropTable(connectionSource, StoreLocationModel.class, false);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }

    /*
	 * Methods returning the RuntimeExceptionDao (Database Access Object)
	 * version of a Dao for our Model classes. It will create it or just
	 * give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
	 */

    public RuntimeExceptionDao<FeedItemModel, Integer> getFeedItemDao() {
        if (feedItemRuntimeDao == null) {
            feedItemRuntimeDao = getRuntimeExceptionDao(FeedItemModel.class);
        }

        return feedItemRuntimeDao;
    }

    public RuntimeExceptionDao<StoreLocationModel, Integer> getStoreLocationDao() {
        if (storeLocationRuntimeDao == null) {
            storeLocationRuntimeDao = getRuntimeExceptionDao(StoreLocationModel.class);
        }

        return storeLocationRuntimeDao;
    }



    public RuntimeExceptionDao<?, Integer> getDaoByClass(Class<?> daoClass) {
        if (daoClass.equals(FeedItemModel.class)) {
            return getFeedItemDao();
        } else if (daoClass.equals(StoreLocationModel.class)) {
            return getStoreLocationDao();
        }
        else {
            return null;
        }
    }
}
