package com.agladkov.loftmoney;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddMoneyActivity extends AppCompatActivity {

    private EditText moneyNameView;
    private EditText moneyPriceView;
    private Button moneyAddView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        moneyNameView = findViewById(R.id.moneyNameView);
        moneyAddView = findViewById(R.id.moneyAddView);
        moneyPriceView = findViewById(R.id.moneyPriceView);

        configureButton();
    }

    private void configureButton() {
        moneyAddView.setOnClickListener(v -> {
            if (moneyNameView.getText().equals("") || moneyPriceView.getText().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_LONG).show();
                return;
            }

            Disposable disposable = ((LoftApp) getApplication()).moneyApi.postMoney(
                    Integer.parseInt(moneyPriceView.getText().toString()),
                    moneyNameView.getText().toString(),
                    "income",
                    getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.AUTH_KEY, "")
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Toast.makeText(getApplicationContext(), getString(R.string.success_added), Toast.LENGTH_LONG).show();
                        finish();
                    }, throwable -> {
                        Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}
