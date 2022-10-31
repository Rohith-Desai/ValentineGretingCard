package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.DoubleFrameAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.SingleFrameAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.DataHolder;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.getAdSize;

public class DoubleFramesListActivity extends AppCompatActivity {
    Dialog myLoadingDialog;
    static final int STRING = 404;
    static final int NATIVE_UnifiedNativeAd = 10519;
    boolean adLoaded, dataLoaded;
    public static final int NUMBER_OF_AD_MOB_ADS = 4;
    //    private NativeAdsManager mNativeAdsManager;
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    
    private FrameLayout adContainerView;
    private AdView adViewBanner1;
    RecyclerView card_templates_rv;

    ImageView back_arrow;
    ArrayList<Object> doubleFramesThumbList = new ArrayList<>();
    ArrayList<Object> doubleFramesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_frames_list);
        CommonMethods.getInstance().frameSelectionAd(DoubleFramesListActivity.this);

        card_templates_rv= findViewById(R.id.double_rv);
        back_arrow=findViewById(R.id.back_arrow);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adContainerView = findViewById(R.id.admob_banner_doubleList);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                  if(RemoteConfigValues.getOurRemote().getRemoveAds() != null){
                    if(RemoteConfigValues.getOurRemote().getRemoveAds().equals("false")){
                        adContainerView.setVisibility(View.VISIBLE);
                        loadBanner();
                    }
                }
            }
        });
        showLoadingDialog();
        prepareDoubleFramesList();

    }
    private void loadBanner() {

        adViewBanner1 = new AdView(this);
        adViewBanner1.setAdUnitId(getString(R.string.admob_banner_id));
        adContainerView.removeAllViews();
        adContainerView.addView(adViewBanner1);

        AdSize adSize = getAdSize(this, adContainerView);
        adViewBanner1.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adViewBanner1.loadAd(adRequest);

        adViewBanner1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adContainerView.setBackgroundResource(0);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.e("aaaaaaaaaaaaaa", "onAdFailedToLoad: "+loadAdError);
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }
    /*private void loadAdMobNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            adLoaded = true;
                            prepareDoubleFramesList();
                            System.out.println("XXX"+"loaded");
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            adLoaded = true;
                            prepareDoubleFramesList();
                        }
                        System.out.println("XXX"+"loaded");
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_AD_MOB_ADS);
    }*/

    private void showLoadingDialog() {
        myLoadingDialog = new Dialog(DoubleFramesListActivity.this);
        Window window = myLoadingDialog.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimationTransition;
        myLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myLoadingDialog.setCancelable(false);
        myLoadingDialog.setContentView(R.layout.fraems_loading);
        myLoadingDialog.setCancelable(false);
        myLoadingDialog.show();
    }

 private void hideLoadingDialog() {
        if (!this.isDestroyed()) {
            if (!isFinishing()) {
                if (myLoadingDialog != null) {
                    if (myLoadingDialog.isShowing()) {
                        myLoadingDialog.dismiss();
                    }
                }

            }
        }
    }
    private void prepareDoubleFramesList() {
        if(DataHolder.getDataHolder().getDoubleFramesList() != null &&
                DataHolder.getDataHolder().getDoubleThumbList() != null){
           /* doubleFramesList.addAll(DataHolder.getDataHolder().getDoubleFramesList());
            doubleFramesList.removeAll(Collections.singleton(null));
            DataHolder.getDataHolder().getDoubleFramesList().clear();
            DataHolder.getDataHolder().getDoubleFramesList().addAll(doubleFramesList);*/

            doubleFramesList.clear();
            doubleFramesThumbList.clear();
            String from = "";

            if(getIntent().hasExtra("from")){
                if(getIntent().getStringExtra("from").equals("anniversary")){
                    from = "anniversary";
                    doubleFramesThumbList.addAll(DataHolder.getDataHolder().getdThumbAnniversary());
                    doubleFramesList.addAll(DataHolder.getDataHolder().getdAnniversary());
                }else if(getIntent().getStringExtra("from").equals("birthday")){
                    from = "birthday";
                    doubleFramesThumbList.addAll(DataHolder.getDataHolder().getdThumbBirthday());
                    doubleFramesList.addAll(DataHolder.getDataHolder().getdBirthday());
                }else if(getIntent().getStringExtra("from").equals("flower")){
                    from = "flower";
                    doubleFramesThumbList.addAll(DataHolder.getDataHolder().getdThumbFlowers());
                    doubleFramesList.addAll(DataHolder.getDataHolder().getdFlowers());
                }else if(getIntent().getStringExtra("from").equals("quote")){
                    from = "quote";
                    doubleFramesThumbList.addAll(DataHolder.getDataHolder().getdThumbQuotes());
                    doubleFramesList.addAll(DataHolder.getDataHolder().getdQuotes());
                }
            }else {
                from = "";
                doubleFramesThumbList.addAll(DataHolder.getDataHolder().getDoubleThumbList());
                doubleFramesList.addAll(DataHolder.getDataHolder().getDoubleFramesList());
            }
          //  dataLoaded = true;
            hideLoadingDialog();
            if (doubleFramesThumbList.size() > 0) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(DoubleFramesListActivity.this, 1);
                card_templates_rv.setLayoutManager(gridLayoutManager);
                DoubleFrameAdapter doubleFrameAdapter = new DoubleFrameAdapter(doubleFramesThumbList, this, 2, from);
                card_templates_rv.setAdapter(doubleFrameAdapter);

            }

        }

    }
    @Override
    protected void onDestroy() {
        hideLoadingDialog();
        super.onDestroy();
    }
}