package com.hindsitesapp.multipleimagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amoghpalnitkar on 8/13/16.
 */
public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.FolderRecyclerViewHolder>{
    private final int dimension;
    private Context mContext;
    private Activity activity;
    private int maxPhotos;
    private boolean showToolbar;

    private List<Folder> mFolders = new ArrayList<>();
    private HashMap<Integer, GlideDrawableImageViewTarget> thumbnailHashMap = new HashMap<>();


    public FolderRecyclerAdapter(Context context, Activity activity, int maxPhotos, boolean showToolbar) {
        super();
        mContext = context;
        this.activity = activity;
        this.maxPhotos = maxPhotos;
        this.showToolbar = showToolbar;

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float scale = displayMetrics.density;
        this.dimension = (displayMetrics.widthPixels - Math.round(8 * scale)) / 2;
    }

    /**
     * @param folders
     */
    public void setData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) {
            mFolders = folders;
        } else {
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public FolderRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.hindsites_custom_gallery_folder_item, parent, false);
        return new FolderRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FolderRecyclerViewHolder holder, int position) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dimension, dimension);
        holder.cover.setLayoutParams(layoutParams);
        GlideDrawableImageViewTarget glideDrawableImageViewTarget =
                new GlideDrawableImageViewTarget(holder.cover);
        thumbnailHashMap.put(position, glideDrawableImageViewTarget);
    }

    @Override
    public void onViewAttachedToWindow(final FolderRecyclerViewHolder holder) {
        final int position = holder.getAdapterPosition();
        if (thumbnailHashMap.containsKey(position)) {
            final Folder folder = mFolders.get(position);
            GlideDrawableImageViewTarget glideDrawableImageViewTarget
                    = thumbnailHashMap.get(position);
            Glide.with(mContext)
                    .load(new File(mFolders.get(position).cover.path))
                    .override(dimension, dimension)
                    .centerCrop()
                    .into(glideDrawableImageViewTarget);

            holder.name.setText(folder.name);
            holder.size.setText("" + folder.images.size());
            holder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, HindSitesCustomImageGalleryActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", folder.path);
                    bundle.putString("folderName", folder.name);
                    bundle.putInt("maxPhotos", maxPhotos);
                    bundle.putBoolean("showToolbar", showToolbar);
                    intent.putExtra("pathBundle", bundle);

                    activity.startActivityForResult(intent, 0);
                    //mContext.startActivity(intent);
                }

            });
        }
    }

    @Override
    public void onViewRecycled(FolderRecyclerViewHolder holder) {
        final int position = holder.getAdapterPosition();
        if (thumbnailHashMap.containsKey(position)) {
            Glide.clear(thumbnailHashMap.get(position));
            thumbnailHashMap.remove(position);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mFolders.size();
    }

    protected class FolderRecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;
        TextView size;

        public FolderRecyclerViewHolder(View view) {
            super(view);
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.number_of_photos);
        }
    }
}
