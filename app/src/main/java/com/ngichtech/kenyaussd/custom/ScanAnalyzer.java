package com.ngichtech.kenyaussd.custom;

import android.content.Context;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.ngichtech.kenyaussd.MainActivity;
import com.ngichtech.kenyaussd.ScannerActivity;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@ExperimentalGetImage
public class ScanAnalyzer implements ImageAnalysis.Analyzer {
    TextRecognizer textRecognizer;
    Context context;
    Pattern pattern = Pattern.compile("[\\d\\s]+");
    Matcher matcher;

    public ScanAnalyzer(Context context) {
        this.context = context;
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image litImage = imageProxy.getImage();
        InputImage image = InputImage.fromMediaImage(Objects.requireNonNull(litImage), imageProxy.getImageInfo().getRotationDegrees());
        Task<Text> result = textRecognizer.process(image)
                .addOnFailureListener(e -> Log.e(MainActivity.TAG, "AnalyzerErr: " + e.getMessage()));

        result.addOnCompleteListener(task -> {
            for (Text.TextBlock block : result.getResult().getTextBlocks()) {
                String[] blockArray = block.getText().split(" ");
                Matcher blockArrTextM = Pattern.compile("\\d+").matcher(blockArray[0]);

                if (blockArray[0].length() == 5 && blockArrTextM.matches() && blockArray.length >= 3) { // process Airtel airtime TODO: Test this
                    StringBuilder scratchNumber = new StringBuilder();
                    for (int i = 0; i < blockArray.length; i++) {
                        if (i == blockArray.length - 1) { // extract the first 4 digits, the rest is date
                            for (int j = 0; j < 4; j++) {
                                scratchNumber.append(blockArray[i].charAt(j));
                            }
                        }
                        scratchNumber.append(blockArray[i]);
                    }

                    Log.w(MainActivity.TAG, "BlockA: " + Arrays.toString(blockArray));

                    if (matcher.matches()) {
                        ScannerActivity.creditNumber = scratchNumber.toString();
                        ScannerActivity.dataChanged = true;
                    }
                } else if (blockArray[0].length() == 4 && blockArrTextM.matches() && blockArray.length >= 3) { // Process Safaricom and Telkom airtime TODO: Faiba?
                    StringBuilder scratchNumber = new StringBuilder();
                    for (String s : blockArray) {
                        scratchNumber.append(s);
                    }
                    Log.w(MainActivity.TAG, "BlockST: " + Arrays.toString(blockArray) + " <> " + scratchNumber);

                    // TODO: double check with regex, was buggy
                    matcher = Pattern.compile("\\d+").matcher(scratchNumber);
                    if (matcher.matches()) {
                        ScannerActivity.creditNumber = scratchNumber.toString();
                        ScannerActivity.dataChanged = true;
                    }
                }
                // release camera processing but provide a refresh button
            }
            imageProxy.close();
        });
    }
}
