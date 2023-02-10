package com.ngichtech.kenyaussd;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.ngichtech.kenyaussd.custom.ScanAnalyzer;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@ExperimentalGetImage
public class ScannerActivity extends AppCompatActivity {

    public static String creditNumber = "";
    public static boolean dataChanged;
    EditText recognizedCreditText;
    FloatingActionButton fabCameraFlash;
    PreviewView cameraPreview;
    CameraControl cameraControl;
    private boolean flashOn;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        recognizedCreditText = findViewById(R.id.recognizedCreditText);
        fabCameraFlash = findViewById(R.id.fabCameraFlash);
        cameraPreview = findViewById(R.id.cameraPreview);

        fabCameraFlash.setOnClickListener(v -> {
            if (flashOn) {
                fabCameraFlash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_flash_off_24, null));
                cameraControl.enableTorch(false);
                flashOn = false;
            } else {
                fabCameraFlash.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_flash_on_24, null));
                cameraControl.enableTorch(true);
                flashOn = true;
            }
        });

        bindToCamera();

        new Thread(() -> {
            while (true) {
                if (dataChanged) {
                    StringBuilder rectifiedString = new StringBuilder();
                    for (int i = 0; i < creditNumber.length(); i++) {
                        try {
                            rectifiedString.append(Integer.parseInt(String.valueOf(creditNumber.charAt(i))));
                        } catch (NumberFormatException ignore) {
                        }
                    }
                    runOnUiThread(() -> recognizedCreditText.setText(rectifiedString));
                    dataChanged = false;
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void bindToCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void bindPreview(ProcessCameraProvider cameraProvider) {
        ScanAnalyzer imageAnalyzer = new ScanAnalyzer(this);

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
//                .setTargetResolution(new Size(176, 144))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(getMainExecutor(), imageAnalyzer);
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());
        ViewPort viewPort = cameraPreview.getViewPort();
        /* ViewPort, viewPort = new ViewPort.Builder(new Rational( 1, 4), Surface.ROTATION_90).build(); */

        Log.w(MainActivity.TAG, "ViewPort: " + cameraPreview.getScaleType() + " <> " + Objects.requireNonNull(viewPort).getAspectRatio());

        if (viewPort != null) {
            UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                    .addUseCase(preview)
                    .addUseCase(imageAnalysis)
                    .setViewPort(viewPort)
                    .build();
            cameraProvider.unbindAll();
            Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup);//, imageAnalysis,preview);
            cameraControl = camera.getCameraControl();
            cameraControl.setLinearZoom(0f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flashOn = false;
    }
}
