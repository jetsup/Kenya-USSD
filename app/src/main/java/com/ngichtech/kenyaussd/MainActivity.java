package com.ngichtech.kenyaussd;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private final int CALL_PERMISSION_REQUEST = 1;
    String[] serviceProviders = {"Safaricom", "Telkom", "Airtel", "Faiba"};
    String[] serviceProviderSlogans = {"The Better Option", "Moving Forward", "The Smartphone Network", "I am Future Proof"};
    File myDirectory = new File(Environment.getExternalStorageDirectory() + "/Kenya USSD");

    {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission already Granted!", Toast.LENGTH_SHORT).show();
        } else {
            requestCallPermission();
        }
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Allow Call Permission")
                    .setMessage("This message is needed by this app!!")
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean directoryExist = myDirectory.exists();
        if (directoryExist) {
            Toast.makeText(this, "Directory already Exists", Toast.LENGTH_SHORT).show();
        } else {
            boolean isCreated = myDirectory.mkdir();
            if (isCreated) {
                Toast.makeText(this, "Directory created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Oops, the directory was not created!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}