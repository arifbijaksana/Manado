package com.haerul.manado.utils;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Admin21
 */
public class PreviewCamera extends ViewGroup implements SurfaceHolder.Callback {
        private static final int FOCUS_AREA_SIZE = 300;
        private final String TAG = "Preview";
        SurfaceView mSurfaceView;
        SurfaceHolder mHolder;
        Camera.Parameters mParams;
        Size mPreviewSize;
        List<Size> mSupportedPreviewSizes;
        Camera mCamera;
        WindowManager wm;
        Display display;
        Context mContext;

    @SuppressWarnings("deprecation")
    public PreviewCamera(Context context, SurfaceView sv) {
            super(context);
            mContext = context;

            mSurfaceView = sv;
            wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void setCamera(Camera camera) {
            mCamera = camera;
            if (mCamera != null) {
                mSupportedPreviewSizes = mCamera.getParameters().getSupportedPictureSizes();
                requestLayout();
                Camera.Parameters params = mCamera.getParameters();
                mParams = mCamera.getParameters();

// Check what resolutions are supported by your camera

// Iterate through all available resolutions and choose one.
// The chosen resolution will be stored in mSize.
//            Size mSize;
//            for (Size size : sizes) {
//                Log.i(TAG, "Available resolution: "+size.width+" "+size.height);
//            }
//
//            Log.i(TAG, "Chosen resolution: "+ mSize.width+" "+mSize.height);
//            params.setPictureSize(mSize.width, mSize.height);
//            mCamera.setParameters(params);

                // get Camera parameters
                List<String> focusModes = params.getSupportedFocusModes();
                List<String> steadyModes = params.getSupportedSceneModes();
                if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }

                if (steadyModes != null && steadyModes.contains(Camera.Parameters.SCENE_MODE_STEADYPHOTO)) {
                    params.setSceneMode(Camera.Parameters.SCENE_MODE_STEADYPHOTO);
                }
                mCamera.setParameters(params);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // We purposely disregard child measurements because act as a
            // wrapper to a SurfaceView that centers the camera preview instead
            // of stretching it.
            final int width = getResources().getDisplayMetrics().widthPixels;
            final int height = getResources().getDisplayMetrics().heightPixels;

            if (mSupportedPreviewSizes != null) {

                mPreviewSize = getBestPreviewSize(mParams);
            }
            float ratio;
            if (mPreviewSize.height >= mPreviewSize.width) {
                ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
            } else {
                ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;
            }
            float camHeight = width * ratio;
            float newCamHeight, newHeightRatio;

            if (camHeight < height) {
                newHeightRatio = (float) height / (float) mPreviewSize.height;
                newCamHeight = (newHeightRatio * camHeight);
                setMeasuredDimension((int) (width * newHeightRatio), (int) newCamHeight);
            } else {
                newCamHeight = camHeight;
                setMeasuredDimension(width, (int) newCamHeight);
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if (changed && getChildCount() > 0) {
                final View child = getChildAt(0);

                final int width = r - l;
                final int height = b - t;

                int previewWidth = width;
                int previewHeight = height;
                if (mPreviewSize != null) {
                    previewWidth = mPreviewSize.width;
                    previewHeight = mPreviewSize.height;
                }

                // Center the child SurfaceView within the parent.
                if (width * previewHeight > height * previewWidth) {
                    final int scaledChildWidth = previewWidth * height / previewHeight;
                    child.layout((width - scaledChildWidth) / 1, 0,
                            (width + scaledChildWidth) / 1, height);
                } else {
                    final int scaledChildHeight = previewHeight * width / previewWidth;
                    child.layout(0, (height - scaledChildHeight) / 1,
                            width, (height + scaledChildHeight) / 1);
                }
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, acquire the camera and tell it where
            // to draw.
            try {
                if (mCamera != null) {
                    Camera.Parameters cameraParameters = mCamera.getParameters();
                    mParams = mCamera.getParameters();
                    Size optimalPreviewSize = getBestPreviewSize(cameraParameters);
                    cameraParameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
                    mParams.setPictureSize(optimalPreviewSize.width, optimalPreviewSize.height);
                    mCamera.setParameters(cameraParameters);
                    mCamera.setPreviewDisplay(holder);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // Surface will be destroyed when we return, so stop the preview.
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }

        private Rect calculateFocusArea(float x, float y) {
            int left = clamp(Float.valueOf((x / getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
            int top = clamp(Float.valueOf((y / getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

            return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
        }

        private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
            int result;
            if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
                if (touchCoordinateInCameraReper > 0) {
                    result = 1000 - focusAreaSize / 2;
                } else {
                    result = -1000 + focusAreaSize / 2;
                }
            } else {
                result = touchCoordinateInCameraReper - focusAreaSize / 2;
            }
            return result;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mCamera != null) {
                if (mHolder.getSurface() == null)//check if the surface is ready to receive camera data
                    return;
                try {
                    mCamera.stopPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                    //this will happen when you are trying the camera if it's not running
                }
                //now, recreate the camera preview
                try {
                    //set the focusable true
                    this.setFocusable(true);
                    //set the touch able true
                    this.setFocusableInTouchMode(true);

                    Display display = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                    Camera.Parameters params = mCamera.getParameters();
                    Size optimalPreviewSize = getBestPreviewSize(params);
                    Size optimalSize = getBestPictureSize(params);

                    if (display.getRotation() == Surface.ROTATION_0) {
                        params.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
                        mCamera.setDisplayOrientation(90);
                    }

                    if (display.getRotation() == Surface.ROTATION_90) {
                        params.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
                    }

                    if (display.getRotation() == Surface.ROTATION_180) {
                        params.setPreviewSize(optimalPreviewSize.height, optimalPreviewSize.width);
                    }

                    if (display.getRotation() == Surface.ROTATION_270) {
                        params.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
                        mCamera.setDisplayOrientation(180);
                    }

                    String supportedValues = params.get("iso-values");
                    if (supportedValues != null) {
                        params.set("iso", "auto");
                    }
                    params.setPictureSize(optimalSize.width, optimalSize.height);
                    mCamera.setParameters(params);
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();

                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }

        private Size getBestPreviewSize(Camera.Parameters parameters) {
            Size bestSize;
            List<Size> sizeList = parameters.getSupportedPreviewSizes();

            bestSize = sizeList.get(0);

            for (int i = 0; i < sizeList.size(); i++) {
                if (sizeList.get(i).width > bestSize.width)
                    bestSize = sizeList.get(i);
            }

            return bestSize;
        }


        private Size getBestPictureSize(Camera.Parameters parameters) {
            Size bestSize;
            List<Size> sizeList = parameters.getSupportedPictureSizes();

            bestSize = sizeList.get(0);

            for (int i = 0; i < sizeList.size(); i++) {
                if (sizeList.get(i).width > bestSize.width)
                    bestSize = sizeList.get(i);
            }

            return bestSize;
        }
}