package com.hindsitesapp.multipleimagepicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by amoghpalnitkar on 8/13/16.
 */
public class MultiImagePicker {

    private int maxPhotos;
    private boolean showToolbar = false;
    public static MultiImagePickerListener imagePickerListener;

    public MultiImagePicker(Builder builder) {
        this.maxPhotos = builder.maxPhotos;
        this.showToolbar = builder.showToolbar;

    }

    public static class Builder {

        private int maxPhotos;
        private boolean showToolbar;

        public Builder(int maxPhotos) {
            this.maxPhotos = maxPhotos;
        }

        public Builder showSeparateToolbar(boolean showToolbar) {
            this.showToolbar = showToolbar;
            return this;
        }

        public Builder setOnReceiveListener(MultiImagePickerListener listener) {
            imagePickerListener = listener;
            return this;
        }

        public MultiImagePicker build() {
            return new MultiImagePicker(this);
        }
    }

    public void startActivity(Context context) {
        Intent intent = new Intent(context, MultipleImagePickerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putInt("maxPhotos", maxPhotos);
        bundle.putBoolean("showToolbar", showToolbar);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
