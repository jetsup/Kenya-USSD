package com.ngichtech.kenyaussd;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngichtech.kenyaussd.adapters.ISPAdapter;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "My_Tag";
    private final int CALL_PERMISSION_REQUEST = 1;
    RecyclerView mainRecyclerView;
    File myDirectory;

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Allow Call Permission")
                    .setMessage("This permission is needed by this app.")
                    .setPositiveButton("OK", (dialogInterface, i) ->
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                    CALL_PERMISSION_REQUEST))
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "The Permission has been successfully granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "The permission was not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.ussd);
        // Manage permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            requestCallPermission();
        }
        // Start initialization here
        mainRecyclerView = findViewById(R.id.ispRecyclerView);
        ISPAdapter ispAdapter = new ISPAdapter(MainActivity.this);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mainRecyclerView.setAdapter(ispAdapter);

        // No permission required for this
        myDirectory = new File(Objects.requireNonNull(getExternalCacheDir().getParentFile()).getPath() + "/Kenya USSD/");
        if (!myDirectory.exists()) {
            if (!myDirectory.mkdir()) {
                Toast.makeText(this, "The folder was not created", Toast.LENGTH_LONG).show();
            }
        }
    }
}
