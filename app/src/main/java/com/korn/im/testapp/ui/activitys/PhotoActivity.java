package com.korn.im.testapp.ui.activitys;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.korn.im.testapp.R;
import com.korn.im.testapp.broadcasts.ShowTimeBroadcast;
import com.korn.im.testapp.databinding.ActivityPhotoBinding;
import com.korn.im.testapp.pojo.Photo;
import com.korn.im.testapp.services.CheckTimeService;

/**
 * Created by korn on 15.09.16.
 */
public class PhotoActivity extends AppCompatActivity {
    public static final String PHOTO = "photo";
    private ActivityPhotoBinding activityPhotoBinding;
    private Photo photo;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPhotoBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_photo);

        photo = getIntent().getParcelableExtra(PHOTO);
        if (photo == null) finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityPhotoBinding.setPhoto(photo);
        registerReceiver(receiver = new ShowTimeBroadcast(),
                new IntentFilter(CheckTimeService.SHOW_TIME_IN_DIALOG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null)
            unregisterReceiver(receiver);
    }
}
