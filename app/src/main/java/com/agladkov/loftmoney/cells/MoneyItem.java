package com.agladkov.loftmoney.cells;

import com.agladkov.loftmoney.remote.MoneyRemoteItem;

public class MoneyItem {

    private String title;
    private String value;

    public MoneyItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public static MoneyItem getInstance(MoneyRemoteItem moneyRemoteItem) {
        return new MoneyItem(moneyRemoteItem.getName(), moneyRemoteItem.getPrice() + "$");
    }
}
