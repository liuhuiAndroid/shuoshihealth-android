package com.ssh.shuoshi.ui.imagegallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssh.shuoshi.R;

import java.util.List;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder> {

    private List<String> imageUrls;
    private OnClickListener mListener;

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_gallery, parent, false);
        return new ImageGalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGalleryViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(imageUrls.get(position)).into(holder.imageView2);

        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        });
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;

    }

    @Override
    public int getItemCount() {
        if (imageUrls == null) {
            return 0;
        } else {
            return imageUrls.size();
        }
    }

    class ImageGalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView2;

        public ImageGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView2 = itemView.findViewById(R.id.imageView);
        }
    }
}
