package com.alinistratescu.android.gornet.activitydelegates.progress;

/**
 * Created by Catalin Matusa on 24.03.2015.
 */

import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alinistratescu.android.gornet.R;


/**
 * A DialogFragment containing a progress view
 */
public class ProgressDialogFragment extends DialogFragment {

    /**
     * TAG
     */
    public static final String FRAGMENT_TAG = "progress_dialog_fragment_tag";

    /**
     * Arguments keys
     */
    private static final String ARG_PROGRESS_LAYOUT_RESOURCE = "arg_progress_layout_resource";
    private static final String ARG_PROGRESS_COLOR_RESOURCE = "arg_progress_color_resource";
    private static final String ARG_DIM_BACKGROUND = "arg_dim_background";

    private RelativeLayout background;
    private ProgressBar progressBar;

    /**
     * Constructor
     */
    public ProgressDialogFragment() {

    }

    /**
     * Static method for creating instances
     *
     * @param progressLayoutResource the progress layout to be displayed on the screen.<br/>
     *                               The progress layout MUST CONTAIN at least the following views:<br/>
     *                               - A RelativeLayout having android:id="@+id/background" as the root element of the layout file
     *                               - A ProgressBar having android:id="@+id/progressBar" as the progress to be displayed on the screen
     * @param progressColorResource the color of the progress bar
     * @return an instance of this fragment
     */
    public static ProgressDialogFragment newInstance(Integer progressLayoutResource, Integer progressColorResource) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setArguments(createBundleArgs(progressLayoutResource, progressColorResource));
        return fragment;
    }

    /**
     * Static method for creating instances
     *
     * @param progressLayoutResource the progress layout to be displayed on the screen.<br/>
     *                               The progress layout MUST CONTAIN at least the following views:<br/>
     *                               - A RelativeLayout having android:id="@+id/background" as the root element of the layout file
     *                               - A ProgressBar having android:id="@+id/progressBar" as the progress to be displayed on the screen
     * @param progressColorResource the color of the progress bar
     * @param dimBackground flag indicating if the background should be dimmed or not
     * @return an instance of this fragment
     */
    public static ProgressDialogFragment newInstance(Integer progressLayoutResource, Integer progressColorResource, boolean dimBackground) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        fragment.setArguments(createBundleArgs(progressLayoutResource, progressColorResource, dimBackground));
        return fragment;
    }

    /**
     * Create a {@link Bundle} containing data given as parameter, to be set as arguments for this fragment
     *
     * @param progressLayoutResource the progress layout to be displayed on the screen.<br/>
     *                               The progress layout MUST CONTAIN at least the following views:<br/>
     *                               - A RelativeLayout having android:id="@+id/background" as the root element of the layout file
     *                               - A ProgressBar having android:id="@+id/progressBar" as the progress to be displayed on the screen
     * @param progressColorResource the color of the progress bar
     * @return a {@link Bundle} to be used as arguments for this fragment
     */
    private static Bundle createBundleArgs(Integer progressLayoutResource, Integer progressColorResource) {
        Bundle args = new Bundle();

        if (progressLayoutResource != null) {
            args.putInt(ARG_PROGRESS_LAYOUT_RESOURCE, progressLayoutResource);
        }

        if (progressColorResource != null) {
            args.putInt(ARG_PROGRESS_COLOR_RESOURCE, progressColorResource);
        }

        return args;
    }

    /**
     * Create a bundle containing data given as parameter, to be set as arguments for this fragment
     *
     * @param progressLayoutResource the progress layout to be displayed on the screen.<br/>
     *                               The progress layout MUST CONTAIN at least the following views:<br/>
     *                               - A RelativeLayout having android:id="@+id/background" as the root element of the layout file
     *                               - A ProgressBar having android:id="@+id/progressBar" as the progress to be displayed on the screen
     * @param progressColorResource the color of the progress bar
     * @param dimBackground flag indicating if the background should be dimmed or not
     * @return  a {@link Bundle} to be used as arguments for this fragment
     */
    private static Bundle createBundleArgs(Integer progressLayoutResource, Integer progressColorResource, boolean dimBackground) {
        Bundle args = createBundleArgs(progressLayoutResource, progressColorResource);
        args.putBoolean(ARG_DIM_BACKGROUND, dimBackground);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DarkActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().containsKey(ARG_PROGRESS_LAYOUT_RESOURCE)) {
            return inflater.inflate(getArguments().getInt(ARG_PROGRESS_LAYOUT_RESOURCE), container, false);
        }

        return inflater.inflate(R.layout.dialog_fragment_indeterminate_circular_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // adjust background dimmed state
        background = (RelativeLayout) view.findViewById(R.id.background);
        try {
            background.setBackgroundColor(getResources().getColor(getArguments().getBoolean(ARG_DIM_BACKGROUND, true) ? R.color.progress_delegate_background : android.R.color.transparent));
        } catch (NullPointerException e) {
            background.setBackgroundColor(getResources().getColor(R.color.progress_delegate_background));
        }

        // set progress color
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ColorFilter cf = new LightingColorFilter(
                getResources().getColor(android.R.color.black),
                getResources().getColor(getArguments() != null && getArguments().containsKey(ARG_PROGRESS_COLOR_RESOURCE)
                        ? getArguments().getInt(ARG_PROGRESS_COLOR_RESOURCE)
                        : android.R.color.white));
        progressBar.getIndeterminateDrawable().setColorFilter(cf);

        setCancelable(false);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }
}
