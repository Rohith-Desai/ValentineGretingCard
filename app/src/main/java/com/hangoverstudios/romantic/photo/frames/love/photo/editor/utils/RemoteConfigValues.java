package com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils;

public class RemoteConfigValues {
    private static final RemoteConfigValues ourRemote = new RemoteConfigValues();
    private String upgradeAppVersion;
    private String showInterstitialOnLaunch;
    private String showInterstitialOnExit;
    private String showInterstitialAd;
    private String InterstitialOnlyGoogle;
    private String InterstitialOnlyFB;
    private String AdRotationPolicy;
    private String AdRotationPolicyNative;
    private String showNativeAd;
    private String showNativeAdOnMainScreen;
    private String NativeOnlyGoogle;
    private String NativeOnlyFB;
    private String showOurAppInterstitials;
    private String ourApps;
    private String ShowRatingLayout;
    private String atCatSelection;
    private String singleLoveUrl;
    private String doubleLoveUrl;

    public static RemoteConfigValues getOurRemote() {
        return ourRemote;
    }

    /*public String getServer() {
        return server;
    }*/

    /*public void setServer(String server) {
        this.server = server;
    }*/

    public String getUpgradeAppVersion() {
        return upgradeAppVersion;
    }

    public void setUpgradeAppVersion(String upgradeAppVersion) {
        this.upgradeAppVersion = upgradeAppVersion;
    }

    public String getShowInterstitialOnLaunch() {
        return showInterstitialOnLaunch;
    }

    public void setShowInterstitialOnLaunch(String showInterstitialOnLaunch) {
        this.showInterstitialOnLaunch = showInterstitialOnLaunch;
    }

    public String getShowInterstitialOnExit() {
        return showInterstitialOnExit;
    }

    public void setShowInterstitialOnExit(String showInterstitialOnExit) {
        this.showInterstitialOnExit = showInterstitialOnExit;
    }

   /* public String getShowNativeAdOnDialog() {
        return showNativeAdOnDialog;
    }

    public void setShowNativeAdOnDialog(String showNativeAdOnDialog) {
        this.showNativeAdOnDialog = showNativeAdOnDialog;
    }*/

    /*public String getShowRewardVideoAd() {
        return showRewardVideoAd;
    }*/

    /*public void setShowRewardVideoAd(String showRewardVideoAd) {
        this.showRewardVideoAd = showRewardVideoAd;
    }*/

    /*public String getImageUrl() {
        return imageUrl;
    }*/

    /*public void setImaUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }*/

    public String getShowInterstitialAd() {
        return showInterstitialAd;
    }

    public void setShowInterstitialAd(String showInterstitialAd) {
        this.showInterstitialAd = showInterstitialAd;
    }

    public String getInterstitialOnlyGoogle() {
        return InterstitialOnlyGoogle;
    }

    public void setInterstitialOnlyGoogle(String interstitialOnlyGoogle) {
        InterstitialOnlyGoogle = interstitialOnlyGoogle;
    }

    public String getInterstitialOnlyFB() {
        return InterstitialOnlyFB;
    }

    public void setInterstitialOnlyFB(String interstitialOnlyFB) {
        InterstitialOnlyFB = interstitialOnlyFB;
    }

    public String getAdRotationPolicy() {
        return AdRotationPolicy;
    }

    public void setAdRotationPolicy(String adRotationPolicy) {
        AdRotationPolicy = adRotationPolicy;
    }

    public String getAdRotationPolicyNative() {
        return AdRotationPolicyNative;
    }

    public void setAdRotationPolicyNative(String adRotationPolicyNative) {
        AdRotationPolicyNative = adRotationPolicyNative;
    }

    public String getShowNativeAd() {
        return showNativeAd;
    }

    public void setShowNativeAd(String showNativeAd) {
        this.showNativeAd = showNativeAd;
    }

    public String getShowNativeAdOnMainScreen() {
        return showNativeAdOnMainScreen;
    }

    public void setShowNativeAdOnMainScreen(String showNativeAdOnMainScreen) {
        this.showNativeAdOnMainScreen = showNativeAdOnMainScreen;
    }

    public String getNativeOnlyGoogle() {
        return NativeOnlyGoogle;
    }

    public void setNativeOnlyGoogle(String nativeOnlyGoogle) {
        NativeOnlyGoogle = nativeOnlyGoogle;
    }

    public String getNativeOnlyFB() {
        return NativeOnlyFB;
    }

    public void setNativeOnlyFB(String nativeOnlyFB) {
        NativeOnlyFB = nativeOnlyFB;
    }

    public String getShowOurAppInterstitials() {
        return showOurAppInterstitials;
    }

    public void setShowOurAppInterstitials(String showOurAppInterstitials) {
        this.showOurAppInterstitials = showOurAppInterstitials;
    }

    public String getOurApps() {
        return ourApps;
    }

    public void setOurApps(String ourApps) {
        this.ourApps = ourApps;
    }


    private String  removeAds , enableIAPflag ;

    public String getRemoveAds() {
        return removeAds;
    }

    public void setRemoveAds(String removeAds) {
        this.removeAds = removeAds;
    }

    public String getEnableIAPflag() {
        return enableIAPflag;
    }

    public void setEnableIAPflag(String enableIAPflag) {
        this.enableIAPflag = enableIAPflag;
    }

    public String getShowRatingLayout() {
        return ShowRatingLayout;
    }

    public void setShowRatingLayout(String showRatingLayout) {
        ShowRatingLayout = showRatingLayout;
    }

    public String getAtCatSelection() {
        return atCatSelection;
    }

    public void setAtCatSelection(String atCatSelection) {
        this.atCatSelection = atCatSelection;
    }

    public String getSingleLoveUrl() {
        return singleLoveUrl;
    }

    public void setSingleLoveUrl(String singleLoveUrl) {
        this.singleLoveUrl = singleLoveUrl;
    }

    public String getDoubleLoveUrl() {
        return doubleLoveUrl;
    }

    public void setDoubleLoveUrl(String doubleLoveUrl) {
        this.doubleLoveUrl = doubleLoveUrl;
    }
}
