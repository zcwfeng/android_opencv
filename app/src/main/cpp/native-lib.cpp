#include <jni.h>
#include <string>

#include <opencv2/opencv.hpp>

using namespace cv;


class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(cv::Ptr<cv::CascadeClassifier> detector) :
            IDetector(),
            Detector(detector) {
        CV_Assert(detector);
    }

    void detect(const cv::Mat &Image, std::vector<cv::Rect> &objects) {

        Detector->detectMultiScale(Image, objects, scaleFactor, minNeighbours, 0, minObjSize,
                                   maxObjSize);

    }

    virtual ~CascadeDetectorAdapter() {

    }

private:
    CascadeDetectorAdapter();

    cv::Ptr<cv::CascadeClassifier> Detector;
};


extern "C"
JNIEXPORT jlong JNICALL
Java_top_zcwfeng_opencv_FaceTracker_nativeCreateObject(JNIEnv *env, jclass clazz, jstring model_) {

    const char *model = env->GetStringUTFChars(model_, 0);


    //智能指针
    Ptr<CascadeClassifier> classifier = makePtr<CascadeClassifier>(model);
    //创建一个跟踪适配器
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(classifier);

    Ptr<CascadeClassifier> classifier1 = makePtr<CascadeClassifier>(model);
    //创建一个跟踪适配器
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(classifier1);

    //拿去用的跟踪器
    DetectionBasedTracker::Parameters DetectorParams;
    DetectionBasedTracker *tracker = new DetectionBasedTracker(mainDetector,
                                                                        trackingDetector,
                                                                        DetectorParams);

    env->ReleaseStringUTFChars(model_, model);

    return (jlong) tracker;

}

extern "C"
JNIEXPORT void JNICALL
Java_top_zcwfeng_opencv_FaceTracker_nativeDestroyObject(JNIEnv *env, jclass clazz, jlong thiz) {
    if(thiz != 0) {
        DetectionBasedTracker *tracker = reinterpret_cast<DetectionBasedTracker *>(thiz);
        tracker->stop();
        delete tracker;
    }}

extern "C"
JNIEXPORT void JNICALL
Java_top_zcwfeng_opencv_FaceTracker_nativeSetSurface(JNIEnv *env, jclass clazz, jlong thiz,
                                                     jobject surface) {
    // TODO: implement nativeSetSurface()
}

extern "C"
JNIEXPORT void JNICALL
Java_top_zcwfeng_opencv_FaceTracker_nativeStart(JNIEnv *env, jclass clazz, jlong thiz) {
    if(thiz != 0) {
        DetectionBasedTracker *tracker = reinterpret_cast<DetectionBasedTracker *>(thiz);
        tracker->run();
    }}

extern "C"
JNIEXPORT void JNICALL
Java_top_zcwfeng_opencv_FaceTracker_nativeStop(JNIEnv *env, jclass clazz, jlong thiz) {
    if(thiz != 0) {
        DetectionBasedTracker *tracker = reinterpret_cast<DetectionBasedTracker *>(thiz);
        tracker->stop();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_top_zcwfeng_opencv_FaceTracker_nativeDetect(JNIEnv *env, jclass clazz, jlong thiz,
                                                 jbyteArray input_image, jint width, jint height,
                                                 jint rotation_degrees) {
    // TODO: implement nativeDetect()
}