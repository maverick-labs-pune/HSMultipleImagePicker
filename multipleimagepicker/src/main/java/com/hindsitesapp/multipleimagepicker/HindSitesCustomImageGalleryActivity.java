/*
 *
 *  *
 *  *   MAVERICK LABS CONFIDENTIAL
 *  *   __________________
 *  *
 *  *    [2015] Maverick Labs
 *  *    All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  *
 *
 *
 */

package com.hindsitesapp.multipleimagepicker;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Authoritah on 29/11/15.
 */
public class HindSitesCustomImageGalleryActivity extends AppCompatActivity {

    private static final int GRID_SPAN = 3;

    private List<PickedPhoto> selectedPhotoList;
    private ImageRecyclerAdapter imageRecyclerAdapter;
    private String folderPath;
    private String path;
    private String folderName;
    public static int maxPhotos;
    private boolean showToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Image> selectedImageList;
        if (savedInstanceState != null) {
            path = savedInstanceState.getString("path");
            folderName = savedInstanceState.getString("folderName");
            folderPath = savedInstanceState.getString("folderPath");
            showToolbar = savedInstanceState.getBoolean("showToolbar");
            selectedImageList = (List<Image>) savedInstanceState.getSerializable("selectedImageList");

            Log.d("TAG" , " selected images" + selectedImageList.size());
            maxPhotos = savedInstanceState.getInt("maxPhotos");
        } else {
            Bundle bundle = this.getIntent().getBundleExtra("pathBundle");
            path = bundle.getString("path");
            showToolbar = bundle.getBoolean("showToolbar");
            folderName = bundle.getString("folderName");
            selectedImageList = new ArrayList<>();
            maxPhotos = bundle.getInt("maxPhotos");
        }
        setContentView(R.layout.activity_hindsites_custom_gallery);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if(showToolbar) {

            toolbar.setTitle("Select Photos");
            if (getSupportActionBar() == null) {
                setSupportActionBar(toolbar);
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        } else {
            toolbar.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Select Photos");
            }
        }



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.image_grid);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                GRID_SPAN,
                GridLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(gridLayoutManager);
        imageRecyclerAdapter = new ImageRecyclerAdapter(getApplicationContext(), new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(int selectedCount) {
                setToolbarTitle(selectedCount);
            }
        });
        recyclerView.setAdapter(imageRecyclerAdapter);
        if(selectedImageList.size() > 0) {
            imageRecyclerAdapter.setSelectedImages(selectedImageList);
            setToolbarTitle(selectedImageList.size());
        }

        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        this.getSupportLoaderManager().initLoader(0, bundle, mLoaderCallback);
        //hindSitesLogger.logInformation("sent args :" + path);

    }

    private void setToolbarTitle(int selectedCount) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            switch (selectedCount) {
                case 0:
                    supportActionBar.setTitle("No photos selected");
                    break;
                case 1:
                    supportActionBar.setTitle("1 photo selected");
                    break;
                default:
                    supportActionBar.setTitle(selectedCount + " photos selected");
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("path", path);
        outState.putString("folderPath", folderPath);
        outState.putString("folderName", folderName);
        outState.putInt("maxPhotos", maxPhotos);
        outState.putBoolean("showToolbar", showToolbar);

        outState.putSerializable("selectedImageList", (Serializable) imageRecyclerAdapter.getSelectedImages());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.action_next) {
            selectedPhotoList = new ArrayList<>();
            List<Image> imageList = imageRecyclerAdapter.getSelectedImages();
            if (imageList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No photos selected", Toast.LENGTH_LONG).show();
                return true;
            }
            for (Image image : imageList) {
                addToList(image.path);
            }

            // Create intent to deliver some kind of result data
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("photos", (Serializable) selectedPhotoList);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToList(String picturePath) {
        if (maxPhotos != -1 && selectedPhotoList.size() >= maxPhotos) {
            return;
        }

        double latitude = 0, longitude = 0;
        String dateTime;
        long milliSeconds = 0;
        try {
            ExifInterface photoMetadata = new ExifInterface(picturePath);
            float[] locationArray = new float[2];
            photoMetadata.getLatLong(locationArray);
            latitude = locationArray[0];
            longitude = locationArray[1];
            dateTime = photoMetadata.getAttribute(ExifInterface.TAG_DATETIME);
            if (!isEmpty(dateTime)) {
                milliSeconds = convertToMilliseconds(dateTime);
            } else {
                milliSeconds = new File(picturePath).lastModified();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedPhotoList.add(new PickedPhoto(latitude, longitude
                , picturePath, milliSeconds));


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hindsites_custom_gallery, menu);
        return true;
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            folderPath = args.getString("path");
            //hindSitesLogger.logInformation("received args :" + folderPath);
            //hindSitesLogger.logDebug("selection :"+
            //        IMAGE_PROJECTION[0] + " like " + DatabaseUtils.sqlEscapeString("%" + args.getString("path") +"%"));
            return new CursorLoader(getApplicationContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_PROJECTION,
                    IMAGE_PROJECTION[0] + " like " + DatabaseUtils.sqlEscapeString("%" + args.getString("path") +"%"),
                    null,
                    IMAGE_PROJECTION[2] + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        long size = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        if (size > 0) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            if (folderPath.equals(folderFile.getAbsolutePath())) {
                                Image image = new Image(path, name, dateTime);
                                images.add(image);
                            }
                        }
                    } while (data.moveToNext());
                    imageRecyclerAdapter.setData(images);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static long convertToMilliseconds(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
        long milliseconds = 0;
        try {
            Date date = simpleDateFormat.parse(dateTime);
            milliseconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milliseconds;
    }
}
