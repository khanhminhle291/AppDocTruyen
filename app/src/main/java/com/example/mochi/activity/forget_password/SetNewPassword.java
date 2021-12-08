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
import com.example.mochi.databinding.ActivitySetNewPasswordBinding;
import com.example.mochi.define.Define;
import com.example.mochi.utils.CheckInternet;

import java.util.Objects;

public class SetNewPassword extends AppCompatActivity {

    private ActivitySetNewPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        binding.setNewPasswordIcon.setAnimation(animation);
        binding.setNewPasswordTitle.setAnimation(animation);
        binding.setNewPasswordDescription.setAnimation(animation);
        binding.newPassword.setAnimation(animation);
        binding.confirmPassword.setAnimation(animation);
        binding.updatePassword.setAnimation(animation);

        binding.updatePassword.setOnClickListener(v -> updatePassword());

        binding.back.setOnClickListener(v -> {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
        );
    }

    private void updatePassword() {
        if (!CheckInternet.isConnected(this)) {
            showCustomDialog();
            return;
        }

        String newPassword = Objects.requireNonNull(binding.newPassword.getEditText()).getText().toString();
        String confirmPassword = Objects.requireNonNull(binding.confirmPassword.getEditText()).getText().toString();
        if (!validatePassword(newPassword) | !validateConfirmPassword(newPassword, confirmPassword)) {
            return;
        }
        binding.setNewPasswordProgressBar.setVisibility(View.VISIBLE);

        String username = getIntent().getStringExtra(Define.KEY_USERNAME);
        UsersFirebase.updateUser(username, Define.KEY_PASSWORD, newPassword);

        startActivity(new Intent(getApplicationContext(), ForgetSuccessMessage.class));
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vui lòng kết nối internet để tiếp tục")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) ->
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.newPassword.setError("Không được để trống");
            return false;
        }

        String noWhiteSpace = "\\A\\w{6,8}\\z";
        if (!password.matches(noWhiteSpace)) {
            binding.newPassword.setError("Không có khoảng trắng!");
            return false;
        }

        binding.newPassword.setError(null);
        binding.newPassword.setErrorEnabled(false);
        return true;
    }

    private Boolean validateConfirmPassword(String newPassword, String confirmPassword) {
        if (confirmPassword.isEmpty()) {
            binding.confirmPassword.setError("Trường không được để trống");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.confirmPassword.setError("Mật khẩu không hợp lệ! Vui lòng thử lại.");
            return false;
        }

        binding.confirmPassword.setError(null);
        binding.confirmPassword.setErrorEnabled(false);
        return true;
    }
}
