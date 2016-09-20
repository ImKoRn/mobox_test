package com.korn.im.testapp.json;


import com.korn.im.testapp.pojo.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by korn on 15.09.16.
 */
public interface PhotosRequest {
    @GET(value = "photos_public.gne")
    Call<List<Photo>> fetchPhoto(@Query(value = "tags") String tags,
                          @Query(value = "format") String format, @Query(value = "nojsoncallback") String jsonCallback);
}
