package com.hindsitesapp.multipleimagepicker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultipleImagePickerActivity extends AppCompatActivity {

    private MultiImagePickerListener listener;

    private ArrayList<Folder> folders = new ArrayList<>();
    private FolderRecyclerAdapter folderRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_image_picker);

        Bundle bundle = this.getIntent().getExtras();
        int maxPhotos = bundle.getInt("maxPhotos");
        boolean showToolbar = bundle.getBoolean("showToolbar");
        listener = MultiImagePicker.imagePickerListener;

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (showToolbar) {

            toolbar.setTitle("Select folder");
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
                getSupportActionBar().setTitle("Select folder");
            }
        }



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.image_grid);
        int GRID_SPAN = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                GRID_SPAN,
                GridLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(gridLayoutManager);
        folderRecyclerAdapter = new FolderRecyclerAdapter(getApplicationContext(), this, maxPhotos, showToolbar);
        recyclerView.setAdapter(folderRecyclerAdapter);
        this.getSupportLoaderManager().initLoader(0, null, mLoaderCallback);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, IMAGE_PROJECTION[2] + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        Folder folder = new Folder();
                        folder.name = folderFile.getName();
                        folder.path = folderFile.getAbsolutePath();
                        folder.cover = image;

                        if (!folders.contains(folder)) {
                            folder.images = new ArrayList<>();
                            folder.images.add(image);
                            folders.add(folder);
                        } else {
                            Folder f = folders.get(folders.indexOf(folder));
                            f.images.add(image);
                        }
                    } while (data.moveToNext());
                    folderRecyclerAdapter.setData(folders);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 ) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    List<PickedPhoto> result = (List<PickedPhoto>) data.getExtras().getSerializable("photos");
                    // Create intent to deliver some kind of result data
                    listener.onImagesPicked(result);
                    finish();

                }


            }
        }
    }
}
