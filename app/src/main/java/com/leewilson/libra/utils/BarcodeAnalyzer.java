package com.leewilson.libra.utils;

import android.annotation.SuppressLint;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

/**
 * Class for analyzing an image, detecting barcodes, and notifying the client when one is found.
 */
public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {

    private static final String TAG = "BarcodeAnalyzer";

    private OnBarcodeDetectedListener mListener;
    private FirebaseVisionBarcodeDetector mDetector;

    public BarcodeAnalyzer(OnBarcodeDetectedListener listener) {
        mListener = listener;
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_EAN_13)
            .build();
        mDetector = FirebaseVision.getInstance()
            .getVisionBarcodeDetector(options);
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {

        if (imageProxy.getImage() == null) {
            Log.e(TAG, "Failed to load image from camera feed.");
            return;
        }

        Image mediaImage = imageProxy.getImage();
        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, 0);

        Task<List<FirebaseVisionBarcode>> result = mDetector.detectInImage(image)
                .addOnSuccessListener(barcodes -> {
                    Log.d(TAG, "onSuccess: "+barcodes);
                    if(barcodes != null) {
                        if(barcodes.size() > 0) {
                            mListener.onBarcodeDetected(barcodes);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Barcode detection failure.", e);
                });

        imageProxy.close();
    }

    public interface OnBarcodeDetectedListener {
        void onBarcodeDetected(List<FirebaseVisionBarcode> barcodes);
    }
}
