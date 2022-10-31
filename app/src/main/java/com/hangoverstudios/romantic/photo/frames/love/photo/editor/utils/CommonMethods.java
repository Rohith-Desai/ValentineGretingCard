package com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.LoadingAd;

import java.util.LinkedList;
import java.util.Queue;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SplashActivity.splashInstance;

public class CommonMethods {
   // private final static String RATING_PREFERENCES = "romanticphoto";
    public static final CommonMethods commonMethods = new CommonMethods();
    private boolean showPermission = false;

    private Queue<String> bitmapEventQueue = new LinkedList<>();

    public void addToBitmapEventQueue(String record) {
        bitmapEventQueue.add(record);
    }

    public Queue<String> getBitmapEventQueue() {
        return bitmapEventQueue;
    }

    public void clearBitmapEventQueue() {
        if (bitmapEventQueue != null) {
            bitmapEventQueue.clear();
        }
    }

    public static CommonMethods getInstance() {
        return commonMethods;
    }

    public void onLaunchAD(Context context) {
        if (RemoteConfigValues.getOurRemote().getShowInterstitialOnLaunch() != null
                && RemoteConfigValues.getOurRemote().getShowInterstitialOnLaunch().equals("true")) {
            showGoogleAd(context);
            //showGoogleAd(context,true);
        }
    }

    public void onExitAD(Context context) {
        if (RemoteConfigValues.getOurRemote().getShowInterstitialOnExit() != null
                && RemoteConfigValues.getOurRemote().getShowInterstitialOnExit().equals("true")) {
            showGoogleAd(context);
            //showGoogleAd(context,false);
        }
    }

    public void loadBannerAd(AdView adViewBanner, FrameLayout adViewContainer, Context context) {
        if (RemoteConfigValues.getOurRemote().getRemoveAds() != null)
            if (!RemoteConfigValues.getOurRemote().getRemoveAds().equals("true")) {
                if(context!=null) {
                    adViewBanner = new AdView(context);
                    adViewBanner.setAdUnitId(context.getString(R.string.admob_banner_id));
                    adViewContainer.removeAllViews();
                    adViewContainer.addView(adViewBanner);

                    AdSize adSize = getAdSize(context, adViewContainer);
                    adViewBanner.setAdSize(adSize);

                    AdRequest adRequest =
                            new AdRequest.Builder().build();
                    adViewBanner.loadAd(adRequest);
                }
            }

    }

    public void activitiesAD(Context context) {
        if (RemoteConfigValues.getOurRemote().getShowInterstitialAd() != null) {
            if (RemoteConfigValues.getOurRemote().getShowInterstitialAd().equals("true")) {
                if (RemoteConfigValues.getOurRemote().getAdRotationPolicy().equals("true")) {
                    if (isInterstitialFB()) {
                        /*if (splashInstance.interstitialFBAd != null && splashInstance.interstitialFBAd.isAdLoaded()) {
                            splashInstance.interstitialFBAd.show();
                        } else if (splashInstance.interstitialGoogleAd != null && splashInstance.interstitialGoogleAd.isLoaded()) {
                            splashInstance.interstitialGoogleAd.show();
                        } else {*/
                       // splashInstance.requestNewInterstitial();
                        // }
                        Intent intent=new Intent(context, LoadingAd.class);
                        context.startActivity(intent);
                    } else {
                       /* if (splashInstance.interstitialGoogleAd != null && splashInstance.interstitialGoogleAd.isLoaded()) {
                            splashInstance.interstitialGoogleAd.show();
                        } else {
                            splashInstance.requestNewInterstitial();
                        }*/
                        Intent intent=new Intent(context, LoadingAd.class);
                        context.startActivity(intent);
                    }
                } else {
                    if (RemoteConfigValues.getOurRemote().getInterstitialOnlyGoogle().equals("true")) {
                        // showGoogleAd(context,false);
                        showGoogleAd(context);
                    }
                }
            }
        }
    }

    public void showGoogleAd(Context context) {
        if (RemoteConfigValues.getOurRemote().getShowInterstitialAd() != null
                && RemoteConfigValues.getOurRemote().getShowInterstitialAd().equals("true")) {
           /* if (splashInstance.interstitialGoogleAd != null && splashInstance.interstitialGoogleAd.isLoaded()) {
                splashInstance.interstitialGoogleAd.show();
            } else {
                splashInstance.requestNewInterstitial();
            }*/
            Intent intent=new Intent(context,LoadingAd.class);
            context.startActivity(intent);
        }

    }

    public void frameSelectionAd(Context context) {
        if (RemoteConfigValues.getOurRemote().getAtCatSelection() != null
                && RemoteConfigValues.getOurRemote().getAtCatSelection().equals("true")) {
            Intent intent=new Intent(context,LoadingAd.class);
            context.startActivity(intent);
        }

    }

  /*  private void showGoogleAd( Context context, final boolean isMain) {
        if (splashInstance.interstitialGoogleAd != null && splashInstance.interstitialGoogleAd.isLoaded()) {
            splashInstance.interstitialGoogleAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {


                    splashInstance.interstitialGoogleAd.show();

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {



                }

                @Override
                public void onAdOpened() {

                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    if(isMain)
                    {
                        showPermission = true;
                    }

                }
            });
        } else {

        }
    }*/
    public  boolean getShowPermission()
    {
        return showPermission;
    }

    private boolean isInterstitialFB() {
        boolean interstitialFB = false;
        return interstitialFB;
    }

    public static AdSize getAdSize(Context context, View adContainerView) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public void disableAds() {

        RemoteConfigValues.getOurRemote().setRemoveAds("true");
        RemoteConfigValues.getOurRemote().setShowInterstitialOnLaunch("false");
        RemoteConfigValues.getOurRemote().setShowInterstitialOnExit("false");
        RemoteConfigValues.getOurRemote().setShowInterstitialAd("false");
        RemoteConfigValues.getOurRemote().setInterstitialOnlyFB("false");
        RemoteConfigValues.getOurRemote().setAdRotationPolicy("false");
        RemoteConfigValues.getOurRemote().setShowNativeAd("false");
        RemoteConfigValues.getOurRemote().setShowNativeAdOnMainScreen("false");
        RemoteConfigValues.getOurRemote().setAdRotationPolicyNative("false");
        RemoteConfigValues.getOurRemote().setInterstitialOnlyGoogle("false");
        RemoteConfigValues.getOurRemote().setNativeOnlyFB("false");
        RemoteConfigValues.getOurRemote().setNativeOnlyGoogle("false");
        RemoteConfigValues.getOurRemote().setShowOurAppInterstitials("false");
        RemoteConfigValues.getOurRemote().setOurApps("false");
        RemoteConfigValues.getOurRemote().setEnableIAPflag("false");

    }

   /* public boolean ratingIsDone(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences(RATING_PREFERENCES, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedpreferences.edit();
        return sharedpreferences.getBoolean("rating", false);
    }*/

   /* public void ratingDone(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(RATING_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("rating", true);
        editor.apply();
    }*/

    public static void myClickAnim(@NonNull Context context, @NonNull View view) {
        if(view != null){
            view.setOnTouchListener(new CardClickAnim(context));
        }

    }


    static class CardClickAnim implements View.OnTouchListener {
        final Context f28359b;
        CardClickAnim(Context context) {
            this.f28359b = context;
        }
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.startAnimation(AnimationUtils.loadAnimation(this.f28359b, R.anim.press));
            return false;
        }
    }


    public static void populateUnifiedNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.GONE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.GONE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.GONE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {
        }
    }


    public static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.GONE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {
        }
    }
}











