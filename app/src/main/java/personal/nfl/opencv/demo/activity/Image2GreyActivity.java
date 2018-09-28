package personal.nfl.opencv.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;

import personal.nfl.opencv.demo.R;
import personal.nfl.opencv.demo.util.OpenCVHelper;

public class Image2GreyActivity extends Activity implements View.OnClickListener {


    private double max_size = 1024;
    private int PICK_IMAGE_REQUEST = 1;
    private ImageView iv_demo;
    private Button selectImageBtn, processBtn;
    private Bitmap selectbp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2_grey);
        // OpenCV 库静态加载并初始化
        staticLoadCVLibraries();
        initView();
        setListeners();
    }

    private void staticLoadCVLibraries() {
        boolean load = OpenCVLoader.initDebug();
        if (load) {
            Log.i("CV", "Open CV Libraries loaded...");
        }
    }

    private void initView() {
        selectImageBtn = findViewById(R.id.select_btn);
        processBtn = findViewById(R.id.process_btn);
        iv_demo = findViewById(R.id.imageView);
    }

    private void setListeners() {
        selectImageBtn.setOnClickListener(this);
        processBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Log.d("image-tag", "start to decode selected image now...");
                InputStream input = getContentResolver().openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(input, null, options);
                int raw_width = options.outWidth;
                int raw_height = options.outHeight;
                int max = Math.max(raw_width, raw_height);
                int newWidth = raw_width;
                int newHeight = raw_height;
                int inSampleSize = 1;
                if (max > max_size) {
                    newWidth = raw_width / 2;
                    newHeight = raw_height / 2;
                    while ((newWidth / inSampleSize) > max_size || (newHeight / inSampleSize) > max_size) {
                        inSampleSize *= 2;
                    }
                }
                options.inSampleSize = inSampleSize;
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                selectbp = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                iv_demo.setImageBitmap(selectbp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图像..."), PICK_IMAGE_REQUEST);
    }

    private void convertGray() {
        Mat src = new Mat();
        Mat temp = new Mat();
        Mat dst = new Mat();
        if (selectbp == null) {
            selectbp = BitmapFactory.decodeResource(getResources(), R.drawable.image_opendv);
        }
        Utils.bitmapToMat(selectbp, src);
        Imgproc.cvtColor(src, temp, Imgproc.COLOR_BGRA2BGR);
        Log.i("CV", "image type:" + (temp.type() == CvType.CV_8UC3));
        Imgproc.cvtColor(temp, dst, Imgproc.COLOR_BGR2GRAY);
        Utils.matToBitmap(dst, selectbp);
        iv_demo.setImageBitmap(selectbp);
    }

    private void convertGray2() {
        int w = iv_demo.getWidth();
        int h = iv_demo.getHeight();

        int[] pix = new int[w * h];
        if (selectbp == null) {
            selectbp = BitmapFactory.decodeResource(getResources(), R.drawable.image_opendv);
        }
        selectbp.getPixels(pix, 0, w, 0, 0, w, h);
        int[] resultPixes = OpenCVHelper.gray(pix, w, h);
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        result.setPixels(resultPixes, 0, w, 0, 0, w, h);
        iv_demo.setImageBitmap(result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_btn:
                selectImage();
                break;
            case R.id.process_btn:
//                convertGray();
                convertGray2();
                break;
        }
    }
}
