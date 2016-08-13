package com.hindsitesapp.multipleimagepicker;

import java.io.Serializable;

/**
 * Created by amoghpalnitkar on 8/13/16.
 */

public class PickedPhoto implements Serializable {

        private String photoPath;
        private Double latitude;
        private Double longitude;
        private long clickedDateTime;

        public PickedPhoto(double latitude, double longitude, String picturePath, long milliSeconds) {
                this.photoPath = picturePath;
                this.latitude = latitude;
                this.longitude = longitude;
                this.clickedDateTime = milliSeconds;


        }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public long getClickedDateTime() {
        return clickedDateTime;
    }

    public void setClickedDateTime(long clickedDateTime) {
        this.clickedDateTime = clickedDateTime;
    }
}
