package com.javinindia.nismwa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.constant.Constants;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;

/**
 * Created by Ashish on 23-08-2017.
 */

public class LoginGuestPageFragment extends BaseFragment implements View.OnClickListener {
    AppCompatTextView txtLogin, txtGuest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void clickMethod(View view) {
        txtLogin.setOnClickListener(this);
        txtGuest.setOnClickListener(this);
    }

    private void initialize(View view) {
        txtLogin = view.findViewById(R.id.txtLogin);
        txtLogin.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGuest = view.findViewById(R.id.txtGuest);
        txtGuest.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.guest_login_layout;
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
            case R.id.txtLogin:
                LoginFragment loginFragment = new LoginFragment();
                callFragmentMethod(loginFragment, Constants.FRG_LOGIN, R.id.container);
                break;
            case R.id.txtGuest:
                HomePageFragment homePageFragment = new HomePageFragment();
                callFragmentMethod(homePageFragment, Constants.FRG_MEMBER_LIST, R.id.container);
                break;
        }
    }
}
