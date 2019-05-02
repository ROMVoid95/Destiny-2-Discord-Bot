package com.prime.core;

public enum PriceList {

    PREMIUM(500000);

    private int price;

    PriceList(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
