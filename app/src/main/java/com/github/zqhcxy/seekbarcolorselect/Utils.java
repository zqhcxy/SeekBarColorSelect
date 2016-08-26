package com.github.zqhcxy.seekbarcolorselect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zqh-pc on 2016/8/24.
 */
public class Utils {
    private Camera mCamera;
    private int bCamId = -1, fCamId = 1;
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = ".ad";

    public Utils() {
        initCamera();
    }
    public void tackPic(){
        if(mCamera!=null){
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    private void initCamera() {
        int PreviewWidth = 0;
        int PreviewHeight = 0;
        int num = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < num; i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                bCamId = i;
            }
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                fCamId = i;
            }
        }
        try {
//            releaseCameraAndPreview();
            if (bCamId != -1)
                mCamera = Camera.open(bCamId);
            else if (fCamId != -1) {
                mCamera = Camera.open(fCamId);
            } else {
                return;
            }
        } catch (Exception e) {
            Log.e("utils", "failed to open Camera");
            e.printStackTrace();
        }
        try {
//            mCamera.setPreviewDisplay(holder);
            Camera.Parameters p = mCamera.getParameters();
            List<Camera.Size> prvSizes = p.getSupportedPreviewSizes();
            p.set("jpeg-quality", 100);// 设置照片质量
            if (prvSizes.size() > 1) {
                Iterator<Camera.Size> it = prvSizes.iterator();
                while (it.hasNext()) {
                    Camera.Size s = it.next();
//                    if(s.width>prvw &&s.width<240&&s.height>prvw&&s.height<320){
//                        prvw=s.width;
//                        prvh=s.height;
//                    }
                    if (s.width >= PreviewWidth
                            && s.height >= PreviewHeight) {
                        PreviewWidth = s.width;
                        PreviewHeight = s.height;
                        break;
                    }
                }
            }
            p.setPreviewSize(PreviewWidth, PreviewHeight); // 获得摄像区域的大小
            p.setPictureSize(PreviewWidth, PreviewHeight); // 获得保存图片的大小
//            sv.setLayoutParams(new RelativeLayout.LayoutParams(PreviewWidth,PreviewWidth));
//            p.setPreviewSize(prvw, prvh);
//            sv.setLayoutParams(new RelativeLayout.LayoutParams(prvw,prvh));
//            Toast.makeText(getApplicationContext(), "宽:"+prvw+"高:"+prvh, Toast.LENGTH_LONG).show();
            mCamera.setParameters(p);
            mCamera.startPreview();
            //cam.startPreview();
        } catch (Exception e) {
            Log.i("utils", e.getMessage());
        }
    }


    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };
    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
//                mCamera.stopPreview();
            }
            //保存图片到sdcard
            if (null != b) {
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
                Bitmap rotaBitmap = getRotateBitmap(b, 90.0f);
                saveBitmap(rotaBitmap);
            }
            //再次进入预览
//            mCamera.startPreview();
        }
    };


    /**
     * 保存Bitmap到sdcard
     *
     * @param b
     */
    public static void saveBitmap(Bitmap b) {

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
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


    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }


    private static String initPath() {
        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    public void destroy() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
