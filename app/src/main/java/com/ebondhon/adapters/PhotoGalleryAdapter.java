package com.ebondhon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ebondhon.R;
import com.ebondhon.models.AppData;
import com.ebondhon.utils.FontUtils;

import java.util.List;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder> {

    private final Context context;
    private final List<AppData.Photo> photos;

    public PhotoGalleryAdapter(Context context, List<AppData.Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppData.Photo photo = photos.get(position);

        holder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(photo.url)
                .placeholder(R.drawable.loading)
                .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                            Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model,
                            Target<android.graphics.drawable.Drawable> target, DataSource dataSource,
                            boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.photoImage);

        if (photo.caption != null && !photo.caption.isEmpty()) {
            holder.captionText.setVisibility(View.VISIBLE);
            holder.captionText.setText(photo.caption);
            FontUtils.applyFont(context, holder.captionText);
        } else {
            holder.captionText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImage;
        ProgressBar progressBar;
        TextView captionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.galleryPhoto);
            progressBar = itemView.findViewById(R.id.galleryProgress);
            captionText = itemView.findViewById(R.id.galleryCaption);
        }
    }
}
