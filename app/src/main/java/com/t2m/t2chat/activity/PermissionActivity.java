package com.t2m.t2chat.activity;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionActivity extends AppCompatActivity {
    private static final String TAG = PermissionActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            onPermissionReady();
        }
    }

    public abstract String[] permissions();

    protected void onPermissionReady() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10086) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Grant permission failed. Exit activity!!");
                    finish();
                    return;
                }
            }
        }

        // callback
        onPermissionReady();
    }

    private boolean checkPermission() {
        // check permission and get permissions to request
        List<String> requestPermissions = new ArrayList<>();
        for (String permission : permissions()) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permission);
            }
        }

        // request permissions
        if (!requestPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[0]), 10086);
        }

        return requestPermissions.isEmpty();
    }
}

