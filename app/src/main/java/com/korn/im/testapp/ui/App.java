package com.korn.im.testapp.ui;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.korn.im.testapp.services.CheckTimeService;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by korn on 15.09.16.
 */
public class App extends Application {
    private static final long DEFAULT_DELAY = 120000;
    private static final int CHECK_TIME = 0;

    private final Handler timeCheckHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CHECK_TIME) {
                timeCheckHandler.sendEmptyMessageDelayed(CHECK_TIME, DEFAULT_DELAY);
                App.this.startService(new Intent(App.this, CheckTimeService.class));
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoader.getInstance().init(
                new ImageLoaderConfiguration.Builder(this)
                        .memoryCache(new LRULimitedMemoryCache(15 * 1024 * 1024))
                        .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                                .displayer(new FadeInBitmapDisplayer(300, true, false, false))
                                .resetViewBeforeLoading(true)
                                .cacheInMemory(true)
                                .showImageOnFail(android.R.mipmap.sym_def_app_icon)
                                .build())
                        .build());

        timeCheckHandler.sendEmptyMessageDelayed(CHECK_TIME, DEFAULT_DELAY);
    }
}
