package com.haerul.manado.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.haerul.manado.R;
import com.haerul.manado.base.BaseActivity;
import com.haerul.manado.databinding.ActivityCameraxBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;


//saus: https://codelabs.developers.google.com/codelabs/camerax-getting-started/

public class CameraXActivity extends BaseActivity<ActivityCameraxBinding, CameraXViewModel> {

    private int GALERY_CODE_ADD = 29;
    private int REQUEST_CODE_PERMISSIONS = 10; //arbitrary number, can be changed accordingly
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"}; //array w/ permissions from manifest
    ActivityCameraxBinding binding;
    CameraXViewModel viewModel;
    ImageCapture imageCapture = null;
    FlashMode flashMode;

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camerax;
    }

    @Override
    public CameraXViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this, new CameraXViewModel.ModelFactory(this)).get(CameraXViewModel.class);

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        
        binding.galeryButton.setOnClickListener(view -> {
            Intent pictureActionIntent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(
                    pictureActionIntent,
                    GALERY_CODE_ADD);
        });
    }

    private void startCamera() {
        //make sure there isn't another camera instance running before starting
        CameraX.unbindAll();
        
        flashMode = FlashMode.OFF;
        binding.flashButton.setOnClickListener(v -> {
            if(flashMode == FlashMode.ON) {
                flashMode = FlashMode.OFF;
                binding.flashButton.setImageResource(R.drawable.f1);
                binding.flashMode.setText("Flash OFF");
            }
            else {
                flashMode = FlashMode.ON;
                binding.flashButton.setImageResource(R.drawable.f2);
                binding.flashMode.setText("Flash ON");
            }
            imageCapture.setFlashMode(flashMode);
        });
        
        /* start preview */
        int aspRatioW = binding.viewFinder.getWidth(); //get width of screen
        int aspRatioH = binding.viewFinder.getHeight(); //get height
        Rational asp = new Rational(aspRatioW, aspRatioH); //aspect ratio
        Size screen = new Size(aspRatioW, aspRatioH); //size of the screen

        //config obj for preview/viewfinder thingy.
        PreviewConfig pConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(asp)
                .setTargetResolution(screen)
                .build();
        Preview preview = new Preview(pConfig); //lets build it
        
        //to update the surface texture we have to destroy it first, then re-add it
        preview.setOnPreviewOutputUpdateListener(
                output -> {
                    ViewGroup parent = (ViewGroup) binding.viewFinder.getParent();
                    parent.removeView(binding.viewFinder);
                    parent.addView(binding.viewFinder, 0);

                    binding.viewFinder.setSurfaceTexture(output.getSurfaceTexture());
                    updateTransform();
                });

        /* image capture */

        //config obj, selected capture mode
        ImageCaptureConfig imgCapConfig = new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .setFlashMode(flashMode)
                .build();
        imageCapture = new ImageCapture(imgCapConfig);

        binding.captureButton.setOnClickListener(v -> {
            String imageFileName = "SLW_IMAGE_" + System.currentTimeMillis() + ".jpg";
            File file = new File(Constants.PATH_IMG + "/" + imageFileName);
            imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                @Override
                public void onImageSaved(@NonNull File file) {
                    try {
                        if (file.exists()) {
                            File compressedImageFile = new Compressor(CameraXActivity.this).compressToFile(file.getAbsoluteFile());
                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                BitmapFactory.Options options;
                                String imageInSD = compressedImageFile.getAbsolutePath();
                                Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
                                options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            intent.putExtra("data", file.getAbsolutePath());
                            intent.putExtra("fileName", imageFileName);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("data", file.getAbsolutePath());
                    intent.putExtra("fileName", imageFileName);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                    String msg = "Photo capture failed: " + message;
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                    if(cause != null){
                        cause.printStackTrace();
                    }
                }
            });
        });

        /* image analyser */

        ImageAnalysisConfig imgAConfig = new ImageAnalysisConfig.Builder().setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE).build();
        ImageAnalysis analysis = new ImageAnalysis(imgAConfig);

        analysis.setAnalyzer(
                (image, rotationDegrees) -> {
                    //y'all can add code to analyse stuff here idek go wild.
                });

        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner)this, analysis, imageCapture, preview);
    }

    private void updateTransform(){
        /*
        * compensates the changes in orientation for the viewfinder, bc the rest of the layout stays in portrait mode.
        * methinks :thonk:
        * imgCap does this already, this class can be commented out or be used to optimise the preview
        */
        Matrix mx = new Matrix();
        float w = binding.viewFinder.getMeasuredWidth();
        float h = binding.viewFinder.getMeasuredHeight();

        float centreX = w / 2f; //calc centre of the viewfinder
        float centreY = h / 2f;

        int rotationDgr;
        int rotation = (int)binding.viewFinder.getRotation(); //cast to int bc switches don't like floats

        switch(rotation){ //correct output to account for display rotation
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float)rotationDgr, centreX, centreY);
        binding.viewFinder.setTransform(mx); //apply transformations to textureview
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //start camera when permissions have been granted otherwise exit app
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted(){
        //check if req permissions have been granted
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_CODE_ADD) {
            try {
                if (data != null) {
                    String imageFileName = "SLW_IMAGE_" + System.currentTimeMillis() + ".jpg";
                    File file = new File(Constants.PATH_IMG + "/" + imageFileName);
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath,
                            null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String selectedImagePath = c.getString(columnIndex);
                    c.close();
                    try {
                        File compressedImageFile = new Compressor(this).compressToFile(new File(selectedImagePath));try {
                            FileOutputStream out = new FileOutputStream(file);
                            BitmapFactory.Options options;
                            String imageInSD = compressedImageFile.getAbsolutePath();
                            Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
                            options = new BitmapFactory.Options();
                            options.inSampleSize = 2;
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("data", compressedImageFile.getAbsolutePath());
                        intent.putExtra("fileName", compressedImageFile.getName());
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("data", new File(selectedImagePath).getAbsolutePath());
                    intent.putExtra("fileName", new File(selectedImagePath).getName());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Photo not available!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
