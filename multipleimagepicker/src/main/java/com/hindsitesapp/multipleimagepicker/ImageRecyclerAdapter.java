package com.hindsitesapp.multipleimagepicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Authoritah on 29/11/2015.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ImageRecyclerViewHolder> {

    private List<Image> mImages = new ArrayList<>();
    private final int dimension;
    private Context mContext;
    private HashMap<Integer, GlideDrawableImageViewTarget> thumbnailHashMap = new HashMap<>();
    private SparseBooleanArray selectedImages;
    private final OnRecyclerItemClickListener mItemClickListener;
    private RecyclerView mRecyclerView;
    private InternalClickListener mInternalClickListener;


    public ImageRecyclerAdapter(Context context,OnRecyclerItemClickListener itemClickListener) {
        super();
        mContext = context;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float scale = displayMetrics.density;
        this.dimension = (displayMetrics.widthPixels - Math.round(8 * scale)) / 3;
        mItemClickListener = itemClickListener;
        mInternalClickListener = new InternalClickListener();
        selectedImages = new SparseBooleanArray();
    }

    /**
     */
    public void setData(List<Image> images) {
        //clearSelections();
        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    public void setSelectedImages(List<Image> selectedImageList) {
        for (Image image: selectedImageList) {
            selectedImages.put(image.position, true);
        }
    }

    @Override
    public ImageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.hindsites_custom_gallery_image_item, parent, false);
        itemView.setOnClickListener(mInternalClickListener);
        return new ImageRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageRecyclerViewHolder holder, int position) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimension, dimension);
        holder.image.setLayoutParams(layoutParams);
        if(selectedImages.get(position,false)) {
            holder.image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            holder.imageOverlay.setVisibility(View.VISIBLE);
        }else {
            holder.image.clearColorFilter();
            holder.imageOverlay.setVisibility(View.GONE);
        }
        GlideDrawableImageViewTarget glideDrawableImageViewTarget =
                new GlideDrawableImageViewTarget(holder.image);
        thumbnailHashMap.put(position, glideDrawableImageViewTarget);
    }

    @Override
    public void onViewAttachedToWindow(ImageRecyclerViewHolder holder) {
        final int position = holder.getAdapterPosition();
        if (thumbnailHashMap.containsKey(position)) {
            final Image image = mImages.get(position);
            image.position = position;
            mImages.set(position, image);
            GlideDrawableImageViewTarget glideDrawableImageViewTarget
                    = thumbnailHashMap.get(position);

            File photoFile = new File(image.path);
            if (photoFile.exists()) {
                Glide.with(mContext)
                        .load(photoFile)
                        .override(dimension, dimension)
                        .centerCrop()
                        .into(glideDrawableImageViewTarget);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_image_white_48dp)
                        .override(dimension, dimension)
                        .centerCrop()
                        .into(glideDrawableImageViewTarget);
            }
        }
    }

    @Override
    public void onViewRecycled(ImageRecyclerViewHolder holder) {
        final int position = holder.getAdapterPosition();
        if (thumbnailHashMap.containsKey(position)) {
            Glide.clear(thumbnailHashMap.get(position));
            thumbnailHashMap.remove(position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    protected class ImageRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView image, imageOverlay;

        public ImageRecyclerViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            imageOverlay = (ImageView) view.findViewById(R.id.imageOverlay);
        }
    }

    public void toggleSelection(int pos) {
        if (selectedImages.get(pos, false)) {
            selectedImages.delete(pos);
        } else {
            if(HindSitesCustomImageGalleryActivity.maxPhotos != -1 && getSelectedItemCount() >= HindSitesCustomImageGalleryActivity.maxPhotos){
                Toast.makeText(mContext, "Maximum " + HindSitesCustomImageGalleryActivity.maxPhotos +" photos can be selected", Toast.LENGTH_LONG).show();
            } else {
                selectedImages.put(pos, true);
            }
        }
    }

    public void clearSelections() {
        selectedImages.clear();
    }

    public int getSelectedItemCount() {
        return selectedImages.size();
    }

    public List<Image> getSelectedImages() {
        List<Image> imageArrayList =
                new ArrayList<Image>(selectedImages.size());
        for (int i = 0; i < selectedImages.size(); i++) {
            imageArrayList.add(mImages.get(selectedImages.keyAt(i)));
        }
        return imageArrayList;
    }

    private class InternalClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(mRecyclerView != null && mItemClickListener != null){
                // find the position of the item that was clicked
                int position = mRecyclerView.getChildAdapterPosition(v);
                // notify the main listener
                toggleSelection(position);
                ImageRecyclerViewHolder imageRecyclerViewHolder =
                        (ImageRecyclerViewHolder) mRecyclerView.getChildViewHolder(v);
                if(selectedImages.get(position,false)) {
                    imageRecyclerViewHolder.image.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    imageRecyclerViewHolder.imageOverlay.setVisibility(View.VISIBLE);
                }else {
                    imageRecyclerViewHolder.image.clearColorFilter();
                    imageRecyclerViewHolder.imageOverlay.setVisibility(View.GONE);
                }
                mItemClickListener.onItemClick(getSelectedItemCount());
            }
        }
    }
}
