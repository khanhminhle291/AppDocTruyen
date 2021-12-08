package com.example.mochi.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.activity.HomePage;
import com.example.mochi.activity.forget_password.ForgetPassword;
import com.example.mochi.activity.signup.SignUp;
import com.example.mochi.database.UserSingleton;
import com.example.mochi.database.UsersFirebase;
import com.example.mochi.databinding.ActivityLoginBinding;
import com.example.mochi.define.Define;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBT.setOnClickListener(v -> {
            enableView(false);

            String username = binding.usernameET.getText().toString();
            String password = binding.passwordET.getText().toString();

            boolean validateUserName = validateUserName(username);
            boolean validPassword = validateUserPass(password);
            if (validateUserName && validPassword) {
                isUser(username, password);
            } else {
                enableView(true);
            }
        });

        binding.signupBT.setOnClickListener(v ->
                startActivity(new Intent(this, SignUp.class)));

        binding.forgetPasswordBT.setOnClickListener(v ->
                startActivity(new Intent(this, ForgetPassword.class)));
    }


    private void isUser(String username, String password) {
        UsersFirebase.getUser(Define.KEY_USERNAME, username, user -> {
            if (user == null) {
                binding.usernameET.setError("Không tìm thấy tài khoản này");
                binding.usernameET.requestFocus();
                enableView(true);
                return;
            }

            binding.usernameET.setError(null);
            if (!user.getPassword().equals(password)) {
                binding.passwordET.setError("Sai mật khẩu");
                binding.passwordET.requestFocus();
                enableView(true);
                return;
            }

            enableView(true);

            binding.usernameET.setError(null);
            binding.passwordET.setText(null);

            UserSingleton.getInstance().setUser(user);

            Log.d("DEBUG", "user = " + user);

            startActivity(new Intent(this, HomePage.class));
        });
    }

    private void enableView(boolean enable) {
        binding.loginBT.setEnabled(enable);
        binding.forgetPasswordBT.setEnabled(enable);
        binding.loginBT.setEnabled(enable);
        binding.signupBT.setEnabled(enable);
        if (enable) {
            binding.progressBar.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }

    private Boolean validateUserName(String username) {
        if (username.isEmpty()) {
            binding.usernameET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        binding.usernameET.setError(null);
        return true;
    }

    private Boolean validateUserPass(String password) {
        if (password.isEmpty()) {
            binding.passwordET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        binding.forgetPasswordBT.setError(null);
        return true;
    }
}
