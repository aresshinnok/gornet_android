package com.alinistratescu.android.gornet.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.base.BaseFragment;
import com.alinistratescu.android.gornet.utils.Constants;

/**
 * Created by Alin on 5/25/2015.
 */
public class ContactFragmentMenu extends BaseFragment {

    private TextView tvTelFax;
    private TextView tvEmail;
    private TextView tvCallCenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTelFax = (TextView) view.findViewById(R.id.tvTelFax);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvCallCenter = (TextView) view.findViewById(R.id.tvCallCenter);

        tvTelFax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + Constants.CONTACT_PHONE_NUMBER.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.CONTACT_EMAIL});
                intent.putExtra(Intent.EXTRA_SUBJECT, Constants.CONTACT_SUBJECT);
                intent.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        tvCallCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + Constants.CONTACT_CALL_CENTER_PHONE_NUMBER.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getSectionNumber() {
        return 0;
    }

    @Override
    public String getGAScreenName() {
        return "contact";
    }
}
