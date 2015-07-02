package com.alinistratescu.android.gornet.adapters;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.utils.Constants;


/**
 * Created by Alin on 2/3/2015.
 * used to generate navigation menu
 */
public class NavigationDrawerMenuListAdapter extends BaseAdapter {

    private Context mContext;

    public NavigationDrawerMenuListAdapter(Context ctx) {
        mContext = ctx;
    }

    @Override
    public int getCount() {
        return Constants.MENU_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View vi = convertView;

        if (vi == null) {
            vi = ((LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_list, parent, false);
            vh = new ViewHolder();

            vh.ivMenuIcon = (ImageView) vi.findViewById(R.id.ivMenuIcon);
            vh.tvMenuTitle = (TextView) vi.findViewById(R.id.tvMenuTitle);
            vh.separator = vi.findViewById(R.id.separator);

            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
//        /**
//         * add separator on 3rd view
//         */
//        if (position != Constants.MENU_IDX_NEWS) {
//            vh.separator.setVisibility(View.GONE);
//        } else {
//            vh.separator.setVisibility(View.VISIBLE);
//        }
        vh.separator.setVisibility(View.VISIBLE);
        //vh.ivMenuIcon.setImageResource(mContext.getResources().obtainTypedArray(R.array.nav_drawer_icons).getResourceId(position, -1));
        vh.ivMenuIcon.setVisibility(View.GONE);
//        /**
//         * add images only for first 3 items
//         */
//        if (CommonUtilities.inArray(position, new int[]{Constants.MENU_IDX_ADD_BON, Constants.MENU_IDX_ARCHIVE, Constants.MENU_IDX_NEWS})) {
//            vh.ivMenuIcon.setVisibility(View.VISIBLE);
//            vh.ivMenuIcon.setImageResource(mContext.getResources().obtainTypedArray(R.array.nav_drawer_icons).getResourceId(position, -1));
//        } else {
//            vh.ivMenuIcon.setVisibility(View.GONE);
//        }

        /**
         * add titles for all sections
         */
        vh.tvMenuTitle.setText(mContext.getResources().getStringArray(R.array.menu_titles)[position]);

        return vi;
    }


    public static class ViewHolder {
        ImageView ivMenuIcon;
        TextView tvMenuTitle;
        View separator;
    }
}
