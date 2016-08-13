package com.hindsitesapp.moduletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hindsitesapp.multipleimagepicker.MultipleImagePicker;
import com.hindsitesapp.multipleimagepicker.Photo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button openGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openGallery = (Button) findViewById(R.id.openGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MultipleImagePicker imagePicker = new MultipleImagePicker();
                Intent intent = new Intent(getApplicationContext(), MultipleImagePicker.class);

                startActivityForResult(intent, 0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 ) {
            if (resultCode == RESULT_OK) {
                List<Photo> result = (List<Photo>) data.getExtras().getSerializable("photos");

                if (result != null) {
                    Log.d("TAG", " get activity result  " + result.size());
                    for(int i=0; i < result.size(); i++) {
                        Photo photo = result.get(i);
                        Log.d("TAG", " " + photo.getPhotoPath());

                    }
                }

            }
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
}
