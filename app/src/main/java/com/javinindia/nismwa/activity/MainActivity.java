package com.javinindia.nismwa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.nismwa.R;
import com.javinindia.nismwa.constant.Constants;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.fragment.AboutFragment;
import com.javinindia.nismwa.fragment.BaseFragment;
import com.javinindia.nismwa.fragment.ChangePasswordFragment;
import com.javinindia.nismwa.fragment.CheckConnectionFragment;
import com.javinindia.nismwa.fragment.ConversionListFragment;
import com.javinindia.nismwa.fragment.FeedbackFragment;
import com.javinindia.nismwa.fragment.HelpSupportFragment;
import com.javinindia.nismwa.fragment.HomePageFragment;
import com.javinindia.nismwa.fragment.InboxFragment;
import com.javinindia.nismwa.fragment.MemberProfileFragment;
import com.javinindia.nismwa.fragment.MySuggestionFragment;
import com.javinindia.nismwa.fragment.NotificationListFragment;
import com.javinindia.nismwa.fragment.SettingFragment;
import com.javinindia.nismwa.fragment.UsefullFragment;
import com.javinindia.nismwa.modelClasses.NavigationModel;
import com.javinindia.nismwa.modelClasses.membertypeparsing.MemberTypeResponse;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsefullMasterDetail;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsfullMasterResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.ComplaintCategoryAdopter;
import com.javinindia.nismwa.recyclerView.NavigationRecycler;
import com.javinindia.nismwa.recyclerView.UsefullMasterAdopter;
import com.javinindia.nismwa.utility.CheckConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements CheckConnectionFragment.OnCallBackInternetListener, NavigationRecycler.NavItemListener, MemberProfileFragment.OnCallBackProfileViewListener, UsefullMasterAdopter.MyClickListener {
    private RequestQueue requestQueue;
    private NavigationView navigationView;
    private NavigationRecycler navigationAdapter;
    private DrawerLayout drawerLayout;
    private RecyclerView navRecycler;
    private List<NavigationModel> navigationModelList;
    Toolbar tb;
    private FragmentManager mFragmentManager;

    ProgressBar progressBar;
    AlertDialog dialog;
    int width;
    int height;
    UsefullMasterAdopter adopter;

    private TextView textView;
    //  int notiCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        navigationModelList = new ArrayList<>();
        iconAndTitleDataList(navigationModelList);
        initToolbar();
        initView();
        if (CheckConnection.haveNetworkConnection(this)) {
            String username = SharedPreferencesManager.getUserID(getApplicationContext());
            if (!TextUtils.isEmpty(username)) {
                BaseFragment homePageFragment = new HomePageFragment();
                FragmentManager fm = this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                fragmentTransaction.add(R.id.containerMain, homePageFragment);
                fragmentTransaction.addToBackStack(Constants.FRG_HOME_PAGE);
                fragmentTransaction.commit();
                /*if (SharedPreferencesManager.getCount(getApplicationContext()) == 0) {
                    BaseFragment homePageFragment = new HomePageFragment();
                    FragmentManager fm = this.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                    fragmentTransaction.add(R.id.containerMain, homePageFragment);
                    fragmentTransaction.addToBackStack(Constants.FRG_HOME_PAGE);
                    fragmentTransaction.commit();
                } else {
                    BaseFragment notificationListFragment = new NotificationListFragment();
                    FragmentManager fm = this.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
                    fragmentTransaction.add(R.id.containerMain, notificationListFragment);
                    fragmentTransaction.addToBackStack(Constants.FRG_NOTIFICATION);
                    fragmentTransaction.commit();
                }*/

            }
        } else {
            CheckConnectionFragment baseFragment = new CheckConnectionFragment();
            baseFragment.setMyCallBackInternetListener(this);
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
            fragmentTransaction.add(R.id.containerMain, baseFragment);
            fragmentTransaction.addToBackStack(Constants.FRG_CHECK_INTERNET);
            fragmentTransaction.commit();
        }


    }

    public void methodNotifiaction() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.NOTIFICATION_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = null;
                            String msg = null, pass = null;
                            int status = 0;
                            try {
                                jsonObject = new JSONObject(response);
                                if (jsonObject.has("status"))
                                    status = jsonObject.optInt("status");
                                if (jsonObject.has("msg"))
                                    msg = jsonObject.optString("msg");
                                if (status == 1) {
                                    pass = jsonObject.optString("data");
                                    if (pass.equalsIgnoreCase("0")) {
                                        Constants.COUNT = Integer.parseInt(pass);
                                        textView.setVisibility(View.GONE);
                                        textView.setText(Constants.COUNT + "");
                                    } else {
                                        Constants.COUNT = Integer.parseInt(pass);
                                        textView.setVisibility(View.VISIBLE);
                                        textView.setText(Constants.COUNT + "");
                                    }
                                } else {
                                    if (!TextUtils.isEmpty(msg)) {

                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", SharedPreferencesManager.getUserID(getApplicationContext()));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        methodNotifiaction();
    }

    private void initView() {
        tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        ImageView imgMenu = (ImageView) findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        ImageView imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drawerLayout.openDrawer(Gravity.LEFT);
                OnCallBackInternet();
            }
        });

    }

    private void initToolbar() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navRecycler = (RecyclerView) findViewById(R.id.navRecycler);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);

        LinearLayoutManager layoutMangerDestination = new LinearLayoutManager
                (getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        navRecycler.setLayoutManager(layoutMangerDestination);

        setNavigationAdapter();
    }

    private void setNavigationAdapter() {
        navigationAdapter = new NavigationRecycler(getApplicationContext(), navigationModelList);
        navRecycler.setAdapter(navigationAdapter);
        navigationAdapter.setListener(this);
    }


    private void iconAndTitleDataList(List<NavigationModel> navigationModelList) {
        NavigationModel navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Home");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Profile");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Conversion List");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Help & Support");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Invite");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Rate Us");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("About App");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Inbox");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

        navigationModel = new NavigationModel();
        navigationModel.setNavTitle("Logout");
        navigationModel.setNavIcons(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arrow_right_black));
        navigationModelList.add(navigationModel);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    public void OnCallBackInternet() {
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }

    @Override
    public void onItemClickPosition(int position) {
        switch (position) {
            case 1:
                OnCallBackInternet();
                break;
            case 2:
                MemberProfileFragment fragment = new MemberProfileFragment();
                fragment.setMyCallBackProfileViewListener(this);
                checkFragmentMethod(fragment);
                break;
            case 3:
                ConversionListFragment conversionListFragment = new ConversionListFragment();
                checkFragmentMethod(conversionListFragment);
                break;
            case 4:
                HelpSupportFragment helpSupportFragment = new HelpSupportFragment();
                checkFragmentMethod(helpSupportFragment);
                break;
            case 5:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "NISMWA App");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.javinindia.nismwa\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose one"));
                } catch (Exception e) {

                }
                break;
            case 6:
                final String appPackageName = getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case 7:
                AboutFragment aboutFragment = new AboutFragment();
                checkFragmentMethod(aboutFragment);
                break;
            case 8:
                MySuggestionFragment inboxFragment = new MySuggestionFragment();
                checkFragmentMethod(inboxFragment);
                break;
            case 9:
                dialogBox();
                break;

            default:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    private void checkFragmentMethod(BaseFragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.containerMain);
        String current = currentFragment.toString();
        String filename = fragment.toString();
        int iend = filename.indexOf("{");
        if (iend != -1) {
            filename = filename.substring(0, iend);
        }

        int bend = current.indexOf("{");
        if (bend != -1) {
            current = current.substring(0, bend);
        }

        if (current.equalsIgnoreCase(filename)) {
        } else {
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .add(R.id.containerMain, fragment).addToBackStack(filename).commit();
        }
    }


    @Override
    public void onBackButtonClick() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onEditClick() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void myProfileCallBack(String title) {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

                if (getVisibleFragment() instanceof HomePageFragment) {
                    open();
                } else if (getVisibleFragment() instanceof CheckConnectionFragment) {
                    open();
                }

            } else {
                super.onBackPressed();
            }
        }


    }

    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure You wanted to Exit.");
        alertDialogBuilder.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });


        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        final View notifications = menu.findItem(R.id.actionNotifications).getActionView();

        textView = (TextView) notifications.findViewById(R.id.txtCount);
        textView.setVisibility(View.GONE);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.GONE);
                NotificationListFragment aboutFragment = new NotificationListFragment();
                checkFragmentMethod(aboutFragment);
            }
        });
        return true;
    }

    public void updateHotCount(final int new_hot_number) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionNotifications:
                NotificationListFragment notificationListFragment = new NotificationListFragment();
                checkFragmentMethod(notificationListFragment);
                return true;
           /* case R.id.action_aboutUs:
                AboutFragment aboutFragment = new AboutFragment();
                checkFragmentMethod(aboutFragment);
                return true;
            case R.id.action_feedBack:
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                checkFragmentMethod(feedbackFragment);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    //--------------------------logout-----------------------------------//
    public void dialogBox() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Logout");
        builder1.setMessage("Are you sure you want to log out.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendDataOnLogOutApi();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void sendDataOnLogOutApi() {
        final ProgressDialog loading = ProgressDialog.show(this, "Logging out...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        responseImplement(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", SharedPreferencesManager.getUserID(getApplicationContext()));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void volleyDefaultTimeIncreaseMethod(StringRequest stringRequest) {
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(180000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String msg = null;
        int status = 0;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.has("status"))
                status = jsonObject.optInt("status");
            if (jsonObject.has("msg"))
                msg = jsonObject.optString("msg");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status == 1) {
            SharedPreferencesManager.setType(getApplicationContext(), null);
            SharedPreferencesManager.setUserID(getApplicationContext(), null);
            SharedPreferencesManager.setUsername(getApplicationContext(), null);
            SharedPreferencesManager.setPassword(getApplicationContext(), null);
            SharedPreferencesManager.setEmail(getApplicationContext(), null);
            SharedPreferencesManager.setProfileImage(getApplicationContext(), null);
            SharedPreferencesManager.setDeviceToken(getApplicationContext(), null);
            Intent refresh = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(refresh);//Start the same Activity
            finish();
        } else {
            if (!TextUtils.isEmpty(msg)) {
                Toast.makeText(this, "Try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void OnCallBackProfileView() {
        setNavigationAdapter();
    }

    //-------------------------------useful detail----------------------------//

    private void dialogMethod(String tt) {
        String test = "";
        screenSizeMethod();
        // custom_dialog_layout dialog
        dialog = new AlertDialog.Builder(this).create();
        //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout.LayoutParams dialogParams = new LinearLayout.LayoutParams(
                width - 100, ViewGroup.LayoutParams.WRAP_CONTENT);//set height(300) and width(match_parent) here, ie (width,height)
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dislogView = inflater
                .inflate(R.layout.custom_dialog_layout, null);
        dialog.setView(dislogView);
        progressBar = (ProgressBar) dislogView.findViewById(R.id.progress);
        RecyclerView recyclerview = (RecyclerView) dislogView.findViewById(R.id.recyclerViewDialog);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(layoutManager);

        AppCompatTextView txtTitle = (AppCompatTextView) dislogView.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(getApplicationContext()).getTypeFace());
        AppCompatEditText etDialog = (AppCompatEditText) dislogView.findViewById(R.id.etDialog);
        etDialog.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(getApplicationContext()).getTypeFace());

        if (tt.equalsIgnoreCase("city")) {

        } else if (tt.equalsIgnoreCase("state")) {
            txtTitle.setText("Select Category");
            etDialog.setVisibility(View.GONE);
            adopter = new UsefullMasterAdopter(getApplicationContext());
            LinearLayoutManager layoutMangerDestination
                    = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(layoutMangerDestination);
            recyclerview.setAdapter(adopter);
            adopter.setMyClickListener(this);
            sendRequestStateListFeed(test);
        }

        dialog.show();
    }

    private void sendRequestStateListFeed(final String test) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBER_USEFUL_MASTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        UsfullMasterResponse responseparsing = new UsfullMasterResponse();
                        responseparsing.responseParseMethod(response);
                        if (response.length() != 0) {
                            int status = responseparsing.getStatus();
                            if (status == 1) {
                                ArrayList arrayList = responseparsing.getUsefullMasterDetailArrayList();
                                if (arrayList.size() > 0) {
                                    adopter.setData(arrayList);
                                    adopter.notifyDataSetChanged();
                                }
                            } else {
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void screenSizeMethod() {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 13) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            width = display.getWidth();
            height = display.getWidth();
        }
    }

    @Override
    public void onItemUseClick(int position, UsefullMasterDetail model) {
        UsefullFragment usefullFragment = new UsefullFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", model.getId().trim());
        bundle.putString("name", model.getuName().trim());
        usefullFragment.setArguments(bundle);
        checkFragmentMethod(usefullFragment);
        dialog.dismiss();
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Constants.COUNT = Constants.COUNT + 1;
            if (Constants.COUNT > 0) {
                textView.setText(Constants.COUNT + "");
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        }
    };
}
