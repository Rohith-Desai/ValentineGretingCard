package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.getAdSize;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.myClickAnim;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.populateUnifiedNativeAdView;

public class GreetingMainActivity extends AppCompatActivity {

    CardView greeting_frames, greeting_trending_frames;
    private FrameLayout adContainerView;
    private AdView adViewBanner1;
    private NativeAd nativeAd;
    private ImageView back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting_main);

        greeting_frames = findViewById(R.id.greeting_frames);
        greeting_trending_frames = findViewById(R.id.greeting_trending_frames);
        myClickAnim(this,greeting_frames);
        myClickAnim(this,greeting_trending_frames);

        greeting_frames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GreetingMainActivity.this, GreetingsListActivity.class);
                startActivity(i);
            }
        });

        greeting_trending_frames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GreetingMainActivity.this, GreetingsTrendListActivity.class);
                startActivity(i);
            }
        });


        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

           if (RemoteConfigValues.getOurRemote().getShowNativeAd() != null) {
            if (RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")) {
                adMobNativeAd();
            }
        }

        adContainerView = findViewById(R.id.admob_banner_greetingMain);
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

    private void adMobNativeAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_id));
        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isDestroyed();
                        }
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (GreetingMainActivity.this.nativeAd != null) {
                            GreetingMainActivity.this.nativeAd.destroy();
                        }
                        GreetingMainActivity.this.nativeAd = nativeAd;
                        FrameLayout frameLayout = findViewById(R.id.native_ad_greetingMain);
                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        populateUnifiedNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        com.google.android.gms.ads.nativead.NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}