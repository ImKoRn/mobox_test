package com.korn.im.testapp.services;

import android.app.IntentService;
import android.content.Intent;

import com.korn.im.testapp.DataManager;

/**
 * Created by korn on 15.09.16.
 */
public class CheckTimeService extends IntentService {
    public static final String SHOW_TIME_IN_DIALOG = "com.korn.im.testapp.show_time_in_dialog";
    public static final String TIME = "time";
    public static final String IMAGE_URI = "image";

    public CheckTimeService() {
        super("CheckTimeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long currentTime = System.currentTimeMillis();

        Intent sendIntent = new Intent(SHOW_TIME_IN_DIALOG);

        sendIntent.putExtra(TIME, currentTime);
        sendIntent.putExtra(IMAGE_URI, DataManager.getInstance().randomImageUrl());

        sendBroadcast(sendIntent);
    }
}
