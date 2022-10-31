package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.ads.mediation.admob.AdMobAdapter;
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
import com.google.android.material.navigation.NavigationView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.billing.BillingController;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.billing.UpdateBilling;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.CustomDialogFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.service.AlarmEvngReceiver;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.service.AlarmReceiver;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.service.NotificationJobService;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.commonMethods;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.getAdSize;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.myClickAnim;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.populateUnifiedNativeAdView;

public class HomeActivity extends AppCompatActivity implements UpdateBilling{
    FrameLayout frameLayout;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 201;
    private static final int PERMISSION__STORAGE_REQUEST_CODE = 200;
    ImageView remove_ads;
    CardView remove_ads_card;
    BillingController bill;
    CardView romanticCard, greetingCard, creations_card, double_card, shapes_card, pip_card;
    Dialog mainDialog;
    boolean camera = false;
    //ImageView back_arrow;
    private FrameLayout adContainerView;
    private AdView adViewBanner1;
    private ImageView starOne, starTwo, starThree, starFour, starFive;
    private CardView rating_layout;
    private NativeAd nativeAd;
    boolean showAd = true;
    private static final int JOB_ID = 0;
    private JobScheduler mScheduler;

  //  DrawerLayout drawer;
  //  NavigationView navigationView;
   // ImageView navigation_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        romanticCard = findViewById(R.id.romantic_card);
        greetingCard = findViewById(R.id.greeting_card);
        creations_card = findViewById(R.id.creations_card);
        double_card = findViewById(R.id.double_card);
        shapes_card = findViewById(R.id.shape_card);
        pip_card = findViewById(R.id.pip_card);

        rating_layout = findViewById(R.id.rating_layout);
        starOne = findViewById(R.id.starOne);
        starTwo = findViewById(R.id.starTwo);
        starThree = findViewById(R.id.starThree);
        starFour = findViewById(R.id.starFour);
        starFive = findViewById(R.id.starFive);
        if (RemoteConfigValues.getOurRemote().getShowRatingLayout() != null
                && RemoteConfigValues.getOurRemote().getShowRatingLayout().equals("true")) {
            rating_layout.setVisibility(View.VISIBLE);
           /* if (!CommonMethods.getInstance().ratingIsDone(HomeActivity.this)) {
                rating_layout.setVisibility(View.VISIBLE);
            }*/

        }

        starFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingPlaystore();
            }
        });
        starFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });
        starThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });
        starTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });
        starOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });

