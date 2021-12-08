package com.example.mochi.activity.forget_password;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.R;
import com.example.mochi.activity.login.Login;
import com.example.mochi.database.UsersFirebase;
import com.example.mochi.databinding.ActivityForgetPasswordBinding;
import com.example.mochi.define.Define;
import com.example.mochi.utils.CheckInternet;

import java.util.Objects;

public class ForgetPassword extends AppCompatActivity {

    private ActivityForgetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        binding.screenIcon.setAnimation(animation);
        binding.title.setAnimation(animation);
        binding.description.setAnimation(animation);
        binding.phoneNumber.setAnimation(animation);
        binding.countryCodePicker.setAnimation(animation);
        binding.next.setAnimation(animation);
        binding.progressBar.setVisibility(View.GONE);

        binding.next.setOnClickListener(v -> verifyPhoneNumber());

        binding.back.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class))
        );
    }

    public void verifyPhoneNumber() {
        binding.next.setEnabled(false);
        if (!CheckInternet.isConnected(this)) {
            binding.next.setEnabled(true);
            showCustomDialog();
            return;
        }

        String phoneNumber = Objects.requireNonNull(binding.phoneNumber.getEditText()).getText().toString();

        if (!validatePhoneNumber(phoneNumber)) {
            binding.next.setEnabled(true);
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);

        if (phoneNumber.charAt(0) != '0') {
            phoneNumber = "0" + phoneNumber;
        }

        String finalPhoneNumber = binding.countryCodePicker.getSelectedCountryCodeWithPlus() + phoneNumber.substring(1);
        UsersFirebase.checkUser(Define.KEY_PHONE_NUMBER, phoneNumber, exist -> {
            binding.next.setEnabled(true);
            binding.progressBar.setVisibility(View.GONE);
            if (!exist) {
                binding.phoneNumber.setError("Số điện thoại này chưa đăng ký tài khoản");
                binding.phoneNumber.requestFocus();
            } else {
                Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                intent.putExtra(Define.KEY_PHONE_NUMBER, finalPhoneNumber);
                startActivity(intent);
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
        builder.setMessage("\n" + "Vui lòng kết nối internet để tiếp tục")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            binding.phoneNumber.setError("Số điện thoại không được để trống");
            binding.phoneNumber.requestFocus();
            return false;
        }

        String checkSpaces = "\\A\\w{9,11}\\z";
        if (!phoneNumber.matches(checkSpaces)) {
            binding.phoneNumber.setError("Không cho phép khoảng trắng!");
            return false;
        }

        binding.phoneNumber.setError(null);
        return true;
    }

}
