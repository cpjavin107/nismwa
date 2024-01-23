package com.javinindia.nismwa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.recyclerView.ViewPagerAdapter;

/**
 * Created by ashis on 3/23/2018.
 */

public class BloodTabFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String postId = "";
    AppCompatTextView txtTitle;
    ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        RelativeLayout rlHeader = (RelativeLayout) view.findViewById(R.id.rlHeader);
        rlHeader.setVisibility(View.GONE);
        imgBack =  view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        txtTitle =  view.findViewById(R.id.txtTitle);
        // txtTitle.setText("Management");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        viewPager =  view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout =  view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.quick_job_tab_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DonateBloodFragment(), "Donate Blood");
        adapter.addFragment(new NeedBloodFragment(), "Need Blood");
        adapter.addFragment(new DonnerListFragment(), "Donors List");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }
}
