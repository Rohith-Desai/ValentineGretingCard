package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingEditActivity;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.DataHolder;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.UnifiedNativeAdViewHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.myClickAnim;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.populateNativeAdView;

public class GreetingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int STRING = 404;
    static final int NATIVE_UnifiedNativeAd = 10519;
    public ArrayList<Object> mData;
    public ArrayList<Object> downloadList;
    public Context mContext;
    int type;

    public GreetingsAdapter(ArrayList<Object> mData, Context CONTEXT, int type) {
        this.mData = mData;
        this.mContext = CONTEXT;
        this.type = type;
        if (DataHolder.getDataHolder().getGreetingsList() != null) {
            downloadList = DataHolder.getDataHolder().getGreetingsList();
        }
    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = mData.get(position);
        if (recyclerViewItem instanceof String) {
            return STRING;
        } else if (recyclerViewItem instanceof UnifiedNativeAd) {
            return NATIVE_UnifiedNativeAd;
        }

        return STRING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case NATIVE_UnifiedNativeAd:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.ad_unified_recycle,
                        parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case STRING:
            default:
                View menuItemLayoutView2 = null;
                if (type == 1) {
                    menuItemLayoutView2 = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.greeting_item, parent, false);
                } else {
                    menuItemLayoutView2 = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.small_greeting_item, parent, false);
                }

                return new GreetingHolder(menuItemLayoutView2);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case NATIVE_UnifiedNativeAd:
                NativeAd nativeAd = (NativeAd) mData.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder1).getAdView());
                break;
            case STRING:
                final GreetingHolder holder = (GreetingHolder) holder1;
                Animation a = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
                File mediaStorageDir = null;

                if (type == 1) {
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    //if you need three fix imageview in width
                    double devicewidth = displaymetrics.widthPixels / 2.13;

                    //if you need 4-5-6 anything fix imageview in height
                    double deviceheight = displaymetrics.heightPixels / 2.8;

                    holder.greetingtwo.getLayoutParams().width = (int) devicewidth;

                    //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
                    holder.greetingtwo.getLayoutParams().height = (int) deviceheight;

                }
                if (downloadList.get(position) != null) {
                    final String img1 = downloadList.get(position).toString();
                    if (position < 5) {
                        holder.download_icon.setVisibility(View.GONE);
                        Glide.with(mContext).load(mData.get(position).toString())
                                .placeholder(R.drawable.loading_img)
                                .into(holder.image);
                    } else {
                        if (img1.contains("https")) {
                            String[] paths = img1.split("/");


                            String getpath = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)?
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/" +"Romantic Photo Editor" + "/Frames"
                                    :Environment.getExternalStorageDirectory().toString()+ "/" + "Romantic Photo Editor" + "/Frames";


                            mediaStorageDir = new File(getpath +"/"+ paths[paths.length - 1]);

                            if (!mediaStorageDir.exists()) {
                                holder.download_icon.setVisibility(View.VISIBLE);
                            } else {
                                holder.download_icon.setVisibility(View.GONE);
                            }


                            holder.image.startAnimation(a);

                            Glide.with(mContext).load(mData.get(position).toString())
                                    .placeholder(R.drawable.loading_img)
                                    .into(holder.image);

                        }
                        // loading images from drawable folder
//                        else {
//                            holder.download_icon.setVisibility(View.GONE);
//                            holder.image.startAnimation(a);
//                            Glide.with(mContext).load(getImage(mData.get(position).toString()))
//                                    .placeholder(R.drawable.loading_img)
//                                    .into(holder.image);
//                        }
                    }


                    final File finalMediaStorageDir = mediaStorageDir;
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position <= 4) {
                                mContext.startActivity(new Intent(mContext, GreetingEditActivity.class).putExtra("PATH", downloadList.get(position).toString()));
                            } else {
                                if (finalMediaStorageDir != null) {
                                    if (!finalMediaStorageDir.exists()) {
                                        showDownLoadDialog(downloadList.get(position).toString(), holder, position);
                                    } else {
                                        mContext.startActivity(new Intent(mContext, GreetingEditActivity.class).putExtra("PATH", downloadList.get(position).toString()));
                                    }
                                }

                            }

                        }
                    });
                    holder.download_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDownLoadDialog(downloadList.get(position).toString(), holder, position);
                        }
                    });
                    myClickAnim(mContext, holder.greetingtwo);
                    myClickAnim(mContext, holder.download_icon);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class GreetingHolder extends RecyclerView.ViewHolder {
        ImageView image, download_icon;
        CardView greetingtwo;

        GreetingHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.greeting_img);
            greetingtwo = itemView.findViewById(R.id.greetingtwo);
            download_icon = itemView.findViewById(R.id.download_icon);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        return drawableResourceId;
    }

    private void showDownLoadDialog(final String img, final GreetingHolder stringViewHolder1, final int position1) {

        android.app.AlertDialog.Builder dialogPermision = new android.app.AlertDialog.Builder(mContext, R.style.MyCustomTheme);
        dialogPermision.setCancelable(false);
        dialogPermision.setMessage(R.string.download_msg);
        dialogPermision.setTitle(R.string.require_attention);

        dialogPermision.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogPermision,
                                        int which) {
                        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                        View alertView = inflater.inflate(R.layout.download_dialog, null);

//                        loadUnifiedNativeAD(alertView);

                        final Dialog dialog = new AlertDialog.Builder(mContext, R.style.MyCustomTheme)
                                .setView(alertView)
                                .create();
                        dialog.show();
                        dialog.setCancelable(false);

                        Glide.with(mContext)
                                .asBitmap()
                                .load(img)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        saveBitMap(resource, img);
                                        stringViewHolder1.download_icon.setVisibility(View.GONE);
                                        int delay = 2000;
//                                        if (CloudDatabase.getFirebase().getDownloadDialogDelay() != null) {
//                                            delay = Integer.parseInt(CloudDatabase.getFirebase().getDownloadDialogDelay());
//                                        }
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!((Activity) mContext).isFinishing() && dialog != null) {
                                                    if (dialog.isShowing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                                setAnimation(stringViewHolder1.image, position1);
//                                                if (CloudDatabase.getFirebase().getInterstitialAfterDownload() != null) {
//                                                    if (CloudDatabase.getFirebase().getInterstitialAfterDownload().equals("true")) {
//                                                        AppHelper.displayFBAD(mContext);
//                                                    }
//                                                }

                                            }
                                        }, delay);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
                    }
                });
        dialogPermision.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogPermision, int which) {
                dialogPermision.dismiss();
            }
        });
        android.app.AlertDialog alertDialog = dialogPermision.create();
        alertDialog.show();
    }

    private void saveBitMap(Bitmap resource, String path) {


        Bitmap r = resource;
        String p = path;

        File pictureFile = getOutputMediaFile(p);
        if (pictureFile == null) {

            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            r.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
//            notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private File getOutputMediaFile(String path) {

        String[] paths = path.split("/");

        String getpath = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)?
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/" +"Romantic Photo Editor"+ "/Frames"
                :Environment.getExternalStorageDirectory().toString()+ "/" + "Romantic Photo Editor" + "/Frames";




        File mediaStorageDir = null;
        mediaStorageDir = new File(getpath);
        if (!mediaStorageDir.exists()) {
            if (mediaStorageDir.mkdirs()) {

            }
        }

//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = paths[paths.length - 1];
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
        viewToAnimate.startAnimation(anim);
    }
}
