package com.alinistratescu.android.gornet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.db.models.FeedItemModel;
import com.alinistratescu.android.gornet.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alin on 5/25/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<FeedItemModel> picturesList;

    private GridClickListener gridClickListener;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public ImageAdapter(Context context, ArrayList<FeedItemModel> pictures, GridClickListener listener) {
        this.context = context;
        picturesList = pictures;
        gridClickListener = listener;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .resetViewBeforeLoading()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    static class ViewHolder{
    }

    public interface GridClickListener {
        public void onGridItemClick(int position);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.grid_list_item, null);


            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            ImageLoader.getInstance().displayImage(Constants.BASE_PICTURE_URL+picturesList.get(position).getPictureUrl(), imageView, options);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridClickListener.onGridItemClick(position);
                }
            });

             //imageView.setImageResource(mobile);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return picturesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateContent (List<FeedItemModel> updates) {
        picturesList.clear();
        picturesList.addAll(updates);
        this.notifyDataSetChanged();
    }

}
