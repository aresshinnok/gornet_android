package com.alinistratescu.android.gornet.db.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Alin on 5/26/2015.
 */
@DatabaseTable(tableName = "locations")
public class StoreLocationModel implements Parcelable {

    public StoreLocationModel(){

    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;

    @Expose
    @DatabaseField
    @SerializedName("Titlu")
    private String titlu;

    @Expose
    @DatabaseField
    @SerializedName("Oras")
    private String oras;

    @Expose
    @DatabaseField
    @SerializedName("Adresa")
    private String adresa;

    @Expose
    @DatabaseField
    @SerializedName("Latitudine")
    private Double latitudine;

    @Expose
    @DatabaseField
    @SerializedName("Longitudine")
    private Double longitudine;

    @DatabaseField
    private int distance;

    public int getId() {
        return id;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getTitlu() {
        return titlu;
    }

    public int getDistance() {
        return distance;
    }

    public String getOras() {
        return oras;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    protected StoreLocationModel(Parcel in) {
        titlu = in.readString();
        oras = in.readString();
        adresa = in.readString();
        latitudine = in.readByte() == 0x00 ? null : in.readDouble();
        longitudine = in.readByte() == 0x00 ? null : in.readDouble();
        distance = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titlu);
        dest.writeString(oras);
        dest.writeString(adresa);
        if (latitudine == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitudine);
        }
        if (longitudine == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitudine);
        }

        dest.writeInt(distance);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StoreLocationModel> CREATOR = new Parcelable.Creator<StoreLocationModel>() {
        @Override
        public StoreLocationModel createFromParcel(Parcel in) {
            return new StoreLocationModel(in);
        }

        @Override
        public StoreLocationModel[] newArray(int size) {
            return new StoreLocationModel[size];
        }
    };
}