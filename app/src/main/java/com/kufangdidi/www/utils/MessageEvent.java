package com.kufangdidi.www.utils;

public class MessageEvent {
    public int getN() {
        return n;
    }

    private int n = -1;

    public String getLocation() {
        return location;
    }

    private String location;

    public MessageEvent(int n) {
        this.n = n;
    }

    public MessageEvent(int n, String location) {
        this.n = n;
        this.location = location;
    }
}
