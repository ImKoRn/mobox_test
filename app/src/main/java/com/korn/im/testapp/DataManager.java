package com.korn.im.testapp;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.korn.im.testapp.json.PhotosDeserializer;
import com.korn.im.testapp.json.PhotosRequest;
import com.korn.im.testapp.pojo.Photo;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by korn on 15.09.16.
 */
public class DataManager {
    private static DataManager instance;

    private static final String FORMAT = "json";

    private final String BASE_URL = "https://api.flickr.com/services/feeds/";

    private BehaviorSubject<List<Photo>> photosEvent = BehaviorSubject.create();

    private List<Photo> photos;
    private Random random;

    private final PhotosRequest photoRequest = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .registerTypeAdapter(new TypeToken<List<Photo>>() {}.getType(), new PhotosDeserializer())
                    .create()))
            .build()
            .create(PhotosRequest.class);

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();

        return instance;
    }

    public Observable<List<Photo>> onPhotosEvent() {
        if (photosEvent == null)
            photosEvent = BehaviorSubject.create(photos);

        return photosEvent.asObservable();
    }

    public void fetchPhotos() {
        photoRequest
                .fetchPhoto("art", FORMAT, "\"\"")
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        photos = response.body();
                        photosEvent.onNext(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {
                        photosEvent.onError(t);
                        photosEvent = null;
                    }
                });
    }

    public String randomImageUrl() {
        if (photos == null || photos.size() == 0)
            return "https://www.google.com.ua/logos/doodles/2016/jean-battens-107th-birthday-5170085972934656-hp.jpg";

        if (random == null)
            random = new Random();

        return photos.get(random.nextInt(photos.size())).imageSource;
    }
}
