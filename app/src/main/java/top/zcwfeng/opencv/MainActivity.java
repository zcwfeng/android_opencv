package top.zcwfeng.opencv;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageProxy;

public class MainActivity extends AppCompatActivity  implements ImageAnalysis.Analyzer{

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private HandlerThread handlerThread;
    private Handler handler;
    private CameraX.LensFacing currentFacing = CameraX.LensFacing.BACK;
    private FaceTracker faceTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path  = Utils.copyAsset2Sdcard(this, "lbpcascade_frontalface.xml");

        faceTracker = new FaceTracker(path);
        faceTracker.start();

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                faceTracker.setSurface(holder.getSurface());
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                faceTracker.setSurface(null);
            }
        });
        handlerThread = new HandlerThread("Analyze-thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        CameraX.bindToLifecycle(this, getAnalysis());
    }


    @Override
    public void analyze(ImageProxy image, int rotationDegrees) {
        // YUV420_888 -> I420
        byte[] bytes = ImageUtils.getBytes(image);
        //定位人脸，并且显示摄像头的图像
        faceTracker.detect(bytes, image.getWidth(), image.getHeight(), rotationDegrees);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quitSafely();
        faceTracker.release();
    }

    private ImageAnalysis getAnalysis() {
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder()
                .setCallbackHandler(handler)
                .setLensFacing(currentFacing)
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
        imageAnalysis.setAnalyzer(this);
        return imageAnalysis;
    }

    public void toggleCamera(View view) {
        CameraX.unbindAll();
        if (currentFacing == CameraX.LensFacing.BACK) {
            currentFacing = CameraX.LensFacing.FRONT;
        } else {
            currentFacing = CameraX.LensFacing.BACK;
        }
        CameraX.bindToLifecycle(this, getAnalysis());
    }

}