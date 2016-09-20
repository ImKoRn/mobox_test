package com.korn.im.testapp.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.korn.im.testapp.R;
import com.korn.im.testapp.services.CheckTimeService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by korn on 15.09.16.
 */
public class ShowTimeBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ImageView view = (ImageView) LayoutInflater.from(context).inflate(R.layout.dialog_image, null);

        ImageLoader.getInstance().displayImage(
                intent.getStringExtra(CheckTimeService.IMAGE_URI),
                view
        );

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(intent.getLongExtra(CheckTimeService.TIME, 0));

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(
                        R.string.current_time, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE)))
                .setView(view)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create();
        alertDialog.show();
    }
}
