package com.hindsitesapp.moduletest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hindsitesapp.multipleimagepicker.MultiImagePicker;
import com.hindsitesapp.multipleimagepicker.MultiImagePickerListener;
import com.hindsitesapp.multipleimagepicker.PickedPhoto;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button openGallery;
    MultiImagePicker multiImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openGallery = (Button) findViewById(R.id.openGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhotos();

            }
        });


    }

    private void selectPhotos() {

        //For supporting api > 23, need to check permission to read external storage.
        if(checkPermission() == PackageManager.PERMISSION_GRANTED) {
            int maxPhotos = 10;

            multiImagePicker = new MultiImagePicker.Builder()

                    //Maximum number of photos that user is allowed to select. By default, there is no limit
                    .setMaxPhotos(maxPhotos)

                    // If your theme is NoActionBar - as in this sample app or you are hiding the bar in your activity,
                    // set this option to true in order to show a toolbar at the top.
                    // Toolbar will follow your app theme.
                    // Default is false.
                    .showSeparateToolbar(true)

                    //Receiver to get all the selected photos
                    .setOnReceiveListener(new MultiImagePickerListener() {
                        @Override
                        public void onImagesPicked(List<PickedPhoto> pickedPhotos) {

                            //Get all the picked photos here . Type of photo is PickedPhoto
                            for (PickedPhoto photo : pickedPhotos) {
                                Log.d("TAG", " In main activity " + photo.getPhotoPath());

                                //You can also get these values from the picked photos.

                                //photo.getLatitude();
                                //photo.getLongitude();
                                //photo.getClickedDateTime();
                            }
                        }
                    })
                    .build();
            multiImagePicker.startActivity(getApplicationContext());

        } else {
            requestPermission();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectPhotos();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private int checkPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }


}
