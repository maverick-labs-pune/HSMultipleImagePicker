package com.hindsitesapp.moduletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hindsitesapp.multipleimagepicker.MultiImagePicker;
import com.hindsitesapp.multipleimagepicker.MultiImagePickerListener;
import com.hindsitesapp.multipleimagepicker.OnRecyclerItemClickListener;
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

        int maxPhotos = 2;
        multiImagePicker = new MultiImagePicker.Builder(maxPhotos)
                .setOnReceiveListener(new MultiImagePickerListener() {
                    @Override
                    public void onImagesPicked(List<PickedPhoto> pickedPhotos) {
                        for(int i=0; i < pickedPhotos.size(); i++) {
                            PickedPhoto photo = pickedPhotos.get(i);
                            Log.d("TAG", " In main activity " + photo.getPhotoPath());

                        }
                    }
                })
                .build();
        multiImagePicker.startActivity(getApplicationContext());
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
}
