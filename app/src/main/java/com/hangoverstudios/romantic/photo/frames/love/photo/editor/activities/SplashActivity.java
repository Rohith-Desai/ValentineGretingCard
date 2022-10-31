package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.billing.BillingController;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.ConnectivityReceiver;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.DataHolder;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.MyAppOpen;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.Update;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.commonMethods;

/*import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;*/

public class SplashActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public static final String PREF_FILE = "PHOTO_COLLAGE";
    public static final String PURCHASE_KEY = "removeads";
    public static SplashActivity splashInstance;
    public static FirebaseAnalytics mFirebaseAnalytics;
    public FirebaseRemoteConfig remoteConfig;
    //  public InterstitialAd interstitialGoogleAd;
    ArrayList<Object> greetingPotList = new ArrayList<>();
    ArrayList<Object> greetingThumbList = new ArrayList<>();
    ArrayList<Object> greetingTrendList = new ArrayList<>();
    ArrayList<Object> greetingTrendThumbList = new ArrayList<>();
    ArrayList<Object> singleFramesList = new ArrayList<>();
    ArrayList<Object> singleFramesThumbList = new ArrayList<>();
    ArrayList<Object> singleTrendingFramesList = new ArrayList<>();
    ArrayList<Object> singleTrendingThumbList = new ArrayList<>();
    ArrayList<Object> doubleFramesList = new ArrayList<>();
    ArrayList<Object> doubleThumbList = new ArrayList<>();
    ArrayList<Object> doubleTrendingFramesList = new ArrayList<>();
    ArrayList<Object> doubleTrendingThumbList = new ArrayList<>();
    ArrayList<Object> anniversaryList = new ArrayList<>();
    ArrayList<Object> anniversaryThumb = new ArrayList<>();
    ArrayList<Object> birthdayList = new ArrayList<>();
    ArrayList<Object> birthdayThumb = new ArrayList<>();
    ArrayList<Object> flowerList = new ArrayList<>();
    ArrayList<Object> flowersThumb = new ArrayList<>();
    ArrayList<Object> quoteList = new ArrayList<>();
    ArrayList<Object> quoteThumb = new ArrayList<>();

    ArrayList<Object> dflowerList = new ArrayList<>();
    ArrayList<Object> dflowersThumb = new ArrayList<>();
    ArrayList<Object> dquoteList = new ArrayList<>();
    ArrayList<Object> dquoteThumb = new ArrayList<>();
    ArrayList<Object> danniversaryList = new ArrayList<>();
    ArrayList<Object> danniversaryThumb = new ArrayList<>();
    ArrayList<Object> dbirthdayList = new ArrayList<>();
    ArrayList<Object> dbirthdayThumb = new ArrayList<>();

    ArrayList<Object> shapeFramesList = new ArrayList<>();
    ArrayList<Object> shapeTrendingFramesList = new ArrayList<>();
    boolean adLoaded, timeOut = false;
    JSONObject remoteConfJson;
    TextView textView1, textView2, textView3;
    private BroadcastReceiver br = new ConnectivityReceiver();
    private boolean receiverRegistered = false;
    private GifImageView gifImageView1, gifImageView2, gifImageView3, gifImageView4, gifImageView5, gifImageView6;
    BillingController bill;

    public static void slideDown(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.f);

        if (view.getHeight() > 0) {
            slideUpNow(view);
        } else {
            // wait till height is measured
            view.post(new Runnable() {
                @Override
                public void run() {
                    slideUpNow(view);
                }
            });
        }
    }

    private static void slideUpNow(final View view) {
        view.setTranslationY(view.getHeight());
        view.animate()
                .translationY(0)
                .setDuration(4000)
                .alpha(1.f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                        view.setAlpha(1.f);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        splashInstance = this;
        FirebaseApp.initializeApp(this);

        bill = new BillingController(this);
        bill.billingInitialization();
        if (bill.getPurchaseValueFromPref()) {
            commonMethods.disableAds();
//            remove_ads.setVisibility(View.GONE);
        }

    }

    private void setAnimation(int res, View view) {
        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this,
                res);
        if (view.getId() == textView1.getId() || view.getId() == textView2.getId() || view.getId() == textView3.getId())
            animation.setRepeatMode(1);
        else
            animation.setRepeatMode(2);
        if (view.getId() == gifImageView2.getId() || view.getId() == gifImageView5.getId())
            animation.setStartOffset(1500);
        else if (view.getId() == gifImageView3.getId() || view.getId() == gifImageView4.getId())
            animation.setStartOffset(3000);
        view.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
              /*  if(more.getVisibility() == VISIBLE)
                    more.setVisibility(GONE);
                else
                    more.setVisibility(VISIBLE);*/
                // setAnimation(R.anim.fade_out, gifImageView1);
                // setAnimation(R.anim.fade_out, gifImageView2);
                // setAnimation(R.anim.fade_out, gifImageView3);
                // setAnimation(R.anim.fade_out, gifImageView4);
                // setAnimation(R.anim.fade_out, gifImageView5);
                //setAnimation(R.anim.fade_out, gifImageView6);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void assignDefaultValues() {
        try {
            /*“showInterstitialOnExit”: “false”,
“showInterstitialOnTrasparentWallpaper”: “false”,
“showInterstitialOnMirrorWallpaper”: “false”,
“showInterstitialOnRatingCancel”: “false”,*/
            JSONObject defaultJson = new JSONObject("{\n" +
                    "            \"upgradeAppVersion\": \"1\",\n" +
                    "            \"showInterstitialOnLaunch\": \"false\",\n" +
                    "            \"showInterstitialOnExit\": \"true\",\n" +
                    "            \"showInterstitialAd\": \"false\",\n" +
                    "            \"InterstitialOnlyGoogle\": \"true\",\n" +
                    "            \"InterstitialOnlyFB\": \"true\",\n" +
                    "            \"AdRotationPolicy\": \"false\",\n" +
                    "            \"AdRotationPolicyNative\": \"false\",\n" +
                    "            \"showNativeAd\": \"true\",\n" +
                    "            \"showNativeAdOnMainScreen\": \"true\",\n" +
                    "            \"NativeOnlyGoogle\": \"true\",\n" +
                    "            \"NativeOnlyFB\": \"false\",\n" +
                    "            \"showOurAppInterstitials\": \"false\",\n" +
                    "            \"OurApps\": \"false\",\n" +
                    "            \"enableIAPflag\": \"false\",\n" +
                    "            \"singleFramesCount\": \"38\",\n" +
                    "            \"greetingCardsCount\": \"28\",\n" +
                    "            \"allFramesCount\": \"20\",\n" +
                    "            \"singleFrames\": \"\",\n" +
                    "            \"doubleFrames\": \"\",\n" +
                    "            \"doubleThumbs\": \"\",\n" +
                    "            \"greetingCards\": \"\",\n" +
                    "            \"shapesFrames\": \"\",\n"+
                    "            \"trendingDoubleFrames\": \"\",\n" +
                    "            \"trendingDoubleThumbs\": \"\",\n" +
                    "            \"trendingGreetingCards\": \"\",\n" +
                    "            \"ShowRatingLayout\": \"\",\n" +
                    "            \"atCatSelection\": \"\",\n" +
                    "            \"singleLoveUrl\": \"\",\n" +
                    "            \"doubleLoveUrl\": \"\",\n" +
                    "            \"trendingGreetingThumbs\": \"\"\n" +
                    "            }");
            assignValuesRemote(defaultJson, "local");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadRemoteConfig();
    }

    /* private void assignDefaultValues() {
         RemoteConfigValues.getOurRemote().setUpgradeAppVersion("1");
         RemoteConfigValues.getOurRemote().setShowInterstitialOnLaunch("true");
         RemoteConfigValues.getOurRemote().setShowInterstitialOnExit("false");
         RemoteConfigValues.getOurRemote().setShowInterstitialOnLaunchFBAd("false");
         RemoteConfigValues.getOurRemote().setShowInterstitialAd("true");
         RemoteConfigValues.getOurRemote().setInterstitialOnlyGoogle("true");
         RemoteConfigValues.getOurRemote().setInterstitialOnlyFB("false");
         RemoteConfigValues.getOurRemote().setAdRotationPolicy("false");
         RemoteConfigValues.getOurRemote().setAdRotationPolicyNative("false");
         RemoteConfigValues.getOurRemote().setShowNativeAd("true");
         RemoteConfigValues.getOurRemote().setShowNativeAdOnMainScreen("true");
         RemoteConfigValues.getOurRemote().setNativeOnlyGoogle("true");
         RemoteConfigValues.getOurRemote().setNativeOnlyFB("false");
         RemoteConfigValues.getOurRemote().setShowOurAppInterstitials("false");
         RemoteConfigValues.getOurRemote().setOurApps("false");
         loadRemoteConfig();
     }*/
    private void assignValuesRemote(JSONObject remoteObj, String remote) {
        try {
            // Log.d("showRate",remoteObj.getString("ShowRatingLayout"));
            RemoteConfigValues.getOurRemote().setUpgradeAppVersion(remoteObj.getString("upgradeAppVersion"));
            RemoteConfigValues.getOurRemote().setShowInterstitialOnLaunch(remoteObj.getString("showInterstitialOnLaunch"));
            RemoteConfigValues.getOurRemote().setShowInterstitialOnExit(remoteObj.getString("showInterstitialOnExit"));
            RemoteConfigValues.getOurRemote().setShowInterstitialAd(remoteObj.getString("showInterstitialAd"));
            RemoteConfigValues.getOurRemote().setInterstitialOnlyGoogle(remoteObj.getString("InterstitialOnlyGoogle"));
            RemoteConfigValues.getOurRemote().setInterstitialOnlyFB(remoteObj.getString("InterstitialOnlyFB"));
            RemoteConfigValues.getOurRemote().setAdRotationPolicy(remoteObj.getString("AdRotationPolicy"));
            RemoteConfigValues.getOurRemote().setAdRotationPolicyNative(remoteObj.getString("AdRotationPolicyNative"));
            RemoteConfigValues.getOurRemote().setShowNativeAd(remoteObj.getString("showNativeAd"));
            RemoteConfigValues.getOurRemote().setShowNativeAdOnMainScreen(remoteObj.getString("showNativeAdOnMainScreen"));
            RemoteConfigValues.getOurRemote().setNativeOnlyGoogle(remoteObj.getString("NativeOnlyGoogle"));
            RemoteConfigValues.getOurRemote().setNativeOnlyFB(remoteObj.getString("NativeOnlyFB"));
            RemoteConfigValues.getOurRemote().setShowOurAppInterstitials(remoteObj.getString("showOurAppInterstitials"));
            RemoteConfigValues.getOurRemote().setOurApps(remoteObj.getString("OurApps"));
            RemoteConfigValues.getOurRemote().setEnableIAPflag(remoteObj.getString("enableIAPflag"));
            RemoteConfigValues.getOurRemote().setShowRatingLayout(remoteObj.getString("ShowRatingLayout"));
            RemoteConfigValues.getOurRemote().setAtCatSelection(remoteObj.getString("atCatSelection"));
            RemoteConfigValues.getOurRemote().setSingleLoveUrl(remoteObj.getString("singleLoveUrl"));
            RemoteConfigValues.getOurRemote().setDoubleLoveUrl(remoteObj.getString("doubleLoveUrl"));
//            RemoteConfigValues.getOurRemote().setEnableIAPflag("true");

            System.out.println(RemoteConfigValues.getOurRemote().getEnableIAPflag());
            RemoteConfigValues.getOurRemote().setRemoveAds("false"); // default false



            /*Log.v("Remote Config","upgradeAppVersion : "+RemoteConfigValues.getOurRemote().getUpgradeAppVersion());
            Log.v("Remote Config","showInterstitialOnLaunch : "+RemoteConfigValues.getOurRemote().getShowInterstitialOnLaunch());
            Log.v("Remote Config","showInterstitialOnExit : "+RemoteConfigValues.getOurRemote().getShowInterstitialOnExit());
            Log.v("Remote Config","ShowInterstitialAd : "+RemoteConfigValues.getOurRemote().getShowInterstitialAd());
            Log.v("Remote Config","InterstitialOnlyGoogle : "+RemoteConfigValues.getOurRemote().getInterstitialOnlyGoogle());
            Log.v("Remote Config","InterstitialOnlyFB : "+RemoteConfigValues.getOurRemote().getInterstitialOnlyFB());
            Log.v("Remote Config","AdRotationPolicy : "+RemoteConfigValues.getOurRemote().getAdRotationPolicy());
            Log.v("Remote Config","AdRotationPolicyNative : "+RemoteConfigValues.getOurRemote().getAdRotationPolicyNative());
            Log.v("Remote Config","ShowNativeAd : "+RemoteConfigValues.getOurRemote().getShowNativeAd());
            Log.v("Remote Config","ShowNativeAdOnMainScreen : "+RemoteConfigValues.getOurRemote().getShowNativeAdOnMainScreen());
            Log.v("Remote Config","NativeOnlyGoogle : "+RemoteConfigValues.getOurRemote().getNativeOnlyGoogle());
            Log.v("Remote Config","NativeOnlyFB : "+RemoteConfigValues.getOurRemote().getNativeOnlyFB());
            Log.v("Remote Config","ShowOurAppInterstitials : "+RemoteConfigValues.getOurRemote().getShowOurAppInterstitials());
            Log.v("Remote Config","OurApps : "+RemoteConfigValues.getOurRemote().getOurApps());*/

            //   RemoteConfigValues.getOurRemote().setServer(remoteObj.getString("server"));
            /*RemoteConfigValues.getOurRemote().setUpgradeAppVersion(remoteObj.getString("upgradeAppVersion"));
            RemoteConfigValues.getOurRemote().setShowInterstitialOnLaunch("true");
            RemoteConfigValues.getOurRemote().setShowInterstitialOnExit("true");
            RemoteConfigValues.getOurRemote().setShowInterstitialAd("true");
            RemoteConfigValues.getOurRemote().setInterstitialOnlyGoogle("true");
            RemoteConfigValues.getOurRemote().setInterstitialOnlyFB("false");
            RemoteConfigValues.getOurRemote().setAdRotationPolicy("true");
            RemoteConfigValues.getOurRemote().setAdRotationPolicyNative("true");
            RemoteConfigValues.getOurRemote().setShowNativeAd("true");
            RemoteConfigValues.getOurRemote().setShowNativeAdOnMainScreen(remoteObj.getString("showNativeAdOnMainScreen"));
            RemoteConfigValues.getOurRemote().setNativeOnlyGoogle("true");
            RemoteConfigValues.getOurRemote().setNativeOnlyFB("true");
            RemoteConfigValues.getOurRemote().setShowOurAppInterstitials(remoteObj.getString("showOurAppInterstitials"));
            RemoteConfigValues.getOurRemote().setOurApps(remoteObj.getString("OurApps"));*/

//            RemoteConfigValues.getOurRemote().setShowNativeAd("true");

            if (remote.equals("remote")) {
                prepareListOfData(remoteObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadRemoteConfig() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timeOut = true;
            }
        }, 10000);
        //initialization
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        // cache expiration in seconds 0 for developer mode 43200 for production
        long cacheExpiration = 43200;
        // fetch
        remoteConfig.fetch(cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // task successful. Activate the fetched data
                    remoteConfig.activate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            String welcomeMessage = remoteConfig.getString("firebase_remote_config");
                            try {
                                remoteConfJson = new JSONObject(welcomeMessage);
                                assignValuesRemote(remoteConfJson, "remote");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        checkData();
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }


    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setSplashVideo();
        if (MyAppOpen.getInstance() != null) {
            MyAppOpen.getInstance().setConnectivityListener(this);
        }
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(br, filter);
        receiverRegistered = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConnectivityReceiver.connectivityReceiverListener = null;
        if (receiverRegistered) {
            unregisterReceiver(br);
        }
    }

    private void loadGoogleAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        /*interstitialGoogleAd = new InterstitialAd(this);
        interstitialGoogleAd.setAdUnitId(getString(R.string.admob_interstitial_id));
        requestNewInterstitial();
        interstitialGoogleAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                System.out.println("loadAd");
                if (!adLoaded) {
                    adLoaded = true;
                }
            }

            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdFailedToLoad(int i) {
                if (!adLoaded) {
                    adLoaded = true;
                }
                requestNewInterstitial();
                super.onAdFailedToLoad(i);
            }
        });*/
    }

   /* public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        if (interstitialGoogleAd != null) {
            interstitialGoogleAd.loadAd(adRequest);
        }
    }*/

    private void checkData() {

//        RemoteConfigValues.getOurRemote().setEnableIAPflag("true");
        int localAppVersion = 22;
        //  if (RemoteConfigValues.getOurRemote().getServer().equals("false")) {
           /* if (timeOut) {
                int a = Integer.parseInt(RemoteConfigValues.getOurRemote().getUpgradeAppVersion());
                if (localAppVersion >= a) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    //startImg.setVisibility(View.VISIBLE);
                   // loading.setVisibility(View.INVISIBLE);
                } else {
                    startActivity(new Intent(SplashActivity.this, Update.class));
                    finish();
                }
            } else {
                holdTime();
            }*/
        // }
        //else {
        if (timeOut) {
            int a = Integer.parseInt(RemoteConfigValues.getOurRemote().getUpgradeAppVersion());
            if (localAppVersion >= a) {
                if (getPurchaseValueFromPref()) {
                    commonMethods.disableAds();
                }
                startActivity(new Intent(SplashActivity.this, NewStarscreen.class));
                finish();
                //startImg.setVisibility(View.VISIBLE);
                //loading.setVisibility(View.INVISIBLE);
            } else {
                startActivity(new Intent(SplashActivity.this, Update.class));
                finish();
            }
        } else {
            holdTime();
        }
        //  }
    }

    private void holdTime() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkData();
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
            }
        }, 3000);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            // checkData();
            // holdTime();
            assignDefaultValues();
            loadGoogleAd();
        } else {
            Toast.makeText(SplashActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
        }
    }

    //share Preference used Here

    private SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private boolean getPurchaseValueFromPref() {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false);
    }

    private void savePurchaseValueToPref(boolean value) {
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit();
    }

    public void prepareListOfData(JSONObject remoteObj) {

        try {
            RemoteConfigValues.getOurRemote().setUpgradeAppVersion(remoteObj.getString("upgradeAppVersion"));
            for (int i = 1; i <= 54; i++) {
                singleFramesList.add(remoteObj.getString("singleFrames") + i + ".png");
                singleFramesThumbList.add(remoteObj.getString("trendingSingleFrames").replace("trending/romantic_frame_", "love/single_frame_thumb_") + i + ".jpg");

            }
            for (int i = 1; i <= 35; i++) {
                if (i <= 5) {
                    singleFramesList.add("valentine_card_" + i);
                    greetingPotList.add("greetings_p" + i);
                    greetingThumbList.add(remoteObj.getString("trendingGreetingCards").replace("trending/greeting_cards_", "love/greeting_thumb_") + i + ".jpg");
                } else {
                    // Log.d("single_frames_check",remoteObj.getString("singleFrames") + i + ".png");
                    singleFramesList.add(
                            remoteObj.getString("singleFrames") + i + ".png");

                    if (i <= 28) {
                        greetingPotList.add(remoteObj.getString("greetingCards") + i + ".jpg");
                        greetingThumbList.add(remoteObj.getString("trendingGreetingCards").replace("trending/greeting_cards_", "love/greeting_thumb_") + i + ".jpg");
                    }
                }

            }
            DataHolder.getDataHolder().setGreetingsList(greetingPotList);
            DataHolder.getDataHolder().setSingleFramesList(singleFramesList);

            for (int i = 1; i <= 35; i++) {

                singleFramesThumbList.add(remoteObj.getString("trendingSingleFrames").replace("trending/romantic_frame_", "love/single_frame_thumb_") + i + ".jpg");
                //  Log.d("singleframethumb",remoteObj.getString("trendingSingleFrames").replace("trending/romantic_frame_", "love/single_frame_thumb_") + i + ".jpg");

                if (i <= 20) {
                    singleTrendingFramesList.add(remoteObj.getString("trendingSingleFrames") + i + ".png");
                    singleTrendingThumbList.add(remoteObj.getString("trendingSingleThumbs") + i + ".jpg");
                    doubleFramesList.add(remoteObj.getString("doubleFrames") + i + ".png");
                    doubleThumbList.add(remoteObj.getString("doubleThumbs") + i + ".png");
                    doubleTrendingFramesList.add(remoteObj.getString("trendingDoubleFrames") + i + ".png");
                    doubleTrendingThumbList.add(remoteObj.getString("trendingDoubleThumbs") + i + ".jpg");
                    greetingTrendList.add(remoteObj.getString("trendingGreetingCards") + i + ".png");
                    greetingTrendThumbList.add(remoteObj.getString("trendingGreetingThumbs") + i + ".jpg");
                    shapeFramesList.add(remoteObj.getString("shapesFrames") + i + ".png");

                    Log.v("LOG_TAG", "url : " + remoteObj.getString("trendingGreetingCards"));
                }
            }


            for (int n = 1; n <= 15; n++) {
                anniversaryList.add(remoteObj.getString("singleLoveUrl") + "anniversary/anniversary_eng_" + n + ".png");
                anniversaryThumb.add(remoteObj.getString("singleLoveUrl") + "anniversary/anniversary_eng_thumb_" + n + ".jpg");
                birthdayList.add(remoteObj.getString("singleLoveUrl") + "birthday/birthday_" + n + ".png");
                birthdayThumb.add(remoteObj.getString("singleLoveUrl") + "birthday/birthday_thumb_" + n + ".jpg");
                flowerList.add(remoteObj.getString("singleLoveUrl") + "flowers/flower_" + n + ".png");
                flowersThumb.add(remoteObj.getString("singleLoveUrl") + "flowers/flower_thumb_" + n + ".jpg");
                quoteList.add(remoteObj.getString("singleLoveUrl") + "quotes/Love_Q_" + n + ".png");
                quoteThumb.add(remoteObj.getString("singleLoveUrl") + "quotes/Love_Q_thumb_" + n + ".jpg");


                dflowerList.add(remoteObj.getString("doubleLoveUrl") + "flowers/dflower_" + n + ".png");
                dflowersThumb.add(remoteObj.getString("doubleLoveUrl") + "flowers/dflower_thumb_" + n + ".jpg");
                danniversaryList.add(remoteObj.getString("doubleLoveUrl") + "anniversary/danniversary_eng_" + n + ".png");
                danniversaryThumb.add(remoteObj.getString("doubleLoveUrl") + "anniversary/danniversary_eng_thumb_" + n + ".jpg");
                if(n<=12){
                    dbirthdayList.add(remoteObj.getString("doubleLoveUrl") + "birthday/dbirthday_" + n + ".png");
                    dbirthdayThumb.add(remoteObj.getString("doubleLoveUrl") + "birthday/dbirthday_thumb_" + n + ".jpg");
                    dquoteList.add(remoteObj.getString("doubleLoveUrl") + "quotes/dLove_Q_" + n + ".png");
                    dquoteThumb.add(remoteObj.getString("doubleLoveUrl") + "quotes/dLove_Q_thumb_" + n + ".jpg");
                }
            }

            DataHolder.getDataHolder().setSingleFramesThumbList(singleFramesThumbList);
            DataHolder.getDataHolder().setSingleTrendingThumbList(singleTrendingThumbList);
            DataHolder.getDataHolder().setSingleTrendingFramesList(singleTrendingFramesList);
            DataHolder.getDataHolder().setDoubleFramesList(doubleFramesList);
            DataHolder.getDataHolder().setDoubleThumbList(doubleThumbList);
            DataHolder.getDataHolder().setDoubleTrendingFramesList(doubleTrendingFramesList);
            DataHolder.getDataHolder().setDoubleTrendingThumbList(doubleTrendingThumbList);
            DataHolder.getDataHolder().setGreetingTrendList(greetingTrendList);
            DataHolder.getDataHolder().setGreetingsThumbList(greetingThumbList);
            DataHolder.getDataHolder().setGreetingTrendThumbList(greetingTrendThumbList);
            DataHolder.getDataHolder().setGreetingsList(greetingPotList);
            DataHolder.getDataHolder().setShapeFramesList(shapeFramesList);


            DataHolder.getDataHolder().setAnniversary(anniversaryList);
            DataHolder.getDataHolder().setThumbAnniversary(anniversaryThumb);
            DataHolder.getDataHolder().setBirthday(birthdayList);
            DataHolder.getDataHolder().setThumbBirthday(birthdayThumb);
            DataHolder.getDataHolder().setFlowers(flowerList);
            DataHolder.getDataHolder().setThumbFlowers(flowersThumb);
            DataHolder.getDataHolder().setQuotes(quoteList);
            DataHolder.getDataHolder().setThumbQuotes(quoteThumb);


            DataHolder.getDataHolder().setdFlowers(dflowerList);
            DataHolder.getDataHolder().setdThumbFlowers(dflowersThumb);
            DataHolder.getDataHolder().setdAnniversary(danniversaryList);
            DataHolder.getDataHolder().setdThumbAnniversary(danniversaryThumb);
            DataHolder.getDataHolder().setdBirthday(dbirthdayList);
            DataHolder.getDataHolder().setdThumbBirthday(dbirthdayThumb);
            DataHolder.getDataHolder().setdQuotes(dquoteList);
            DataHolder.getDataHolder().setdThumbQuotes(dquoteThumb);

            Log.v("LOG_TAG", "single trend thumb url : " + DataHolder.getDataHolder().getSingleTrendingThumbList());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
