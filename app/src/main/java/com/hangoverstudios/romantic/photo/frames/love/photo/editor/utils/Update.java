package com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

public class Update extends AppCompatActivity {
    private TextView update_text;
    private Button update_button;
    private ImageView up_hand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        update_text = findViewById(R.id.update_text);
        update_button = findViewById(R.id.update_button);
        up_hand = findViewById(R.id.up_hand);
        fadeIn();
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void fadeIn() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(up_hand, "alpha", 0f, 1f);
        objectAnimator.setDuration(500L);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeOut();
            }
        });
        objectAnimator.start();
    }

    private void fadeOut() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(up_hand, "alpha", 1f, 0f);
        objectAnimator.setDuration(500L);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeIn();
            }
        });
        objectAnimator.start();
    }
}
