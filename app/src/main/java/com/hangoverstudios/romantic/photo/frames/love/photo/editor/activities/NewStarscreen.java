package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

public class NewStarscreen extends AppCompatActivity {
    TextView startScreenbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_starscreen);
        startScreenbtn=findViewById(R.id.start_screen);
        startScreenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewStarscreen.this, HomeActivity.class));
                finish();
            }
        });
    }
}