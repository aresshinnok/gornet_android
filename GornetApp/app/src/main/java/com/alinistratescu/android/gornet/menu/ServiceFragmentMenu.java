package com.alinistratescu.android.gornet.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.adapters.ImageAdapter;
import com.alinistratescu.android.gornet.base.BaseFragment;
import com.alinistratescu.android.gornet.db.DatabaseUtils;
import com.alinistratescu.android.gornet.db.models.FeedItemModel;
import com.alinistratescu.android.gornet.fragments.ServiceDialogAlert;
import com.alinistratescu.android.gornet.utils.Constants;
import com.alinistratescu.android.gornet.utils.MockJson;
import com.alinistratescu.android.gornet.views.GridViewWithHeaderAndFooter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Alin on 5/25/2015.
 */
public class ServiceFragmentMenu extends BaseFragment implements ImageAdapter.GridClickListener {


    GridViewWithHeaderAndFooter gridItems;
    ArrayList<FeedItemModel> feeds;
    ImageAdapter imageAdapter;

    @Override
    protected int getSectionNumber() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridItems = (GridViewWithHeaderAndFooter) view.findViewById(R.id.gridItems);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View headerView = layoutInflater.inflate(R.layout.services_header_view, null);
        gridItems.addHeaderView(headerView);

        if (getActivity().getSharedPreferences(Constants.PREFS_BASE, 0).getLong(Constants.PREFS_SETTINGS_DOWNLOADED_DB_TIME, 0L) + Constants.ONE_DAY_TIME_IN_MILIS < System.currentTimeMillis()) {
            getAllServices();
        }
        feeds = new ArrayList<>(DatabaseUtils.getFeedList(getActivity()));
        imageAdapter = new ImageAdapter(getActivity(), feeds, this);


        gridItems.setAdapter(imageAdapter);
        gridItems.smoothScrollToPosition(0);


        //populates database


    }

    public void populateOrUpdateDB() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation() // every field used by GSON must have the @Expose annotation
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        ArrayList<FeedItemModel> feedss = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(MockJson.mockJS);
            for (int i = 0; i < jsonArray.length(); i++) {
                feedss.add(gson.fromJson(jsonArray.optJSONObject(i).toString(), FeedItemModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < feedss.size(); i++) {
            DatabaseUtils.saveFeedItem(getActivity(), feedss.get(i));
        }


        Log.d("TAG", feedss.size() + "");
    }

    public void getAllServices() {

    }

    @Override
    public void onGridItemClick(int position) {

        ServiceDialogAlert serviceDialogAlert = new ServiceDialogAlert();
        Bundle bundle = new Bundle();
        bundle.putString("title", feeds.get(position).getTitle());
        bundle.putString("message", feeds.get(position).getDescription());
        serviceDialogAlert.setArguments(bundle);
        serviceDialogAlert.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public String getGAScreenName() {
        return "servicii";
    }
}
