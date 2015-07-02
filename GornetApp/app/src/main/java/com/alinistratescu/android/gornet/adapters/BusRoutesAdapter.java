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
import com.alinistratescu.android.gornet.db.models.BusTransportModel;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;

import java.util.ArrayList;

/**
 * Created by alinistratescu on 7/2/15.
 */
public class BusRoutesAdapter extends RecyclerView.Adapter<BusRoutesAdapter.ViewHolder> {

    private ArrayList<BusTransportModel> transportTimes;
    private Context context;
    ViewHolder.RecycleViewItemListener listener;

    public BusRoutesAdapter(Context context, ArrayList<BusTransportModel> transportTimes, ViewHolder.RecycleViewItemListener listener) {
        this.context = context;
        this.transportTimes = transportTimes;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_bus_transport_item, parent, false);
        ViewHolder vh = new ViewHolder(v, listener);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvPlecare.setText(transportTimes.get(position).getPlecare());
        holder.tvSosire.setText(transportTimes.get(position).getSosire());
        holder.tvDurata.setText(transportTimes.get(position).getDurata()+" min");
    }

    @Override
    public int getItemCount() {
        return transportTimes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvPlecare;
        public TextView tvSosire;
        public TextView tvDurata;
        public RecycleViewItemListener mListener;

        public static interface RecycleViewItemListener {
            public void onRecycleItemClick(View caller, int position);
        }

        public ViewHolder(View v, RecycleViewItemListener listener) {
            super(v);
            this.mListener = listener;

            tvPlecare = (TextView) v.findViewById(R.id.tvPlecare);
            tvSosire = (TextView) v.findViewById(R.id.tvSosire);
            tvDurata = (TextView) v.findViewById(R.id.tvDurata);



            //rlNearLocation.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onRecycleItemClick(v, getAdapterPosition());
        }


    }
}
