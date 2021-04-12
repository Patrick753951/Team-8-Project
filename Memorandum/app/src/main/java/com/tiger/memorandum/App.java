package com.tiger.memorandum;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * @name: App
 * @date: 2021-04-12 14:29
 * @comment:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
