# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

#CMake版本信息
cmake_minimum_required(VERSION 3.4.1)
#支持-std=gnu++11
set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

# find_path(rootPath )
# 暂时没有找到获取绝对路径的方法，所以更改工作环境时，这里都要设置一次项目的绝对路径
set(rootPath D:\\Android\\workplace\\MyOpenCVDemo)
#工程路径
set(pathToProject ${rootPath})
#OpenCV-android-sdk路径
set(pathToOpenCv ${rootPath}/native)

#配置加载 native 依赖
include_directories(${pathToOpenCv}/jni/include)

# 动态方式加载
add_library(opencv_java3 STATIC IMPORTED)

# 设置库路径
set_target_properties(opencv_java3
        PROPERTIES
        IMPORTED_LOCATION
        ${pathToProject}/opencvlibrary/libs/${ANDROID_ABI}/libopencv_java3.so)

add_library(
        mixed_sample

        SHARED

        src/main/cpp/jni_part.cpp)

target_link_libraries( # Specifies the target library.
        mixed_sample

        opencv_java3
        ${log-lib})

# OpenCVHelper
add_library(
        OpenCVHelper

        SHARED

        src/main/cpp/opencv_helper.cpp)

target_link_libraries( # Specifies the target library.
        OpenCVHelper

        opencv_java3
        ${log-lib})

# 人脸检测
add_library(
        detection_based_tracker

        SHARED

        src/main/cpp/DetectionBasedTracker_jni.cpp)

target_link_libraries( # Specifies the target library.
        detection_based_tracker

        opencv_java3
        ${log-lib})

