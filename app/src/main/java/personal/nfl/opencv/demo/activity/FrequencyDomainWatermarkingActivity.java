package personal.nfl.opencv.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import personal.nfl.opencv.demo.R;
import personal.nfl.opencv.demo.util.FrequencyDomainWatermarkingUtil;

public class FrequencyDomainWatermarkingActivity extends Activity {

    private Context context;
    private ImageView iv_source, iv_result;
    private EditText et_water_marking_string;
    private Button bn_add_water_marking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_domain_watermarking);
        this.context = this;
        initView();
        setListeners();
    }

    private void initView() {
        iv_source = findViewById(R.id.iv_source);
        iv_result = findViewById(R.id.iv_result);
        et_water_marking_string = findViewById(R.id.et_water_marking_string);
        bn_add_water_marking = findViewById(R.id.bn_add_water_marking);
    }

    private void setListeners() {
        bn_add_water_marking.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bn_add_water_marking:
                    Mat resourceMat = new Mat();
                    Utils.bitmapToMat(drawableToBitmap(iv_source.getDrawable()), resourceMat);
//                    FrequencyDomainWatermarkingUtil.getInstance().transformImageWithText();
                    break;
            }
        }
    };

    private static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
