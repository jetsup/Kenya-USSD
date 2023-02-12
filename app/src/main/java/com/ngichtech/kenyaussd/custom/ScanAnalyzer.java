package com.ngichtech.kenyaussd.custom;

import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_SAFARICOM;
import static com.ngichtech.kenyaussd.custom.ISPConstants.ISP_NAME_TELKOM;
import static com.ngichtech.kenyaussd.custom.ISPConstants.SAFARICOM_CREDIT_LENGTH;
import static com.ngichtech.kenyaussd.custom.ISPConstants.SAFARICOM_RECHARGE_PREFIX;
import static com.ngichtech.kenyaussd.custom.ISPConstants.TELKOM_CREDIT_LENGTH;
import static com.ngichtech.kenyaussd.custom.ISPConstants.TELKOM_RECHARGE_PREFIX;

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
    StringBuilder scratchNumber = new StringBuilder();
    String prefix = "";
    String rechargeFor = "";
    Pattern pattern = Pattern.compile("[\\d\\s]+");
    Matcher matcher;
    boolean canScan = true;
    private String TAG = "MyTag";
    private volatile boolean done = false;

    public ScanAnalyzer(Context context) {
        this.context = context;
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        if (done)
            return;
        Image litImage = imageProxy.getImage();
        InputImage image = InputImage.fromMediaImage(Objects.requireNonNull(litImage), imageProxy.getImageInfo().getRotationDegrees());
        Task<Text> result = textRecognizer.process(image)
                .addOnFailureListener(e -> Log.e(MainActivity.TAG, "AnalyzerErr: " + e.getMessage()))
                .addOnCanceledListener(() -> {
                    Log.w("MyTag", "Canceled");
                    imageProxy.close();
                });
        result.addOnCompleteListener(task -> {
            try {
                if (!done) {
                    for (Text.TextBlock block : result.getResult().getTextBlocks()) {
                        String[] blockArray = block.getText().split(" ");
                        Matcher blockArrTextM = Pattern.compile("\\d+").matcher(blockArray[0]);

                        if (blockArray[0].length() == 5 && blockArrTextM.matches() && blockArray.length >= 3) { // process Airtel airtime TODO: Test this
                            scratchNumber = new StringBuilder();
                            for (int i = 0; i < blockArray.length; i++) {
                                if (i == blockArray.length - 1) { // extract the first 4 digits, the rest is date
                                    for (int j = 0; j < 4; j++) {
                                        scratchNumber.append(blockArray[i].charAt(j));
                                    }
                                }
                                scratchNumber.append(blockArray[i]);
                            }

                            Log.w(MainActivity.TAG, "BlockA: " + Arrays.toString(blockArray));

//                            if (matcher.matches()) {
//                                ScannerActivity.creditNumber = scratchNumber.toString();
//                                ScannerActivity.dataChanged = true;
//                            }
                        } else if (blockArray[0].length() == 4 && blockArrTextM.matches() && blockArray.length >= 3) { // Process Safaricom and Telkom airtime TODO: Faiba?
                            Log.w(TAG, "analyze: " + Arrays.toString(blockArray));
                            scratchNumber = new StringBuilder();
                            for (String s : blockArray) {
                                scratchNumber.append(s);
                            }
                            Log.w(MainActivity.TAG, "BlockST: " + Arrays.toString(blockArray) + " <> " + scratchNumber);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "ErrTRY: " + e.getMessage());
            }
            imageProxy.close();
        });
        // TODO: double check with regex, was buggy
        Log.w("MyTag", "analyzing: " + scratchNumber);
        if (scratchNumber.toString().length() > 10) {
            matcher = Pattern.compile("\\d+").matcher(scratchNumber.toString());
            canScan = false;
            if (!done) {
                if (matcher.matches()) {
                    Log.w(TAG, "Scratchcard: " + scratchNumber);
                    int creditStringLength = scratchNumber.toString().length();

                    if (creditStringLength == SAFARICOM_CREDIT_LENGTH) {
                        ScannerActivity.prefix = SAFARICOM_RECHARGE_PREFIX;
                        ScannerActivity.rechargeFor = ISP_NAME_SAFARICOM;
                    } else if (creditStringLength == TELKOM_CREDIT_LENGTH) {
                        ScannerActivity.prefix = TELKOM_RECHARGE_PREFIX;
                        ScannerActivity.rechargeFor = ISP_NAME_TELKOM;
                    }

                    /*Uri dialUri = Uri.parse("tel:" + prefix + scratchNumber + Uri.encode("#"));
                    Intent rechargeIntent = new Intent(Intent.ACTION_CALL);
                    rechargeIntent.setData(dialUri);
                    if (ScannerActivity.simInfo.size() > 0) {
                        rechargeIntent.putExtra(SELECT_SIM_SLOT, ScannerActivity.simInfo.get(rechargeFor));
                    }
                    done = true;
                    Log.w(TAG, "Proxy closed: " + rechargeFor);
                    Toast.makeText(context, "Recharging " + rechargeFor, Toast.LENGTH_SHORT).show();
                    MainActivity.context.startActivity(rechargeIntent);*/
//                    Uri dialUri = Uri.parse("tel:" + prefix + scratchNumber + Uri.encode("#"));
//                    Intent rechargeIntent = new Intent(Intent.ACTION_CALL);
//                    rechargeIntent.setData(dialUri);
//                    if (ScannerActivity.simInfo.size() > 0) {
//                        rechargeIntent.putExtra(SELECT_SIM_SLOT, ScannerActivity.simInfo.get(rechargeFor));
//                    }
//                    done = true;
//                    Log.w(TAG, "Proxy closed: " + rechargeFor);
//                    Toast.makeText(context, "Recharging " + rechargeFor, Toast.LENGTH_SHORT).show();
//                    MainActivity.context.startActivity(rechargeIntent);
                    imageProxy.close();
                    ScannerActivity.creditNumber = scratchNumber.toString();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ScannerActivity.dataChanged = true;
                }
            }
        }
    }
}
