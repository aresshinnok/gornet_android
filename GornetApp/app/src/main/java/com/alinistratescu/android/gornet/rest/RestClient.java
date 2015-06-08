package com.alinistratescu.android.gornet.rest;

/**
 * Created by Alin on 5/28/2015.
 */

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.alinistratescu.android.gornet.BuildConfig;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Singleton REST client managing the communication with the API.
 */
public class RestClient {

    /**
     * Class static instance
     */
    private static RestClient sInstance;

    /**
     * Retrofit interface for defining API requests
     */
    private ApiService apiService;

    /**
     * Constructor
     */
    private RestClient() {

    }

    /**
     * Static method for instantiating this client
     * @return an instance of this class
     */
    public static RestClient getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RestClient();
            //sInstance.setup(context.getApplicationContext());
        }

        return sInstance;
    }

    /**
     * REST client setup
     */
    private void setup(Context context) {
        /**
         * GSON (JSON library from Google) setup.<br/>
         * The GSON is, by default, used by the Retrofit library, to convert the server
         * response into a JSON.
         */
//        Gson gson = new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .excludeFieldsWithoutExposeAnnotation() // every field used by GSON must have the @Expose annotation
//                .setPrettyPrinting()
//                .serializeNulls()
//                .create();

        /**
         * REST client setup
         */
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setLogLevel(RestAdapter.LogLevel.FULL) // set log level
//                .setEndpoint(BuildConfig.API_URL) // set API URL
//                .setClient(new OkClient(new OkHttpClient())) // Retrofit is used in conjunction with OkHttpClient
//                .setConverter(new GsonConverter(gson)) // attach GSON coverter
//                .setLogLevel(BuildConfig.LOG_ENABLE ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
//                .build();


        ;
        /**
         * Create the implementation of the API defined by the specified service interface.
         */
       // this.apiService = getHostAdapter("", gson).create(ApiService.class);
    }

    /**
     * Getter
     */

    public ApiService getApiService() {
        return this.apiService;
    }

    public RestAdapter getHostAdapter(String baseHost){

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation() // every field used by GSON must have the @Expose annotation
                .setPrettyPrinting()
                .serializeNulls()
                .create();


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(baseHost)
                .setClient(new OkClient(new OkHttpClient())) // Retrofit is used in conjunction with OkHttpClient
                .setConverter(new GsonConverter(gson)) // attach GSON coverter
                .setLogLevel(BuildConfig.LOG_ENABLE ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
        return restAdapter;
    }
}

