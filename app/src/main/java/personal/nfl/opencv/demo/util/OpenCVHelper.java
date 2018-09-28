package personal.nfl.opencv.demo.util;

public class OpenCVHelper {

    static {
        System.loadLibrary("OpenCVHelper");
    }
    public static native int[] gray(int[] buf, int w, int h);
    public static native int[] imageToGray(String path);
}
