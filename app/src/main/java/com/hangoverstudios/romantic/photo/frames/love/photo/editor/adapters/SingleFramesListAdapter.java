package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

import java.util.ArrayList;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SubGreetingActivity.sGreeting;

public class SingleFramesListAdapter extends RecyclerView.Adapter<SingleFramesListAdapter.GreetingHolder> {

    private static final int STRING = 404;

    public ArrayList<Object> mData;
    public Context mContext;

    public SingleFramesListAdapter(ArrayList<Object> mData, Context CONTEXT) {
        this.mData = mData;
        this.mContext = CONTEXT;
    }

    @NonNull
    @Override
    public GreetingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {

            case STRING:
            default:
                View menuItemLayoutView2 = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.small_greeting_item, parent, false);
                return new GreetingHolder(menuItemLayoutView2);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull GreetingHolder holder, final int position) {

        Glide.with(mContext).load(getImage(mData.get(position).toString()))
                .placeholder(R.drawable.loading_img)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, GreetingEditActivity.class).putExtra("PATH",mData.get(position).toString()));
//                sGreeting.setBackgroundOfGreeting(mData.get(position).toString());
//                sGreeting.sendBack(mData.get(position).toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = mData.get(position);
        if (recyclerViewItem instanceof String) {
            return STRING;
        }
        return STRING;
    }

    public static class GreetingHolder extends RecyclerView.ViewHolder {
        ImageView image;

        GreetingHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.greeting_img);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        return drawableResourceId;
    }
}
