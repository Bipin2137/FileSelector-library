package com.bipin.filepicker;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bipin.fileselector.FileUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int RC_CAMERA_PERM = 100;
    private static final int REQUEST_SELECT_FILE = 101;
    private TextView tvPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnChoose = findViewById(R.id.btnChoose);
        tvPath = findViewById(R.id.tvPath);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraTask();
            }
        });
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (hasCameraPermission()) {
            // Have permission, do the thing!
            openFilePicker();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "",
                    RC_CAMERA_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), REQUEST_SELECT_FILE);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_FILE:
                    Uri uri = data.getData();

                    String path = FileUtils.getPath(this, uri);

                    if (!TextUtils.isEmpty(path)) {
                        tvPath.setText(path);
                    } else {
                        tvPath.setText("File Not Found");
                    }

                    break;
            }
        }
    }
}
