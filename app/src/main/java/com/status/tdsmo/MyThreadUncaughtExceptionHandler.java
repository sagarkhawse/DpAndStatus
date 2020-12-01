package com.status.tdsmo;

import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MyThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "Received exception '" + ex.getMessage() + "' from thread " + thread.getName(), ex);
    }
}