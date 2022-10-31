package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

import java.util.ArrayList;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.DoubleSubListActivity.doubleObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.DoubleTrendSubListActivity.doubleTrendObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingTrendSubListActivity.greetingTrendObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.ShapeSubListActivity.shapeObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SingleSubListActivity.singObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SingleTrendSubListActivity.singTrendObj;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SubGreetingActivity.sGreeting;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.myClickAnim;

public class SubDoubleAdapter extends RecyclerView.Adapter<SubDoubleAdapter.SubDoubleHolder> {

    private static final int STRING = 404;

    public ArrayList<Object> mData;
    public Context mContext;
    int type;

    public SubDoubleAdapter(ArrayList<Object> mData, Context CONTEXT, int type) {
        this.mData = mData;
        this.mContext = CONTEXT;
        this.type=type;
    }

    @NonNull
    @Override
    public SubDoubleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {

            case STRING:
            default:
                View menuItemLayoutView2 = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.double_list_item, parent, false);
                return new SubDoubleHolder(menuItemLayoutView2);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull SubDoubleHolder holder, final int position) {
        if (type == 1){

            Glide.with(mContext).load(mData.get(position).toString())
                    .placeholder(R.drawable.loading_img)
                    .into(holder.image);

        }if (type == 2){

            Glide.with(mContext).load(mData.get(position).toString())
                    .placeholder(R.drawable.loading_img)
                    .into(holder.image);

        }


        myClickAnim(mContext, holder.greetingtwo);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, GreetingEditActivity.class).putExtra("PATH",mData.get(position).toString()));
//                sGreeting.setBackgroundOfGreeting(mData.get(position).toString());
                if (type == 1){

                    doubleObj.sendBack(mData.get(position).toString());

                } else if (type == 2){

                    doubleTrendObj.sendBack(mData.get(position).toString());

                }


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

    public static class SubDoubleHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CardView greetingtwo;
        SubDoubleHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.greeting_img);
            greetingtwo = itemView.findViewById(R.id.greetingtwo);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        return drawableResourceId;
    }
}
