package com.alinistratescu.android.gornet.rest;

/**
 * Created by Alin Istratescu on 23.03.2015.
 */

import com.alinistratescu.android.gornet.db.models.StoreLocationModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Interface defining API requests accordingly with Retrofit library specs
 */
public interface ApiService {

    @GET("/locations.json")
    public void getTerminalsLocations(Callback <List<StoreLocationModel>> response);
}
