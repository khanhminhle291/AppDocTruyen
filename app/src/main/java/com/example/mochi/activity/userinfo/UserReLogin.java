package com.example.mochi.activity.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.database.UserSingleton;
import com.example.mochi.database.UsersFirebase;
import com.example.mochi.databinding.ActivityUserReLoginBinding;
import com.example.mochi.define.Define;

public class UserReLogin extends AppCompatActivity {

    private ActivityUserReLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserReLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBT.setOnClickListener(v -> {
            viewEnable(false);
            String username = binding.usernameET.getText().toString();
            String password = binding.passwordET.getText().toString();
            boolean validUsername = validateUserName(username);
            boolean validPassword = validatePassword(password);
            if (validUsername && validPassword) {
                isUser(username, password);
            } else {
                viewEnable(true);
            }
        });
    }


    private Boolean validateUserName(String username) {
        if (username.isEmpty()) {
            binding.usernameET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        binding.usernameET.setError(null);
        return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.passwordET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        binding.passwordET.setError(null);
        return true;
    }


    private void isUser(String username, String password) {
        UsersFirebase.getUser(Define.KEY_USERNAME, username, user -> {
            if (user == null) {
                viewEnable(true);
                binding.usernameET.setError("Không tìm thấy tài khoản này");
                binding.usernameET.requestFocus();
                return;
            }
            binding.usernameET.setError(null);
            if (!user.getPassword().equals(password)) {
                viewEnable(true);
                binding.passwordET.setError("Sai mật khẩu");
                binding.passwordET.requestFocus();
                return;
            }

            viewEnable(true);
            binding.usernameET.setError(null);
            binding.passwordET.setText(null);

            UserSingleton.getInstance().setUser(user);
            startActivity(new Intent(this, UserInfo.class));
        });
    }

    private void viewEnable(boolean enable) {
        binding.loginBT.setEnabled(enable);
        if (enable) {
            binding.progressBar.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }
}
