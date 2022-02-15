package com.ondevice.myjobcard_glass;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements Callback
{
    private SurfaceHolder surfaceHolder = null;
    public static Camera camera = null;
    @SuppressWarnings("deprecation")
    public CameraView(Context context)
    {
        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try
        {
        camera = Camera.open();
        //this.setCameraParameters(camera);

            camera.setPreviewDisplay(surfaceHolder);
        }catch (Exception e)
        {
            this.releaseCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (camera != null)
        {
            camera.startPreview();
            camera.setDisplayOrientation(90);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.releaseCamera();
    }
    public void setCameraParameters(Camera camera)
    {
        if (camera != null)
        {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewFpsRange(30000, 30000);
            camera.setParameters(parameters);
        }
    }
    public void releaseCamera()
    {
        if (camera != null)
        {
            camera.release();
            camera = null;
        }
    }
}

