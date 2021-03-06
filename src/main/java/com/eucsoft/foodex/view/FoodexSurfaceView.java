package com.eucsoft.foodex.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.eucsoft.foodex.log.Log;
import com.eucsoft.foodex.util.CameraUtil;

import java.io.IOException;

public class FoodexSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;
    private Bitmap currentBitmap;
    private SurfaceHolder holder;

    public FoodexSurfaceView(Context context, Camera camera) {
        super(context);

        this.camera = camera;

        holder = getHolder();
        holder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
        if (currentAPIVersion < Build.VERSION_CODES.HONEYCOMB) {
            getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        getHolder().setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(FoodexSurfaceView.class, "Error setting camera preview: ",e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        params.setRotation(90);

        Camera.Size cameraSize = null;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            cameraSize = CameraUtil.getBestPictureSizeForOldDevices(params.getSupportedPictureSizes());
        } else {
            cameraSize = CameraUtil.getBestPictureSize(params.getSupportedPictureSizes());
        }
        params.setPictureSize(cameraSize.width, cameraSize.height);

        //TODO: Set MAXIMUM Size
        params.setPictureSize(cameraSize.width, cameraSize.height);
        camera.setParameters(params);

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e){
            Log.d(FoodexSurfaceView.class, "Error starting camera preview: ", e.getMessage());
        }
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }
}
