package com.javinindia.nismwa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.javinindia.nismwa.R;
import com.javinindia.nismwa.constant.Constants;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.adsparsing.AdsResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SplashActivity extends BaseActivity {
    private RequestQueue requestQueue;
    ImageView imgAdd;
    private String ownerId;
    ProgressBar progress;
    final ArrayList<String> partner = new ArrayList<>();
    int siz = 0;
    int time = 1;
    private int picName = 0;
    double startTimer = 3100;
    double durationTimer = 3100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAndroidFirebaseInstanceIdMain main = new MyAndroidFirebaseInstanceIdMain();
        main.methodTest();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutResourceId());
        TextView txt_splash = findViewById(R.id.txt_splash);
        txt_splash.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(getApplicationContext()).getTypeFace());
        AppCompatTextView txtLoading = findViewById(R.id.txtLoading);
        txtLoading.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(getApplicationContext()).getTypeFace());
        imgAdd = findViewById(R.id.imgAdd);
        progress = findViewById(R.id.progress);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(getApplicationContext())) && !SharedPreferencesManager.getUserID(getApplicationContext()).equalsIgnoreCase("null")) {
            ownerId = SharedPreferencesManager.getUserID(getApplicationContext());
        } else {
            ownerId = "";
        }
        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferencesManager.setAndroidId(getApplicationContext(), m_androidId);
        hitaddMethod();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    private void hitaddMethod() {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MAIN_ADS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        final AdsResponse candidateQuickJobsResponse = new AdsResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);

                        try {
                            if (candidateQuickJobsResponse.getAdsDetailArrayList().size() > 0) {
                                siz = candidateQuickJobsResponse.getAdsDetailArrayList().size();
                                for (int i = 0; i < siz; i++) {
                                    partner.add(candidateQuickJobsResponse.getAdsDetailArrayList().get(i).getBig_image().trim());
                                }
                                imgAdd.setVisibility(View.VISIBLE);
                            }

                            if (siz > 0) {
                                time = siz;
                                startTimer = startTimer * time + startTimer;
                            }

                            CountDownTimer waitTimer;
                            waitTimer = new CountDownTimer((long) startTimer, (long) durationTimer) {

                                public void onTick(long millisUntilFinished) {
                                    if (siz > 0 && siz != picName) {
                                        if (!TextUtils.isEmpty(candidateQuickJobsResponse.getAdsDetailArrayList().get(picName).getBig_image().trim())) {
                                            String ads = candidateQuickJobsResponse.getAdsDetailArrayList().get(picName).getBig_image().trim();
                                            Picasso.with(getApplicationContext()).load(ads).error(R.drawable.bg_image).into(imgAdd);
                                        } else {
                                            Picasso.with(getApplicationContext()).load(R.drawable.bg_image).into(imgAdd);
                                        }
                                        picName++;
                                    }
                                }

                                public void onFinish() {
                                    if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(getApplicationContext()))) {
                                        Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(splashIntent);
                                        finish();
                                    } else {
                                        Intent splashIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(splashIntent);
                                        finish();
                                    }

                                }
                            }.start();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
                        Log.e("MAIN_ADS_URL ", error + "");
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ownerId", ownerId);
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

    public class MyAndroidFirebaseInstanceIdMain extends FirebaseInstanceIdService {
        @Override
        public void onTokenRefresh() {
            //Get hold of the registration token
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (!TextUtils.isEmpty(refreshedToken)) {
                setTokenMethod(refreshedToken);
            } else {

            }
        }

        public void methodTest() {
            onTokenRefresh();
        }

    }

    private void setTokenMethod(String refreshedToken) {
        SharedPreferencesManager.setDeviceToken(getApplicationContext(), refreshedToken);
    }

}
