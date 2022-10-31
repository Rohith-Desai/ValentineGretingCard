package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SingleFrameEditActivity;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity.greetingCardEditActivity;

public class GreetingCardsPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int STRING = 3;
        private Context mContext;
        ArrayList<Integer> mRecyclerViewItems;
        String typeOfD = "";

        public GreetingCardsPreviewAdapter(Context mContext, ArrayList<Integer> mResources, String type) {
            typeOfD = type;
            this.mContext = mContext;
            mRecyclerViewItems = new ArrayList<>();
            this.mRecyclerViewItems = mResources;
        }

        @Override
        public int getItemCount() {
            return mRecyclerViewItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            return STRING;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View menuItemLayoutView2 = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.single_greeting_preview_item, parent, false);
            return new GreetingCardsPreviewAdapter.ViewHolder(menuItemLayoutView2);

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final GreetingCardsPreviewAdapter.ViewHolder viewHolder = (GreetingCardsPreviewAdapter.ViewHolder) holder;
            Animation a = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            double devicewidth = displaymetrics.widthPixels / 2.13;

            //if you need 4-5-6 anything fix imageview in height
            double deviceheight = displaymetrics.heightPixels / 2.8;

            viewHolder.greetingcard.getLayoutParams().width = (int)devicewidth;

            //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
            viewHolder.greetingcard.getLayoutParams().height = (int)deviceheight;


            Glide.with(mContext).load(mRecyclerViewItems.get(position)).into(viewHolder.greetingPreviewItem);

//            viewHolder.greetingPreviewItem.setImageResource(mRecyclerViewItems.get(position));
            viewHolder.greetingPreviewItem.startAnimation(a);
//            viewHolder.greetingPreviewItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mContext,GreetingCardEditActivity.class);
//                    intent.putExtra("position",position);
//                    mContext.startActivity(intent);
//                }
//            });
            viewHolder.greetingPreviewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SingleFrameEditActivity.class);
                    intent.putExtra("position",position);
                    mContext.startActivity(intent);
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView greetingPreviewItem;
            private CardView greetingcard;

            ViewHolder(View view) {
                super(view);
                greetingPreviewItem = view.findViewById(R.id.single_greeting_card_img_item);
                greetingcard=view.findViewById(R.id.greetingcard);
            }
        }
    }
