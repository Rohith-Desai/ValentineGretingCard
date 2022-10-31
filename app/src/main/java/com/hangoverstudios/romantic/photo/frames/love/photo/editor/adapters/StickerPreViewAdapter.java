package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity;

import java.util.ArrayList;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.os.Environment;
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
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;

        import java.util.ArrayList;
        import java.util.List;
        import android.widget.TextView;
        import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
        import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity.greetingCardEditActivity;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.AddStickerFragment.addStickerFragment;

public class StickerPreViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int STRING = 3;
    private Context mContext;
    ArrayList<Integer> mRecyclerViewItems;
    String typeOfD = "";

    public StickerPreViewAdapter(Context mContext, ArrayList<Integer> mResources, String type) {
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
                R.layout.sticker_item, parent, false);
        return new StickerPreViewAdapter.ViewHolder(menuItemLayoutView2);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final StickerPreViewAdapter.ViewHolder viewHolder = (StickerPreViewAdapter.ViewHolder) holder;
        Animation a = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
        viewHolder.stickerImg.setImageResource(mRecyclerViewItems.get(position));
        viewHolder.stickerImg.startAnimation(a);
        viewHolder.stickerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //greetingCardEditActivity.mContentRootView.setVisibility(View.VISIBLE);
                addStickerFragment.INT_RESOURCE_ID_TO_COPY = mRecyclerViewItems.get(position);
                addStickerFragment.mContentRootView.setVisibility(View.VISIBLE);
                addStickerFragment.addStickerView(mRecyclerViewItems.get(position));
                //greetingCardEditActivity.stickerRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView stickerImg;

        ViewHolder(View view) {
            super(view);
            stickerImg = view.findViewById(R.id.sticker_img);
        }
    }
}
