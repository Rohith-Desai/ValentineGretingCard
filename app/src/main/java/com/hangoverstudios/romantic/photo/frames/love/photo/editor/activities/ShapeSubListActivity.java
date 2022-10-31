package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.SmallGreetingsAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.getAdSize;

public class ShapeSubListActivity extends AppCompatActivity {
    private FrameLayout adContainerView;
    private AdView adViewBanner1;
    ImageView back_arrow;
    public  static  ShapeSubListActivity shapeObj;
    RecyclerView shape_sub_recycle;
    ArrayList<Object> shapeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shapeObj = this;
        setContentView(R.layout.activity_shape_sub_list);
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        shape_sub_recycle = findViewById(R.id.shape_sub_recycle);
        showFramesList();
        adContainerView = findViewById(R.id.admob_banner_shapeSubList);
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
        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        adViewBanner1 = new AdView(this);
        adViewBanner1.setAdUnitId(getString(R.string.admob_banner_id));
        adContainerView.removeAllViews();
        adContainerView.addView(adViewBanner1);

        AdSize adSize = getAdSize(this, adContainerView);
        adViewBanner1.setAdSize(adSize);

        AdRequest adRequest =
                new AdRequest.Builder().build();

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
    private void showFramesList() {
        shapeList.clear();
        prepareList();
//        shapeList  = DataHolder.getDataHolder().getSingleFramesList();
        if(shapeList!=null){
            if(shapeList.size() > 0){
//            LinearLayoutManager layoutManager
//                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(ShapeSubListActivity.this, 2);
                shape_sub_recycle.setLayoutManager(gridLayoutManager);
                SmallGreetingsAdapter greetingsAdapter = new SmallGreetingsAdapter(shapeList, ShapeSubListActivity.this,3);
                shape_sub_recycle.setAdapter(greetingsAdapter);
            }
        }
        shape_sub_recycle.setVisibility(View.VISIBLE);
    }
    private void prepareList() {
//        for (int i = 1; i <= 5; i++) {
//            shapeList.add("valentine_card_" + i);
//        }
        getAllSavedGreetings();

    }

    private void getAllSavedGreetings() {

       /* File directory = new File(Environment.getExternalStorageDirectory()
                + "/root/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");*/
        String getpath = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ?
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + "Romantic Photo Editor" + "/Frames"
                : Environment.getExternalStorageDirectory().toString() + "/" + "Romantic Photo Editor" + "/Frames";
        File directory = new File(getpath);

        if (directory.exists()) {
            File[] files = directory.listFiles();
            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                        return -1;
                    } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }
            });
            if (files != null && files.length > 0) {
                for (File file : files) {

                    if(file.getName().contains("shape_frames_"))
                        shapeList.add(file.getAbsolutePath().toString());
                }
                System.out.println(files.length);
            } else {
//                finish();
                Toast.makeText(ShapeSubListActivity.this, "No albums found.", Toast.LENGTH_SHORT).show();
            }
        }
//        Collections.reverse(framesList1);


    }

    public void sendBack(String toString) {
        Intent intent = new Intent();
        intent.putExtra("GREETING_PATH", toString);
        setResult(RESULT_OK, intent);
        finish();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        CommonMethods.getInstance().showGoogleAd(ShapeSubListActivity.this);
    }
}