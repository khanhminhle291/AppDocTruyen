package com.example.mochi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.activity.login.Login;
import com.example.mochi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.mochi.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation top = AnimationUtils.loadAnimation(this, R.anim.top_amin);
        Animation button = AnimationUtils.loadAnimation(this, R.anim.button);

        binding.imageView.setAnimation(top);
        binding.logo.setAnimation(button);
        binding.slogan.setAnimation(button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, Login.class));
        }, 5000);
    }
}
