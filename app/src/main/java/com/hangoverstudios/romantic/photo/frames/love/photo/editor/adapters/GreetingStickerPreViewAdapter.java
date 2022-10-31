package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

import java.util.ArrayList;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.EditScreen.eds;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SingleFrameEditActivity.sfeObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingEditActivity.eObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.AddStickerFragment.addStickerFragment;

public class GreetingStickerPreViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int STRING = 3;
    private Context mContext;
    ArrayList<Integer> mRecyclerViewItems;
    String typeOfD = "";
    String from_option = "";

    public GreetingStickerPreViewAdapter(Context mContext, ArrayList<Integer> mResources, String type, String option) {
        typeOfD = type;
        this.mContext = mContext;
        mRecyclerViewItems = new ArrayList<>();
        this.mRecyclerViewItems = mResources;
        from_option = option;
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
        return new GreetingStickerPreViewAdapter.ViewHolder(menuItemLayoutView2);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final GreetingStickerPreViewAdapter.ViewHolder viewHolder = (GreetingStickerPreViewAdapter.ViewHolder) holder;
        Animation a = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
        viewHolder.stickerImg.setImageResource(mRecyclerViewItems.get(position));
        viewHolder.stickerImg.startAnimation(a);
        viewHolder.stickerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                //greetingCardEditActivity.mContentRootView.setVisibility(View.VISIBLE);
//                addStickerFragment.INT_RESOURCE_ID_TO_COPY = mRecyclerViewItems.get(position);
//                addStickerFragment.mContentRootView.setVisibility(View.VISIBLE);
//                addStickerFragment.addStickerView(mRecyclerViewItems.get(position));
                //greetingCardEditActivity.stickerRecyclerView.setVisibility(View.GONE);

                if(from_option.equals("single")){
                    sfeObj.addStickerView(mRecyclerViewItems.get(position));
                }else if(from_option.equals("greeting")){
                    eObj.addStickerView(mRecyclerViewItems.get(position));
                }else if(from_option.equals("Double")){
                    eds.addStickerView(mRecyclerViewItems.get(position));
                }
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
