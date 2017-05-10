package com.niyo.hotstock.util;

public interface Observable {

    void addObserver();

    void notifyObserver(Object t);
}
