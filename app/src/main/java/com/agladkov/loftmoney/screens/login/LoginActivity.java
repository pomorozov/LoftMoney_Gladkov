package com.agladkov.loftmoney.screens.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.agladkov.loftmoney.LoftApp;
import com.agladkov.loftmoney.R;
import com.agladkov.loftmoney.screens.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configureViews();
        configureViewModel();
    }

    private void configureViews() {
        AppCompatButton loginEnterView = findViewById(R.id.loginEnterView);

        loginEnterView.setOnClickListener(v -> {
            loginViewModel.makeLogin(((LoftApp) getApplication()).authApi);
        });
    }

    private void configureViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.messageString.observe(this, error -> {
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        loginViewModel.authToken.observe(this, authToken -> {
            if (!TextUtils.isEmpty(authToken)) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);
                sharedPreferences.edit().putString(LoftApp.AUTH_KEY, authToken).apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
