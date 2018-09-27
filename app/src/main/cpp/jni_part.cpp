#include <jni.h>

#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>

//#include "../head/opencv2/core/mat.hpp"
//#include "../head/opencv2/imgproc.hpp"
//#include "../head/opencv2/features2d.hpp"

#include <vector>

using namespace std;
using namespace cv;

extern "C" {
JNIEXPORT void JNICALL
Java_personal_nfl_opencv_demo_activity_MixedProcessingActivity_FindFeatures(JNIEnv *, jobject,
                                                                            jlong addrGray,
                                                                            jlong addrRgba);

JNIEXPORT void JNICALL
Java_personal_nfl_opencv_demo_activity_MixedProcessingActivity_FindFeatures(JNIEnv *, jobject,
                                                                            jlong addrGray,
                                                                            jlong addrRgba) {
    Mat &mGr = *(Mat *) addrGray;
    Mat &mRgb = *(Mat *) addrRgba;
    vector<KeyPoint> v;

    Ptr <FeatureDetector> detector = FastFeatureDetector::create(50);
    detector->detect(mGr, v);
    for (unsigned int i = 0; i < v.size(); i++) {
        const KeyPoint &kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 10, Scalar(255, 0, 0, 255));
    }
}
}