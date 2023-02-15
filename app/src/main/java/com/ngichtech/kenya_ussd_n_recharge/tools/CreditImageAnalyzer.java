package com.ngichtech.kenya_ussd_n_recharge.tools;

import android.graphics.Matrix;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

public class CreditImageAnalyzer implements ImageAnalysis.Analyzer {
    @Override
    public void analyze(@NonNull ImageProxy image) {

    }

    @Nullable
    @Override
    public Size getDefaultTargetResolution() {
        return ImageAnalysis.Analyzer.super.getDefaultTargetResolution();
    }

    @Override
    public int getTargetCoordinateSystem() {
        return ImageAnalysis.Analyzer.super.getTargetCoordinateSystem();
    }

    @Override
    public void updateTransform(@Nullable Matrix matrix) {
        ImageAnalysis.Analyzer.super.updateTransform(matrix);
    }
}
