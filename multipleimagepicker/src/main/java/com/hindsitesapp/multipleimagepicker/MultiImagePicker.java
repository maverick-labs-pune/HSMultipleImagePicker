package com.hindsitesapp.multipleimagepicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by amoghpalnitkar on 8/13/16.
 */
public class MultiImagePicker {

    private int maxPhotos;
    public static MultiImagePickerListener imagePickerListener;

    public MultiImagePicker(Builder builder) {
        this.maxPhotos = builder.maxPhotos;

    }

    public static class Builder {

        private int maxPhotos;

        public Builder(int maxPhotos) {
            this.maxPhotos = maxPhotos;
        }

        public Builder setOnReceiveListener(MultiImagePickerListener listener) {
            //this.listener = listener;
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
        //bundle.putSerializable("listener", listener);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
