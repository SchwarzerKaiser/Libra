package com.leewilson.libra.views;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.leewilson.libra.R;
import com.leewilson.libra.utils.BarcodeAnalyzer;
import com.leewilson.libra.utils.BarcodeAnalyzer.OnBarcodeDetectedListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BarcodeScannerFragment extends Fragment {

    private static final String TAG = "BarcodeScannerFragment";

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA};

    private Preview mPreview;
    private PreviewView mViewFinder;
    private Camera mCamera;
    private Executor mCameraExecutor = Executors.newSingleThreadExecutor();
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;
        initViews(view);

        // Request camera permissions:
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            requestPermissions(PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void initViews(View view) {
        mViewFinder = view.findViewById(R.id.view_finder);
    }

    private void startCamera() {
        ListenableFuture cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider provider = (ProcessCameraProvider) cameraProviderFuture.get();
                mPreview = new Preview.Builder().build();

                ImageAnalysis imgAnalysis = createImageAnalysis();
                imgAnalysis.setAnalyzer(mCameraExecutor, new BarcodeAnalyzer(new OnBarcodeDetectedListener() {
                    @Override
                    public void onBarcodeDetected(List<FirebaseVisionBarcode> barcodes) {
                        handleBarcodes(barcodes);
                    }
                }));

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                provider.unbindAll();
                mCamera = provider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, imgAnalysis, mPreview);

                if (mPreview != null) {
                    mPreview.setSurfaceProvider(
                            mViewFinder.createSurfaceProvider(
                                    mCamera.getCameraInfo()
                            )
                    );
                }
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Use case binding failed.", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void handleBarcodes(List<FirebaseVisionBarcode> barcodes) {
        FirebaseVisionBarcode barcode = barcodes.get(0);

        View windowTop = mRootView.findViewById(R.id.scanner_window_top);
        View windowBottom = mRootView.findViewById(R.id.scanner_window_bottom);

        // Check if inside window, return if it isn't
        Rect barcodeBox = barcode.getBoundingBox();
        if (barcodeBox == null) return;
        if (isViewContains(windowTop, barcodeBox.centerX(), barcodeBox.centerY())) return;
        if (isViewContains(windowBottom, barcodeBox.centerX(), barcodeBox.centerY())) return;

        String isbn = barcode.getRawValue();
        Log.d(TAG, "handleBarcodes: ISBN: " + isbn);

        // Navigate to detail view
        Vibrator v = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        NavDirections action = BarcodeScannerFragmentDirections.navActionToScannedBook(isbn);
        Navigation.findNavController(requireView()).navigate(action);
    }

    private ImageAnalysis createImageAnalysis() {
        return new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(requireActivity(), "Camera permission required.", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).popBackStack();
            }
        }
    }

    private boolean allPermissionsGranted() {
        for (String perm : PERMISSIONS) {
            int status = ContextCompat.checkSelfPermission(requireContext(), perm);
            if (status != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    /**
     * Checks whether the given view is contained within the specified rectangle coordinates.
     */
    private boolean isViewContains(View view, int rx, int ry) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();

        if (rx < x || rx > x + w || ry < y || ry > y + h) {
            return false;
        }
        return true;
    }
}
