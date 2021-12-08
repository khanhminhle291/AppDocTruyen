package com.example.mochi.activity.forget_password;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.R;
import com.example.mochi.activity.login.Login;
import com.example.mochi.databinding.ActivityForgetSuccessMessageBinding;

public class ForgetSuccessMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityForgetSuccessMessageBinding binding =
                ActivityForgetSuccessMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        binding.successMessageIcon.setAnimation(animation);
        binding.successMessageTitle.setAnimation(animation);
        binding.successMessageDescription.setAnimation(animation);
        binding.signin.setAnimation(animation);

        binding.signin.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), Login.class)));

    }
}
