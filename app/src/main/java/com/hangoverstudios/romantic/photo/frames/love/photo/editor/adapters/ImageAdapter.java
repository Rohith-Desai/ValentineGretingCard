package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SlideShowActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GalleryActivity.galleryActivity;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.myClickAnim;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    public ArrayList<String> mData;
    private LayoutInflater mInflater;
    public Context CONTEXT;
    public boolean isLongSelection = false, selectAll;
    public ArrayList<Integer> selectedItems = new ArrayList<>();
    int type;

    public ImageAdapter(Context context, ArrayList<String> data, boolean selectAll) {
        CONTEXT = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.selectAll = selectAll;
    }
    public ImageAdapter(Context context, ArrayList<String> data, int type) {
        CONTEXT = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.type=type;

    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if (type==1){
            view = mInflater.inflate(R.layout.image_item_main, parent, false);

        }
        else {
            view = mInflater.inflate(R.layout.image_item, parent, false);
        }

        return new ImageAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder holder, final int position) {
        if (type==1){

            Animation a = AnimationUtils.loadAnimation(CONTEXT, R.anim.scale_up);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) CONTEXT).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            double devicewidth = displaymetrics.widthPixels / 2.13;

            //if you need 4-5-6 anything fix imageview in height
            double deviceheight = displaymetrics.heightPixels / 2.8;

            holder.greetingtwo.getLayoutParams().width = (int)devicewidth;

            //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
            holder.greetingtwo.getLayoutParams().height = (int)deviceheight;

            Glide.with(CONTEXT).load(mData.get(position)).into(holder.images);
            holder.images.startAnimation(a);
            Log.e("image",mData.get(position));
            holder.images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(CONTEXT, SlideShowActivity.class);
                    intent.putExtra("paths", mData);
                    intent.putExtra("matchPath", mData.get(position));
                    intent.putExtra("know_activity",1);
                    CONTEXT.startActivity(intent);

                }
            });

        }
        else {
            Log.e("imagecheck",mData.get(position));

            Glide.with(CONTEXT).load(mData.get(position)).into(holder.image);
            Log.e("INDEX", "select all : " + selectAll);
            if (selectAll) {
                selectedItems.clear();
                for (int i = 0; i < mData.size(); i++) {
                    selectedItems.add(mData.indexOf(mData.get(i)));
                    Log.e("INDEX", "selected items all : " + selectedItems.toString());
                }
                galleryActivity.selectCount.setText("selected : " + getItemCount());
                setImageFilter(holder);
            } else {
                if (selectedItems.size() == 0) {
                    removeImageFilter(holder);
                    galleryActivity.selectCount.setText("selected : ");
                    galleryActivity.toolOptions.setVisibility(View.VISIBLE);
                    galleryActivity.selectionOptions.setVisibility(View.GONE);
                } else {
                    galleryActivity.toolOptions.setVisibility(View.GONE);
                    galleryActivity.selectionOptions.setVisibility(View.VISIBLE);
                }
            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    if (isLongSelection) {
                        if (selectedItems.contains(position)) {
                            selectedItems.remove(selectedItems.indexOf(position));
                            galleryActivity.selectCount.setText("selected : " + selectedItems.size());
                            removeImageFilter(holder);
                        } else {
                            selectedItems.add(position);
                            galleryActivity.selectCount.setText("selected : " + selectedItems.size());
                            setImageFilter(holder);
                        }
                    } else {
                        Intent intent = new Intent(CONTEXT, SlideShowActivity.class);
                        intent.putExtra("paths", mData);
                        intent.putExtra("matchPath", mData.get(position));
                        CONTEXT.startActivity(intent);
                    }
                }
            });
            holder.image.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onLongClick(View v) {
                    isLongSelection = true;
                    galleryActivity.selectCount.setText("selected : 1");
                    galleryActivity.selectionOptions.setVisibility(View.VISIBLE);
                    galleryActivity.toolOptions.setVisibility(View.GONE);
                    selectedItems.add(position);
                    setImageFilter(holder);
                    return true;
                }
            });
            myClickAnim(CONTEXT, holder.greetingtwo);
//            myClickAnim(CONTEXT,holder.download_icon);

        }

    }

    private void setImageFilter(ViewHolder holder) {
        holder.image.setColorFilter(CONTEXT.getResources().getColor(R.color.transparent_blue));
    }

    private void removeImageFilter(ViewHolder holder) {
        holder.image.setColorFilter(CONTEXT.getResources().getColor(R.color.transparent));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView images;
        CardView greetingtwo;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            images = itemView.findViewById(R.id.greeting_img);
            greetingtwo=itemView.findViewById(R.id.greetingtwo);
        }
    }
}



