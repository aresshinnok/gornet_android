package com.alinistratescu.android.gornet.db.models;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by alinistratescu on 7/2/15.
 */
public class BusTransportModel {

    @Expose
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;

    @Expose
    @DatabaseField
    private String plecare;

    @Expose
    @DatabaseField
    private String durata;

    @Expose
    @DatabaseField
    private String sosire;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlecare() {
        return plecare;
    }

    public void setPlecare(String plecare) {
        this.plecare = plecare;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getSosire() {
        return sosire;
    }

    public void setSosire(String sosire) {
        this.sosire = sosire;
    }
}
