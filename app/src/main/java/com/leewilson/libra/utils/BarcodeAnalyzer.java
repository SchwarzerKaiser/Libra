package com.leewilson.libra.utils;

import android.annotation.SuppressLint;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.TYPE_ISBN
                        )
                        .build();
        mDetector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector();
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {

        if (imageProxy.getImage() == null) {
            Log.e(TAG, "Failed to load image from camera feed.");
            return;
        }

        Image mediaImage = imageProxy.getImage();
        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, 0); // May have to configure rotation param!

        Task<List<FirebaseVisionBarcode>> result = mDetector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                        Log.d(TAG, "onSuccess: "+barcodes);
                        if(barcodes != null) {
                            if(barcodes.size() > 0) {
                                mListener.onBarcodeDetected(barcodes);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Barcode detection failure.", e);
                    }
                });

        imageProxy.close();
    }

    public interface OnBarcodeDetectedListener {
        void onBarcodeDetected(List<FirebaseVisionBarcode> barcodes);
    }
}
