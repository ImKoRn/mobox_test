package com.korn.im.testapp.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.korn.im.testapp.R;
import com.korn.im.testapp.databinding.HolderPhotoBinding;
import com.korn.im.testapp.pojo.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by korn on 15.09.16.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoHolder> implements SwipeRemove.OnItemSwipedListener {
    private final Context context;
    private SortedList<Photo> data;
    private PhotoClickListener listener;
    private List<Photo> dataSource;

    public PhotosAdapter(Context context) {
        this.context = context;

        data = new SortedList<>(Photo.class,
                new SortedList.BatchedCallback<>(
                new SortedListAdapterCallback<Photo>(this) {
                    @Override
                    public int compare(Photo o1, Photo o2) {
                        return o1.imageDescription.compareToIgnoreCase(o2.imageDescription);
                    }

                    @Override
                    public boolean areContentsTheSame(Photo oldItem, Photo newItem) {
                        return oldItem.equals(newItem);
                    }

                    @Override
                    public boolean areItemsTheSame(Photo item1, Photo item2) {
                        return item1.equals(item2);
                    }
                }
        ));
    }

    @Override
    public PhotosAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoHolder(LayoutInflater
                .from(context)
                .inflate(
                        R.layout.holder_photo,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(PhotosAdapter.PhotoHolder holder, int position) {
        holder.binding.setPhoto(data.get(position));
        holder.binding.setHandler(listener);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }

    public void setData(List<Photo> data) {
        dataSource = data;

        this.data.beginBatchedUpdates();
        this.data.addAll(data);
        this.data.endBatchedUpdates();
    }

    @Override
    public void onSwiped(int position) {
        data.beginBatchedUpdates();
        dataSource.remove(data.removeItemAt(position));
        data.endBatchedUpdates();
    }

    public void setListener(PhotoClickListener listener) {
        this.listener = listener;
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        public final HolderPhotoBinding binding;

        public PhotoHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }
}
