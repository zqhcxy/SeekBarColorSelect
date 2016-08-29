package com.github.zqhcxy.seekbarcolorselect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private SurfaceView sv;
    private SurfaceHolder sh;
    private Camera cam;
    private int bCamId=-  1,fCamId=  1;
    private int prvw=100,prvh=100;

    LinearLayout take1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sv=(SurfaceView)findViewById(R.id.surfaceView1);
        sh=sv.getHolder();
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sh.setFormat(Color.TRANSPARENT);
        sh.setFixedSize(500, 350);
        sh.addCallback(this);

         take1=(LinearLayout)findViewById(R.id.take1);
        take1.setOnClickListener(this);
//        sv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cam.takePicture(mShutterCallback, null, mJpegPictureCallback);
//            }
//        });
    }

    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
//            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Bitmap b = null;
            if(null != data){
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                cam.stopPreview();

//                isPreviewing = false;
            }
            //保存图片到sdcard
            if(null != b)
            {
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
                Bitmap rotaBitmap = getRotateBitmap(b, 90.0f);
                saveBitmap(rotaBitmap);
            }
            //再次进入预览
            cam.startPreview();
        }
    };
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub

        int PreviewWidth=0;
        int PreviewHeight=0;
        int num=Camera.getNumberOfCameras();
        Camera.CameraInfo ci=new Camera.CameraInfo();
        for(int i=  0;i<num;i++){
            Camera.getCameraInfo(i, ci);
            if(ci.facing== Camera.CameraInfo.CAMERA_FACING_BACK){
                bCamId=i;
            }
            if(ci.facing== Camera.CameraInfo.CAMERA_FACING_FRONT){
                fCamId=i;
            }
        }
        try {
//            releaseCameraAndPreview();
            if(bCamId !=-  1)
                cam=Camera.open(bCamId);
            else if(fCamId!=-  1){
                cam=Camera.open(fCamId);
            }
            else{
                return;
            }
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }
        try {
            cam.setPreviewDisplay(holder);
            Camera.Parameters p=cam.getParameters();
            List<Camera.Size> prvSizes=p.getSupportedPreviewSizes();
            p.set("jpeg-quality", 100);// 设置照片质量
            if(prvSizes.size()>  1){
                Iterator<Camera.Size> it=prvSizes.iterator();
                while(it.hasNext()){
                    Camera.Size s=it.next();
                    if(s.width>prvw &&s.width<240&&s.height>prvw&&s.height<320){
                        prvw=s.width;
                        prvh=s.height;
                    }
//                    if (s.width >= PreviewWidth
//                            && s.height >= PreviewHeight) {
//                        PreviewWidth = s.width;
//                        PreviewHeight = s.height;
//                        break;
//                    }
                }
            }
//            p.setPreviewSize(PreviewWidth, PreviewHeight); // 获得摄像区域的大小
//            p.setPictureSize(PreviewWidth, PreviewHeight); // 获得保存图片的大小
//            sv.setLayoutParams(new RelativeLayout.LayoutParams(PreviewWidth,PreviewWidth));
            p.setPreviewSize(prvw, prvh);
            sv.setLayoutParams(new RelativeLayout.LayoutParams(prvw,prvh));
//            Toast.makeText(getApplicationContext(), "宽:"+prvw+"高:"+prvh, Toast.LENGTH_LONG).show();
            cam.setParameters(p);
            cam.startPreview();
            //cam.startPreview();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }


    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static   String storagePath = "";
    private static final String DST_FOLDER_NAME = ".ad";
    private static String initPath(){
        if(storagePath.equals("")){
            storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if(!f.exists()){
                f.mkdir();
            }
        }
        return storagePath;
    }

    /**保存Bitmap到sdcard
     * @param b
     */
    public static void saveBitmap(Bitmap b){

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake +".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void releaseCameraAndPreview() {
//        sv.setCamera(null);
        if (cam != null) {
            cam.release();
            cam = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take1:
                cam.takePicture(mShutterCallback, null, mJpegPictureCallback);
                break;
        }
    }
}
