package com.alinistratescu.android.gornet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;

import java.util.ArrayList;

/**
 * Created by Alin on 5/27/2015.
 */
public class NearLocationsAdapter extends RecyclerView.Adapter<NearLocationsAdapter.ViewHolder> {

    private ArrayList<StoreLocationModel> storeLocations;
    private Context context;
    ViewHolder.RecycleViewItemListener listener;

    public NearLocationsAdapter(Context context, ArrayList<StoreLocationModel> locations, ViewHolder.RecycleViewItemListener listener) {
        this.context = context;
        this.storeLocations = locations;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_near_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v, listener);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.llDistance.setVisibility(View.VISIBLE);
        holder.tvTitleLocation.setText(storeLocations.get(position).getTitlu());
        holder.tvDetailsLocation.setText(storeLocations.get(position).getOras() +" "+storeLocations.get(position).getAdresa());
        holder.tvDistance.setText(String.valueOf(storeLocations.get(position).getDistance()));

        if (storeLocations.get(position).getDistance() == -1){
            holder.llDistance.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return storeLocations.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvTitleLocation;
        public TextView tvDetailsLocation;
        public TextView tvDistance;
        public RelativeLayout rlNearLocation;
        public RecycleViewItemListener mListener;
        public LinearLayout llDistance;

        public static interface RecycleViewItemListener {
            public void onRecycleItemClick(View caller, int position);
        }

        public ViewHolder(View v, RecycleViewItemListener listener) {
            super(v);
            this.mListener = listener;

            tvTitleLocation = (TextView) v.findViewById(R.id.tvTitleLocation);
            tvDetailsLocation = (TextView) v.findViewById(R.id.tvDetailsLocation);
            tvDistance = (TextView) v.findViewById(R.id.tvDistance);

            rlNearLocation = (RelativeLayout) v.findViewById(R.id.rlNearLocation);

            llDistance = (LinearLayout) v.findViewById(R.id.llDistance);

            rlNearLocation.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onRecycleItemClick(v, getAdapterPosition());
        }


    }
}
