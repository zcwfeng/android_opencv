package top.zcwfeng.opencv;

import android.view.Surface;

public class FaceTracker {

    private long mNativeObj = 0;


    public FaceTracker(String model) {
        mNativeObj = nativeCreateObject(model);
    }

    public void setSurface(Surface surface) {
        nativeSetSurface(mNativeObj, surface);
    }

    public void release() {
        nativeDestroyObject(mNativeObj);
        mNativeObj = 0;
    }

    public void start() {
        nativeStart(mNativeObj);
    }

    public void stop() {
        nativeStop(mNativeObj);
    }

    ///其他逻辑
    public void detect(byte[] inputImage, int width, int height, int rotationdegrees) {
        nativeDetect(mNativeObj, inputImage, width, height, rotationdegrees);
    }

    private static native long nativeCreateObject(String model);


    private static native void nativeDestroyObject(long thiz);

    private static native void nativeSetSurface(long thiz, Surface surface);

    private static native void nativeStart(long thiz);

    private static native void nativeStop(long thiz);

    private static native void nativeDetect(long thiz, byte[] inputImage, int width, int height, int rotationDegrees);
}