//        back_arrow = findViewById(R.id.back_arrow);
//        back_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        myClickAnim(this, romanticCard);
        myClickAnim(this, greetingCard);
        myClickAnim(this, double_card);
        myClickAnim(this, shapes_card);
        myClickAnim(this, pip_card);
        myClickAnim(this, creations_card);

        if (getIntent().hasExtra("showAd")) {
            showAd = getIntent().getBooleanExtra("showAd", true);
        }
        if (showAd){
            CommonMethods.getInstance().onLaunchAD(HomeActivity.this);
        }
        romanticCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SingleMainActivity.class);
                startActivity(i);
            }
        });

        greetingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, GreetingMainActivity.class);
                startActivity(i);
            }
        });

        creations_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GalleryActivity.class));
            }
        });

        double_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, DoubleMainActivity.class);
                startActivity(i);
            }
        });

        shapes_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ShapeMainActivity.class);
                startActivity(i);
            }
        });

        pip_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "pip", Toast.LENGTH_SHORT).show();
            }
        });

        if (!checkStoragePermission() || !checkCameraPermission()) {
            showPermissionDialog();
        }


        if (RemoteConfigValues.getOurRemote().getShowNativeAd() != null) {
            if (RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")) {
                if (RemoteConfigValues.getOurRemote().getShowNativeAdOnMainScreen().equals("true")) {

                    admobAd();
                }
            }
        }

        adContainerView = findViewById(R.id.admob_banner_home);
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


        //billing related view and click listener
        remove_ads = findViewById(R.id.remove_ads);
        remove_ads_card= findViewById(R.id.remove_ads_card);
        if (RemoteConfigValues.getOurRemote().getEnableIAPflag() != null) {
            if (RemoteConfigValues.getOurRemote().getEnableIAPflag().equals("true")) {
                remove_ads.setVisibility(View.VISIBLE);
                remove_ads_card.setVisibility(View.VISIBLE);
            } else if (RemoteConfigValues.getOurRemote().getEnableIAPflag().equals("false")) {
                remove_ads.setVisibility(View.GONE);
                remove_ads_card.setVisibility(View.GONE);
            }
        }

        frameLayout = findViewById(R.id.native_ad_home);
        bill = new BillingController(this,this);
        bill.billingInitialization();
        if (bill.getPurchaseValueFromPref()) {
            commonMethods.disableAds();
            remove_ads.setVisibility(View.GONE);

            frameLayout.setVisibility(View.INVISIBLE);
            adContainerView.setVisibility(View.GONE);
//            HomeActivity.this.recreate();
        }

        remove_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bill.showPurchaseDialog();
            }
        });
        remove_ads_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill.showPurchaseDialog();
            }
        });

       /* drawer=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigation_click=findViewById(R.id.navigtaion_drawer);
        navigation_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener( this ) ;
        navigationView.setItemIconTintList(null);*/

        SharedPreferences prefs = getSharedPreferences("Notifications_Cal_Romant", MODE_PRIVATE);
        boolean startServ = prefs.getBoolean("STARTSERVICE", true);
        if (startServ) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("current time:  " + c.getTime());
            long old = c.getTimeInMillis();
            c.add(Calendar.DATE, 1);  // number of days to add
            c.set(Calendar.HOUR_OF_DAY, 10);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            long newT = c.getTimeInMillis();
            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent receiverIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, receiverIntent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            if (alarmMgr != null) {
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntent);
            }

            //Evening Alarm

            AlarmManager alarmMgrEvng = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent receiverIntentEvng = new Intent(getApplicationContext(), AlarmEvngReceiver.class);
            PendingIntent alarmIntentEvng = PendingIntent.getBroadcast(getApplicationContext(), 0, receiverIntentEvng, 0);

            Calendar calendarEvng = Calendar.getInstance();
            calendarEvng.setTimeInMillis(System.currentTimeMillis());
            calendarEvng.set(Calendar.HOUR_OF_DAY, 20);
            if (alarmMgrEvng != null) {
                alarmMgrEvng.setRepeating(AlarmManager.RTC_WAKEUP, calendarEvng.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, alarmIntentEvng);
            }

            SharedPreferences.Editor editor = getSharedPreferences("Notifications_Cal_Romant", MODE_PRIVATE).edit();
            editor.putBoolean("STARTSERVICE", false);
            editor.apply();


        }

    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        adViewBanner1 = new AdView(this);
        adViewBanner1.setAdUnitId(getString(R.string.admob_banner_id));
        adContainerView.removeAllViews();
        adContainerView.addView(adViewBanner1);
        Bundle bundle = new Bundle();
        bundle.putString("collapsible", "bottom");
        AdSize adSize = getAdSize(this, adContainerView);
        adViewBanner1.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();

        // Start loading the ad in the background.
        adViewBanner1.loadAd(adRequest);

        adViewBanner1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adContainerView.setBackgroundResource(0);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

    private void openFeedback() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.MyCustomTheme);
        alertDialog.setTitle(R.string.send_us_feedback);
        alertDialog.setMessage(R.string.share_experience_with_us);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "hangoverstudios.mobile@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject:Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
              //  CommonMethods.getInstance().ratingDone(HomeActivity.this);
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void ratingPlaystore() {
       // CommonMethods.getInstance().ratingDone(HomeActivity.this);
        Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(ratingIntent);
    }

    public boolean checkStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void requestStoragePermission() {

        ActivityCompat.requestPermissions((Activity) this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION__STORAGE_REQUEST_CODE);
    }

    private void showStoragePermisionDialog() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this, R.style.MyCustomTheme);
        dialog.setMessage(getString(R.string.permission_request_msg));
        dialog.setTitle(getString(R.string.permission_request));
        dialog.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


    public boolean checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION__STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    updateList(false);

                    if (readAccepted && writeAccepted) {
                        if (camera) {
                            mainDialog.dismiss();
                        } /*else {
                            if (savedGallery) {
                                startActivity(new Intent(HomeActivity.this, GalleryActivity.class));
                            } else {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryIntent.setType("image/*");
                                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
                            }

                        }*/
                    } else {
                        showStoragePermisionDialog();
                    }

                }
                break;
            case PERMISSION_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        if(mainDialog!=null){
                            if(mainDialog.isShowing()){
                                mainDialog.dismiss();
                            }
                        }
                    }
                } else {
                    showCamPermisionDialog();
                }
                break;
        }
    }

    private void showCamPermisionDialog() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this, R.style.MyCustomTheme);
        dialog.setMessage(getString(R.string.permission_request_msg));
        dialog.setTitle(getString(R.string.permission_request));
        dialog.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        requestCameraPermission();

                    }
                });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void showPermissionDialog() {
        mainDialog = new Dialog(HomeActivity.this);
        Window window = mainDialog.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimationTransition;
        mainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mainDialog.setCancelable(false);
        mainDialog.setContentView(R.layout.permission_dialog);
        TextView msgTxt = mainDialog.findViewById(R.id.dialog_msg);
        TextView msgSubTxt = mainDialog.findViewById(R.id.dialog_msg_sub);
        msgTxt.setText("Storage Permission");
        msgSubTxt.setText("Need storage and camera\npermission to access your photos\nfrom gallery and take picture");
        TextView dialogOk = mainDialog.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = true;
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else if (!checkStoragePermission()) {
                    requestStoragePermission();
                }
            }
        });
        //TextView dialogCancel = dialog.findViewById(R.id.dialog_cancel);
        /*dialogCancel.setVisibility(View.VISIBLE);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        mainDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        CommonMethods.getInstance().onExitAD(HomeActivity.this);

        startActivity(new Intent(HomeActivity.this, ExitActivity.class));
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CustomDialogFragment editNameDialogFragment = new CustomDialogFragment();
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private void admobAd() {
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
                        if (HomeActivity.this.nativeAd != null) {
                            HomeActivity.this.nativeAd.destroy();
                        }
                        HomeActivity.this.nativeAd = nativeAd;
                        FrameLayout frameLayout = findViewById(R.id.native_ad_home);
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
    public void updateUI() {
        commonMethods.disableAds();
        remove_ads.setVisibility(View.GONE);
        remove_ads_card.setVisibility(View.GONE);

        frameLayout.setVisibility(View.INVISIBLE);
        adContainerView.setVisibility(View.GONE);
    }

    public void playstore() {
      //  CommonMethods.getInstance().ratingDone(HomeActivity.this);
        Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(ratingIntent);
    }

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId() ;


        if (id == R.id. nav_share ) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out the App at: https://play.google.com/store/apps/details?id=" +getPackageName() );
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id. nav_rate_us ) {
            playstore();
        }
        else if (id == R.id. app_no_1 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.photo.suit.editor"));
            startActivity(appno);
        }else if (id == R.id. app_no_2 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hs.rtovehicledetail.vahan.vehicleregistrationdetails.rtoapp"));
            startActivity(appno);
        }
        else if (id == R.id. app_no_3 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.vehicleinfo"));
            startActivity(appno);
        }else if (id == R.id. app_no_4 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.romantic.photo.frames.love.photo.editor"));
            startActivity(appno);
        }

        else if (id == R.id. app_no_7 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.photo.photocollage.photoeditor.collagemaker"));
            startActivity(appno);
        }
        else if (id == R.id. app_no_11 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.men.photo.suite.editor.app"));
            startActivity(appno);
        }else if (id == R.id. app_no_12 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.reverse.video.effect"));
            startActivity(appno);
        }
        else if (id == R.id. app_no_21 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.invitationmaker.invitation.maker.birthday.card"));
            startActivity(appno);
        }
        else if (id == R.id. app_no_23 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.video.flip.rotate"));
            startActivity(appno);
        }else if (id == R.id. app_no_26 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.statusdp.whatsappstatussaver.saveit"));
            startActivity(appno);
        }
        else if (id == R.id. app_no_27 ) {
            Intent appno = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hangoverstudios.daily.expensemanager"));
            startActivity(appno);
        }
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        drawer.closeDrawer(GravityCompat. START ) ;
        return true;
    }*/

}