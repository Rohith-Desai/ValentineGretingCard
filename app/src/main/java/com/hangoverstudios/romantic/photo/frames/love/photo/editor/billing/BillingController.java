package com.hangoverstudios.romantic.photo.frames.love.photo.editor.billing;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.billing.Security.verifyPurchase;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.commonMethods;

public class BillingController implements PurchasesUpdatedListener {
    public static final String PREF_FILE = "PHOTO_COLLAGE";
    public static final String PURCHASE_KEY = "removeads";
    String PRODUCT_ID = "removeads";
    Context mContext;
    UpdateBilling updateBilling;
    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //if purchase is acknowledged
                // Grant entitlement to the user. and restart activity
                savePurchaseValueToPref(true);
                if(updateBilling != null){
                    updateBilling.updateUI();
                }
                Toast.makeText(mContext, "Item Purchased", Toast.LENGTH_SHORT).show();
//                MainActivity.this.recreate(); // need to recreate the activity...,

            }
        }
    };
    private BillingClient billingClient;

    public BillingController(Context mContext) {
        this.mContext = mContext;
    }
    public BillingController(Context mContext,UpdateBilling updateBilling) {
        this.mContext = mContext;
        this.updateBilling = updateBilling;
    }

    public void billingInitialization() {
        billingClient = BillingClient.newBuilder(mContext)
                .enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(INAPP);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if (queryPurchases != null && queryPurchases.size() > 0) {
                        handlePurchases(queryPurchases);
                    }
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
                    else {
                        savePurchaseValueToPref(false);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private SharedPreferences getPreferenceObject() {
        return mContext.getSharedPreferences(PREF_FILE, 0);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    public boolean getPurchaseValueFromPref() {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false);
    }

    private void savePurchaseValueToPref(boolean value) {
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit();
    }

    public void handlePurchases(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            //if item is purchased
            if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(mContext, "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    if (!getPurchaseValueFromPref()) {
                        savePurchaseValueToPref(true);
//                        Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                        commonMethods.disableAds();
                        if(updateBilling != null){
                            updateBilling.updateUI();
                        }
//                        remove_ads.setVisibility(View.GONE);  // need to recreate the activity.
//                        this.recreate();
                    }
                }
            }
            //if purchase is pending
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(mContext,
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false);

                Toast.makeText(mContext, "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArW30RHIYqfOharZg8QEPtr0Lt2mVrf3fX0+ohShed3B/uNXZFFdI3Rt3Xf0NXojTRcv9MMiVovGmIiz7M1f22wYgmi9gX4je0TOJOZn3VOuvdpsXmxowNFGpsS+UNv53QwIHPMkoK2A37jz+lE6odIbRfLgyl0U/5cRSMGisd+KI4ElQFJjzCgw3O5a0u+zsknFlQH6GbyV3mBl0ZEbhX6Ab1OKPsWWV3wN010HdGSjq4vGj2KQf6l47NGprXsPsive4iAGSXRvjpTUbYnOwEHO2BuPncscDxK6gDtLwwDPCzFcjO9XQp8cEGEA08Rr/mLw07zdINaXvP8bVeIiILwIDAQAB";
            return verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;

        }

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

        //if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(INAPP);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if (alreadyPurchases != null) {
                handlePurchases(alreadyPurchases);
            }
        }
        //if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(mContext, "Purchase Canceled", Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            Toast.makeText(mContext, "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void showPurchaseDialog() {
        //check if service is already connected
        if (billingClient.isReady()) {
            initiatePurchase();
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(mContext).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase();
                    } else {
                        Toast.makeText(mContext, "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                }
            });
        }
    }

    private void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                billingClient.launchBillingFlow((Activity) mContext, flowParams);
                            } else {
                                //try to add item/product id "purchase" inside managed product in google play console
                                Toast.makeText(mContext, "Purchase Item not Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext,
                                    " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
