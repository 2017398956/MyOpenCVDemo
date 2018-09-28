#include "personal_nfl_opencv_demo_util_OpenCVHelper.h"

#include <string.h>

#include <opencv2/core.hpp>
#include <opencv2/core/hal/interface.h>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>

//#include <Android/log.h>
//#include <Android/asset_manager.h>
//#include <Android/asset_manager_jni.h>

//#define TAG "cn.linjk.ihouse.jni"
//#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
//#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
//#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
//#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
//#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

#include <vector>

using namespace std;
using namespace cv;

extern "C" {

JNIEXPORT jintArray JNICALL Java_personal_nfl_opencv_demo_util_OpenCVHelper_imageToGray
        (JNIEnv *, jclass, jstring);

JNIEXPORT jintArray JNICALL Java_personal_nfl_opencv_demo_util_OpenCVHelper_gray(
        JNIEnv *env, jclass obj, jintArray buf, int w, int h) {

    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf, JNI_FALSE);//将JNI数组转换为基本类型数组
    if (cbuf == NULL) {
        return 0;
    }

    Mat imgData(h, w, CV_8UC4, (unsigned char *) cbuf);

    uchar *ptr = imgData.ptr(0);
    for (int i = 0; i < w * h; i++) {
        //计算公式：Y(亮度) = 0.299*R + 0.587*G + 0.114*B
        //对于一个int四字节，其彩色值存储方式为：BGRA
        int grayScale = (int) (ptr[4 * i + 2] * 0.299 + ptr[4 * i + 1] * 0.587 +
                               ptr[4 * i + 0] * 0.114);
        ptr[4 * i + 1] = grayScale;
        ptr[4 * i + 2] = grayScale;
        ptr[4 * i + 0] = grayScale;
    }

    int size = w * h;
    jintArray result = env->NewIntArray(size);//生成新的数组
    env->SetIntArrayRegion(result, 0, size, cbuf);//为result赋值
    env->ReleaseIntArrayElements(buf, cbuf, 0);//释放内存
    return result;
}

}
