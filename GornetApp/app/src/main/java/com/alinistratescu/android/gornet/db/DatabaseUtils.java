package com.alinistratescu.android.gornet.db;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import com.j256.ormlite.stmt.QueryBuilder;
import com.alinistratescu.android.gornet.db.models.FeedItemModel;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;

import java.util.List;


/**
 * Abstract class intended to contain only methods to access / modify database tables content.
 */
public abstract class DatabaseUtils {


    // save location
    public static void saveStoreLocation(Context context, StoreLocationModel store) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        try {
            helper.getStoreLocationDao().createOrUpdate(store);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

//    // save location
//    public static void updateStoreLocation(Context context, StoreLocationModel store) {
//        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();
//
//        try {
//            QueryBuilder<StoreLocationModel, Integer> locationQueryBuilder = helper.getStoreLocationDao().queryBuilder();
//            locationQueryBuilder.where().eq("Titlu", store.getTitlu());
//            helper.getStoreLocationDao().createOrUpdate(store);
//        } catch (SQLiteException e) {
//            e.printStackTrace();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//    }

    // save location
    public static void saveStoresLocations(Context context, List<StoreLocationModel> stores) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        try {
            for (int i=0; i< stores.size(); i++){
                helper.getStoreLocationDao().createOrUpdate(stores.get(i));
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }


    //get location by id
    public static StoreLocationModel getStoreLocation(Context context, int storeId) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        try {
            return helper.getStoreLocationDao().queryForId(storeId);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return null;
    }

    // save feed item
    public static void saveFeedItem(Context context, FeedItemModel feed) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        try {
            helper.getFeedItemDao().createOrUpdate(feed);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

    }


    // get feed item by id
    public static FeedItemModel getFeedItem(Context context, int feedId) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        try {
            return helper.getFeedItemDao().queryForId(feedId);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return null;
    }

    //delete all the feed
    public static void deleteFeed(Context context) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        List<FeedItemModel> feedItems = helper.getFeedItemDao().queryForAll();
        for (FeedItemModel feeditem : feedItems) {
            deleteFeedItem(context, feeditem);
        }
    }

    //delete feed item
    public static void deleteFeedItem(Context context, FeedItemModel feedItemModel) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        helper.getFeedItemDao().deleteById(feedItemModel.getId());
    }

    // returns all the feed
    public static List<FeedItemModel> getFeedList(Context context) {

        final DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        try {
            QueryBuilder<FeedItemModel, Integer> feedItemQueryBuilder = helper.getFeedItemDao().queryBuilder();
            feedItemQueryBuilder.orderBy("sort_order", true);
            // feedItemQueryBuilder.where();
            return feedItemQueryBuilder.query();
        } catch (java.sql.SQLException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    //returns all the locations
    public static List<StoreLocationModel> getLocationsList(Context context) {

        final DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        try {
            QueryBuilder<StoreLocationModel, Integer> locationQueryBuilder = helper.getStoreLocationDao().queryBuilder();
            // feedItemQueryBuilder.where();
            return locationQueryBuilder.query();
        } catch (java.sql.SQLException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //delete location
    public static void deleteLocationItem(Context context, StoreLocationModel storeLocationModel) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        helper.getStoreLocationDao().deleteById(storeLocationModel.getId());
    }

    //delete all the feed
    public static void deleteLocations(Context context) {
        DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();

        List<StoreLocationModel> locationList = helper.getStoreLocationDao().queryForAll();
        for (StoreLocationModel location : locationList) {
            deleteLocationItem(context, location);
        }
    }
}
