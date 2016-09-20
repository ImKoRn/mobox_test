package com.korn.im.testapp.ui.activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.korn.im.testapp.adapters.PhotoClickListener;
import com.korn.im.testapp.broadcasts.ShowTimeBroadcast;
import com.korn.im.testapp.DataManager;
import com.korn.im.testapp.R;
import com.korn.im.testapp.pojo.Photo;
import com.korn.im.testapp.adapters.PhotosAdapter;
import com.korn.im.testapp.adapters.SwipeRemove;
import com.korn.im.testapp.services.CheckTimeService;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PhotoClickListener {
    private static final String LAST_LIST_POSITION = "last_list_position";

    private RecyclerView photosList;
    private PhotosAdapter photosAdapter;
    private Subscription onPhotosSubscription;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Parcelable lastListPosition;
    private LinearLayoutManager llm;
    private ShowTimeBroadcast receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photosList = (RecyclerView) findViewById(R.id.photosList);
        photosList.setAdapter(photosAdapter = new PhotosAdapter(this));
        photosList.setLayoutManager(llm = new LinearLayoutManager(this));
        new ItemTouchHelper(new SwipeRemove(photosAdapter))
                .attachToRecyclerView(photosList);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        photosAdapter.setListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPhotosSubscription = subscribeOnPhotosEvent();
        registerReceiver(receiver = new ShowTimeBroadcast(),
                new IntentFilter(CheckTimeService.SHOW_TIME_IN_DIALOG));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastListPosition = savedInstanceState.getParcelable(LAST_LIST_POSITION);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_LIST_POSITION, lastListPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (onPhotosSubscription != null) onPhotosSubscription.unsubscribe();
        lastListPosition = llm.onSaveInstanceState();

        if (receiver != null)
            unregisterReceiver(receiver);
    }


    private Subscription subscribeOnPhotosEvent() {
        return DataManager.getInstance().onPhotosEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Photo>>() {
                    @Override
                    public void call(List<Photo> photos) {
                        photosAdapter.setData(photos);
                        swipeRefreshLayout.setRefreshing(false);
                        if (lastListPosition != null) {
                            llm.onRestoreInstanceState(lastListPosition);
                            lastListPosition = null;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        onPhotosSubscription = subscribeOnPhotosEvent();
                    }
                });
    }

    @Override
    public void onRefresh() {
        DataManager.getInstance().fetchPhotos();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void photoClick(View view, Photo photo) {
        Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
        intent.putExtra(PhotoActivity.PHOTO, photo);
        startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this,
                        view.findViewById(R.id.photo),
                        getResources().getString(R.string.image))
                        .toBundle()
        );
    }
}
