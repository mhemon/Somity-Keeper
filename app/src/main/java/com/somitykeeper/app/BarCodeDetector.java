package com.somitykeeper.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;
import java.util.Objects;

public class BarCodeDetector extends AppCompatActivity {

    private CameraView cameraView;
    private boolean isDetected = false;
    Button btn_start_again;
    MediaPlayer mediaPlayer;
    private boolean onlyone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_detector);
        // for permission handler
        requestForcamera();
        // [START set_detector_options]
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
    }

    private void requestForcamera() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    requestforpreview();
                } else {
                    requestForcamera();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void requestforpreview() {
        cameraView = findViewById(R.id.camera_view);
        btn_start_again = findViewById(R.id.btn_again);
        btn_start_again.setEnabled(isDetected);
        btn_start_again.setAlpha(1f);
        cameraView.setLifecycleOwner(this);
        if (!isDetected) {
            cameraView.addFrameProcessor(new FrameProcessor() {
                @Override
                public void process(@NonNull Frame frame) {
                    byte[] data = frame.getData();
                    InputImage image = InputImage.fromByteArray(data, frame.getSize().getWidth(), frame.getSize().getHeight(), frame.getRotation(), InputImage.IMAGE_FORMAT_NV21);
                    requestforscan(image);
                }
            });
        }
    }

    private void requestforscan(InputImage image) {
        BarcodeScanner scanner = BarcodeScanning.getClient();
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {

                        if (barcodes.size() > 0) {
                            isDetected = true;
                            stopcamerapreview();

                            // Task completed successfully
                            // [START_EXCLUDE]
                            // [START get_barcodes]
                            for (Barcode barcode : barcodes) {
                                Rect bounds = barcode.getBoundingBox();
                                Point[] corners = barcode.getCornerPoints();

                                String rawValue = barcode.getRawValue();

                                int valueType = barcode.getValueType();
                                // See API reference for complete list of supported types
                                if (valueType == Barcode.TYPE_CONTACT_INFO) {
                                    String emon = barcode.getContactInfo().getName().getFormattedName().toString();
                                    String url = barcode.getContactInfo().getUrls().get(0).toString();
                                    String Organization = barcode.getContactInfo().getOrganization().toString();
                                    //createdialog("Setup successfully completed!"+"\n"+"Name:- "+emon+"\n"+"Somity Name:- "+Organization+"\n"+"Software url:- "+url);
                                    Prefs.putString("NAME", emon);
                                    Prefs.putString("WEB", url);
                                    Prefs.putString("SOMITY", Organization);
                                    startanother();

                                } else {
                                    createdialog("Our machine learning technology didn't recognise this qr code. maybe it didn't belong to us!");
                                }
                            }

                        }
                        // [END get_barcodes]
                        // [END_EXCLUDE]

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BarCodeDetector.this, "Internal Error!", Toast.LENGTH_SHORT).show();
                    }
                });
        // [END run_detector]
    }

    private void startanother() {
        if (!onlyone) {
            onlyone = true;
            Intent intent = new Intent(BarCodeDetector.this, MainActivity.class);
            startActivity(intent);
            mediaPlayer = MediaPlayer.create(BarCodeDetector.this, R.raw.sound);
            mediaPlayer.start();
            finish();
        }
    }

    private void stopcamerapreview() {
        cameraView.clearFrameProcessors();
    }

    private void createdialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(BarCodeDetector.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}

