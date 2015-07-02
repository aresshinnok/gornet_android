package com.alinistratescu.android.gornet.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.adapters.BusRoutesAdapter;
import com.alinistratescu.android.gornet.adapters.NearLocationsAdapter;
import com.alinistratescu.android.gornet.base.BaseFragment;
import com.alinistratescu.android.gornet.db.DatabaseUtils;
import com.alinistratescu.android.gornet.db.models.BusTransportModel;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;
import com.alinistratescu.android.gornet.utils.Constants;
import com.alinistratescu.android.gornet.utils.MockJson;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by alinistratescu on 6/30/15.
 */
public class TransportFragment extends Fragment implements BusRoutesAdapter.ViewHolder.RecycleViewItemListener {
    private ArrayList<BusTransportModel> routes = new ArrayList<>();
    RecyclerView recList;
    BusRoutesAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_transport, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);

    }

    private void setupViews(View view) {


        recList = (RecyclerView) view.findViewById(R.id.rvBusRoutes);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        populateDb();
        routes = new ArrayList<>(DatabaseUtils.getRoutesList(getActivity()));

        mAdapter = new BusRoutesAdapter(getActivity(), routes, null);
        recList.setAdapter(mAdapter);

        //getAllLocations();

    }

    @Override
    public void onRecycleItemClick(View caller, int position) {

    }


    public void populateDb() {

        ArrayList<BusTransportModel> routes = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation() // every field used by GSON must have the @Expose annotation
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try {
            JSONArray jsonArray = new JSONArray(MockJson.mockRoutesJs);
            for (int i = 0; i < jsonArray.length(); i++) {
                routes.add(gson.fromJson(jsonArray.optJSONObject(i).toString(), BusTransportModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        DatabaseUtils.saveRoutes(getActivity(), routes);


        Log.d("TAG", routes.size() + "");
    }

}
