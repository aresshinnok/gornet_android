package com.alinistratescu.android.gornet.db.models;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Alin on 5/26/2015.
 */
@DatabaseTable(tableName = "feeditems")
public class FeedItemModel {

    @Expose
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;

    @Expose
    @DatabaseField
    private String title;

    @Expose
    @DatabaseField
    private String description;

    @Expose
    @DatabaseField (columnName = "sort_order")
    private int sortOrder;

    @Expose
    @DatabaseField (columnName = "picture_url")
    private String pictureUrl;


    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
