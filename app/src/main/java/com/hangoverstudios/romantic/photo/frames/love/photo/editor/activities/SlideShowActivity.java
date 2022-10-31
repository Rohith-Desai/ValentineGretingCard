package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.BuildConfig;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

import java.io.File;
import java.util.ArrayList;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GalleryActivity.galleryActivity;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity.mainActivity;

public class SlideShowActivity extends Activity {
    private ViewFlipper myViewFlipper;
    private float initialXPoint;
    ArrayList<String> imgPaths = new ArrayList<>();
    String matchPath = null;
    int index;
    ImageView deleteImage, selectShare, setWallpaper;
    LinearLayout selectBackToGallery;
    TextView imgName;
    String deletePath;
    int know_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        matchPath = getIntent().getStringExtra("matchPath");
        know_activity = getIntent().getIntExtra("know_activity", 0);

        myViewFlipper = (ViewFlipper) findViewById(R.id.myflipper);
        if (know_activity == 1) {
            if (mainActivity != null) {
                imgPaths = mainActivity.getImagesFromDevice();
            }

        } else {
            if (galleryActivity != null) {
                imgPaths = galleryActivity.getImagesFromDevice();
            }
        }

        for (String key : imgPaths) {
            if (key.equals(matchPath)) {
                index = imgPaths.indexOf(matchPath);
                break;
            }
        }
        for (int i = 0; i < imgPaths.size(); i++) {
            updateImage(imgPaths.get(i), -1);
        }
        if (imgPaths.size() >= 1) {
            if (imgPaths.size() > myViewFlipper.getDisplayedChild())
                deletePath = imgPaths.get(myViewFlipper.getDisplayedChild());
        }
        imgName = findViewById(R.id.img_name);
        if (myViewFlipper.getDisplayedChild() < imgPaths.size())
            imgName.setText(imgPaths.get(myViewFlipper.getDisplayedChild()).substring(imgPaths.get(myViewFlipper.getDisplayedChild()).lastIndexOf("/") + 1));
        deleteImage = findViewById(R.id.delete_image);
        setWallpaper = findViewById(R.id.set_wallpaper);
        selectShare = findViewById(R.id.select_share);
        selectBackToGallery = findViewById(R.id.select_back);
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletePath != null) {
                    showDeleteDialog();
                }
            }
        });
        selectBackToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (know_activity != 1) {

                    if (galleryActivity != null)
                        galleryActivity.updateList(false);

                }
                if (mainActivity != null)
                    mainActivity.updateList(false);
                onBackPressed();
            }
        });
        selectShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deletePath!=null){
                    if(!deletePath.equals("")) {
                        setWallpaper(new File(deletePath));
                    }
                }
            }
        });
    }

    private void updateImage(String path, int localIndex) {
        if (localIndex == -1) {
            ImageView image = new ImageView(getApplicationContext());
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(SlideShowActivity.this).load(path).into(image);
            myViewFlipper.addView(image);
            myViewFlipper.setDisplayedChild(index);
        } else {
            ImageView image = new ImageView(getApplicationContext());
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(SlideShowActivity.this).load(path).into(image);
            myViewFlipper.addView(image);
            myViewFlipper.setDisplayedChild(localIndex);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (know_activity == 1) {
            mainActivity.updateList(false);

        } else {
            if(galleryActivity!=null){
                galleryActivity.updateList(false);
            }
            if (mainActivity != null) {
                mainActivity.updateList(false);
            }

        }
    }

    public void shareImage() {
        ArrayList<Uri> files = new ArrayList<Uri>();
        ArrayList<String> deleteFile = new ArrayList<>();
        deleteFile.add(imgPaths.get(myViewFlipper.getDisplayedChild()));
        for (int i = 0; i < deleteFile.size(); i++) {

            Uri uri = FileProvider.getUriForFile(SlideShowActivity.this, getApplicationContext().getPackageName()+ ".provider", new File(deleteFile.get(i)));
            files.add(uri);
        }
        Intent shareIntent;
        shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    public void shareImages(String path) {
        ArrayList<Uri> files = new ArrayList<Uri>();
        Uri uri = FileProvider.getUriForFile(SlideShowActivity.this, getApplicationContext().getPackageName() + ".provider", new File((String) path));
        files.add(uri);
        Intent shareIntent;
        shareIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    public void showDeleteDialog() {
        final Dialog dialog = new Dialog(SlideShowActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_custom_dialog);
        TextView msgTxt = dialog.findViewById(R.id.dialog_msg);
        TextView msgSubTxt = dialog.findViewById(R.id.dialog_msg_sub);
        msgTxt.setText("Conform Delete.");
        msgSubTxt.setText("Are you sure want to delete selected items permanently.");
        TextView dialogOk = dialog.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage(imgPaths.get(myViewFlipper.getDisplayedChild()));
                if (imgPaths != null && imgPaths.size() > 0) {
                    myViewFlipper.removeViewAt(myViewFlipper.getDisplayedChild());
                } else {
                    if (know_activity == 1) {
                        mainActivity.updateList(false);

                    } else {

                        galleryActivity.updateList(false);
                        if (mainActivity != null) {
                            mainActivity.updateList(false);
                        }

                    }
                    onBackPressed();
                }
                dialog.dismiss();
            }
        });
        TextView dialogCancel = dialog.findViewById(R.id.dialog_cancel);
        dialogCancel.setVisibility(View.VISIBLE);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void removeImage(String path) {
        File fdelete = new File(path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                if (know_activity == 1) {
                    imgPaths = mainActivity.getImagesFromDevice();

                } else {

                    imgPaths = galleryActivity.getImagesFromDevice();

                }
                // imgPaths = galleryActivity.getImagesFromDevice();
                System.out.println("file Deleted :");
            } else {
                System.out.println("file not Deleted :");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialXPoint = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalx = event.getX();
                if (initialXPoint > finalx) {
                    if (myViewFlipper.getDisplayedChild() == imgPaths.size() - 1)
                        break;
                    else {

                        if ( myViewFlipper.getDisplayedChild() < imgPaths.size()){

                            myViewFlipper.setInAnimation(this, R.anim.in_from_right);
                            myViewFlipper.setOutAnimation(this, R.anim.out_from_left);
                            myViewFlipper.showNext();
                            // myViewFlipper.setInAnimation(AnimationUtils.loadAnimation(SlideShowActivity.this,R.anim.left_to_right));

                            imgName.setText(imgPaths.get(myViewFlipper.getDisplayedChild()).substring(imgPaths.get(myViewFlipper.getDisplayedChild()).lastIndexOf("/") + 1));
                            deletePath = imgPaths.get(myViewFlipper.getDisplayedChild());

                        }

                    }
                } else {
                    if (myViewFlipper.getDisplayedChild() == 0)
                        break;
                    else {
                        myViewFlipper.setInAnimation(this, R.anim.in_from_left);
                        myViewFlipper.setOutAnimation(this, R.anim.out_from_right);
                        myViewFlipper.showPrevious();
                        //myViewFlipper.setInAnimation(AnimationUtils.loadAnimation(SlideShowActivity.this,R.anim.right_to_left));
                        imgName.setText(imgPaths.get(myViewFlipper.getDisplayedChild()).substring(imgPaths.get(myViewFlipper.getDisplayedChild()).lastIndexOf("/") + 1));
                        deletePath = imgPaths.get(myViewFlipper.getDisplayedChild());
                    }
                }
                break;
        }
        return false;
    }

    private void setWallpaper(final File file) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SlideShowActivity.this);
        dialog.setTitle("Wallpaper")
                .setIcon(R.drawable.wallpaper)
                .setMessage("Are you sure you want to set wallpaper!")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                        WallpaperManager myWallpaperManager
                                = WallpaperManager.getInstance(getApplicationContext());
