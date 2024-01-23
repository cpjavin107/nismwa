package com.javinindia.nismwa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.utility.Utility;


/**
 * Created by Ashish on 02-05-2017.
 */

public class AboutFragment extends BaseFragment implements View.OnClickListener {
    AppCompatTextView txtTitle, txtAbout;
    ImageView imgBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        imgBack =  view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        txtTitle =  view.findViewById(R.id.txtTitle);
        txtTitle.setText("About Us");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtAbout =  view.findViewById(R.id.txtAbout);
        txtAbout.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAbout.setText(Utility.fromHtml("NISMWA is Naraiana Iron & Steel Merchants Welfare aassciation App. This App helps Association Members to collborate with each other and get to know News & Annoucements made by Association. This App also have other useful tools and features.<br><br>NISMWA App is developed by Intechub.com which is a Website & Mobile App development company located in Delhi. "));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.about_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
        }
    }
}
