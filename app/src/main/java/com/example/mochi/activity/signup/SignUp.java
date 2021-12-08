package com.example.mochi.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.activity.login.Login;
import com.example.mochi.database.UsersFirebase;
import com.example.mochi.databinding.ActivitySignUpBinding;
import com.example.mochi.define.Define;
import com.example.mochi.model.User;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerBT.setOnClickListener(v -> {
            enableView(false);

            String fullName = binding.fullNameET.getText().toString();
            String username = binding.usernameET.getText().toString();
            String email = binding.emailET.getText().toString();
            String phoneNumber = binding.phoneNumberET.getText().toString();
            String password = binding.passwordET.getText().toString();

            boolean validFullName = validateFullName(fullName);
            boolean validEmail = validateEmail(email);
            boolean validUsername = validateUserName(username);
            boolean validPhoneNumber = validatePhoneNumber(phoneNumber);
            boolean validPassword = validatePassword(password);
            if (!validFullName || !validEmail || !validUsername || !validPhoneNumber || !validPassword) {
                enableView(true);
                return;
            }

            UsersFirebase.checkUser(Define.KEY_USERNAME, username, existUsername -> {
                if (existUsername) {
                    enableView(true);
                    binding.usernameET.setError("Tên tài khoản đã được đăng ký");
                    return;
                }
                binding.usernameET.setError(null);
                UsersFirebase.checkUser(Define.KEY_EMAIL, email, existEmail -> {
                    if (existEmail) {
                        enableView(true);
                        binding.emailET.setError("Email đã được đăng ký");
                        return;
                    }
                    binding.emailET.setError(null);
                    UsersFirebase.checkUser(Define.KEY_PHONE_NUMBER, phoneNumber, existPhoneNumber -> {
                        if (existPhoneNumber) {
                            enableView(true);
                            binding.phoneNumberET.setError("Số điện thoại đã được đăng ký");
                            return;
                        }
                        enableView(true);
                        binding.phoneNumberET.setError(null);
                        UsersFirebase.addUser(new User(fullName, username, email, phoneNumber, password, new ArrayList<>()));
                        createUserSuccess();
                    });
                });
            });
        });
    }

    private void createUserSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\n" + "Tạo tài khoản thành công")
                .setCancelable(false)
                .setTitle("Thông báo")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    startActivity(new Intent(this, Login.class));
                    finish();

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Boolean validateFullName(String fullName) {
        if (fullName.isEmpty()) {
            binding.fullNameET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        binding.fullNameET.setError(null);
        return true;
    }

    private Boolean validateUserName(String username) {
        if (username.isEmpty()) {
            binding.usernameET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        String noWhiteSpace = "^[A-Za-z]\\w{5,19}$";
        if (!username.matches(noWhiteSpace)) {
            binding.usernameET.setError("Tên tài khoản không hợp lệ");
            return false;
        }
        binding.usernameET.setError(null);
        return true;
    }

    private Boolean validateEmail(String email) {
        if (email.isEmpty()) {
            binding.emailET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        String reg = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(reg)) {
            binding.emailET.setError("Địa chỉ email không hợp lệ");
            return false;
        }
        binding.emailET.setError(null);
        return true;
    }

    private Boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            binding.phoneNumberET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        String noWhiteSpace = "\\A\\w{10,11}\\z";
        if (!phoneNumber.matches(noWhiteSpace)) {
            binding.phoneNumberET.setError("Số điện thoai không hợp lệ");
            return false;
        }
        binding.phoneNumberET.setError(null);
        return true;
    }

    private Boolean validatePassword(String password) {
        if (password.isEmpty()) {
            binding.passwordET.setError("Phải điền đầy đủ thông tin");
            return false;
        }
        String noWhiteSpace = "\\A\\w{6,8}\\z";
        if (!password.matches(noWhiteSpace)) {
            binding.passwordET.setError("Mật khẩu không hợp lệ");
            return false;
        }
        binding.passwordET.setError(null);
        return true;
    }

    private void enableView(boolean enable) {
        binding.registerBT.setEnabled(enable);
        if (enable) {
            binding.progressBar.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
    }

}
