package com.example.mochi.activity.forget_password;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mochi.database.UsersFirebase;
import com.example.mochi.databinding.ActivityVerifyOtpBinding;
import com.example.mochi.define.Define;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    String phoneNumber;
    String verificationId = null;
    String smsCode;
    private ActivityVerifyOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phoneNumber = getIntent().getStringExtra(Define.KEY_PHONE_NUMBER);
        binding.otpDescriptionText.setText("Nhập mã xác thực được gửi đến cho số điện thoại\n" + phoneNumber);

        sendOTP(phoneNumber);

        binding.confirm.setEnabled(false);

        binding.confirm.setOnClickListener(v -> {
            if (verificationId != null) {
                smsCode = Objects.requireNonNull(binding.smsCode.getText()).toString();
                if (smsCode.length() != 6) {
                    Toast.makeText(getApplicationContext(), "Mã xác thực không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    binding.confirm.setEnabled(false);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, smsCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        binding.progressBar.setVisibility(View.GONE);
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        binding.confirm.setEnabled(true);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, (task -> {
                    if (task.isSuccessful()) {
                        String mPhoneNumber = "0" + phoneNumber.substring(3);
                        UsersFirebase.getUser(Define.KEY_PHONE_NUMBER, mPhoneNumber, user -> {
                                    Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
                                    intent.putExtra(Define.KEY_FULL_NAME, user.getFullName());
                                    intent.putExtra(Define.KEY_USERNAME, user.getUsername());
                                    intent.putExtra(Define.KEY_EMAIL, user.getEmail());
                                    intent.putExtra(Define.KEY_PHONE_NUMBER, user.getPhoneNumber());
                                    intent.putExtra(Define.KEY_PASSWORD, user.getPassword());
                                    startActivity(intent);
                                }
                        );
                    } else {
                        binding.confirm.setEnabled(true);
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Chưa hoàn thành xác minh! Thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
