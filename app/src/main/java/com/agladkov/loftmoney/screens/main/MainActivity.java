package com.agladkov.loftmoney.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agladkov.loftmoney.AddMoneyActivity;
import com.agladkov.loftmoney.LoftApp;
import com.agladkov.loftmoney.R;
import com.agladkov.loftmoney.cells.MoneyCellAdapter;
import com.agladkov.loftmoney.cells.MoneyItem;
import com.agladkov.loftmoney.remote.MoneyRemoteItem;
import com.agladkov.loftmoney.remote.MoneyResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MoneyCellAdapter moneyCellAdapter = new MoneyCellAdapter();
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViews();
        configureViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mainViewModel.loadIncomes(
                ((LoftApp) getApplication()).moneyApi,
                getSharedPreferences(getString(R.string.app_name), 0)
        );
    }

    private void configureViews() {
        RecyclerView recyclerView = findViewById(R.id.itemsView);
        recyclerView.setAdapter(moneyCellAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                false));

        FloatingActionButton addNewIncomeView = findViewById(R.id.addNewExpense);
        addNewIncomeView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddMoneyActivity.class);
            startActivity(intent);
        });
    }

    private void configureViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.moneyItemsList.observe(this, moneyItems -> {
            moneyCellAdapter.setData(moneyItems);
        });

        mainViewModel.messageString.observe(this, message -> {
            if (!message.equals("")) {
                showToast(message);
            }
        });

        mainViewModel.messageInt.observe(this, message -> {
            if (message > 0) {
                showToast(getString(message));
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