//                                try {

                                    /*getSharedPreferences("TransparentWallpaper", MODE_PRIVATE)
                                            .edit().putBoolean("MIRROR", false).apply();
                                    getSharedPreferences("TransparentWallpaper", MODE_PRIVATE)
                                            .edit().putBoolean("TRANSPARENT", false).apply();
                                    Intent lintent = new Intent(SetWallpaper.this, FloatingWidgetService.class);
                                    stopService(lintent);*/

                      /*  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(getIntent().getStringExtra("walImage")));
                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/LatestShare.jpg";
                        OutputStream out = null;
                        File file = new File(path);
                        try {
                            out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        Intent setAs = new Intent(Intent.ACTION_ATTACH_DATA);
                        setAs.addCategory(Intent.CATEGORY_DEFAULT);
                        Uri sourceUri = FileProvider.getUriForFile(SlideShowActivity.this, getApplicationContext().getPackageName() + ".provider", file);
                        setAs.putExtra("mimeType", "image/*");
                        setAs.setData(sourceUri);
                        setAs.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(setAs, "Set As"));

//                                    myWallpaperManager.setResource(Integer.parseInt(getIntent().getStringExtra("walImage")));
//                                    Toast.makeText(SetWallpaper.this, "Wallpaper updated", Toast.LENGTH_SHORT).show();
//                                } catch (IOException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
                    }
                });
        if (!isFinishing()){
            dialog.show();
        }


    }
}
