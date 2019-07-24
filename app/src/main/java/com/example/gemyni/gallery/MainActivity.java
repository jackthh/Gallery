package com.example.gemyni.gallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private static String perms = "android.permission.READ_EXTERNAL_STORAGE";
    private static int reqCode = 151;
    private static int RESULT_LOAD_IMAGE = 186;
    ArrayList<Image> images = new ArrayList<>();


    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);


        // Check granted permissions and request if needed
        checkSelfPermission(perms);

        // Check the availability of storage
        if (isExternalStorageReadable()) {
            requestImages();
        } else {
            Toast.makeText(this, "External Storage isn't available", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        while (requestCode == reqCode && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Reading external storage is needed to run the app", Toast.LENGTH_SHORT).show();
            this.requestPermissions(new String[]{perms}, reqCode);
        }
    }


    /* Checks if external storage is available to read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    // Loads images
    private void requestImages() {
        Intent imageLoadIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageLoadIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageLoadIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(imageLoadIntent, "Choose images"), RESULT_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            images.clear();

            if (data.getClipData() != null) {
                // Add multi returned images
                int size = data.getClipData().getItemCount();

                for (int i = 0; i < size; i++) {
                    Log.d("Image co so: " + i, " ---");
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    String path = uri.getPath();
                    Image tmp = new Image();

                    tmp.setTitle(path.substring(path.lastIndexOf("/") + 1));
                    tmp.setUri(uri);
                    images.add(tmp);
                    Log.d("Image co so: " + i, " ---" + tmp.getUri());
                }

            } else if (data.getData() != null) {
                // Add 1 returned image
                Uri uri = data.getData();
                String path = uri.getPath();
                Image tmp = new Image();

                tmp.setTitle(path.substring(path.lastIndexOf("/") + 1));
                tmp.setUri(uri);
                images.add(tmp);
                Log.d("Image duy nhat", " ---" + tmp.getUri());

            }

            adapter = new CustomRecyclerViewAdapter(this, images);
            adapter.notifyDataSetChanged();
            layoutManager = new GridLayoutManager(this, 2);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        } else return;
    }
}

