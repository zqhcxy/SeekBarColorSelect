package com.github.zqhcxy.seekbarcolorselect;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.zqhcxy.seekbarcolorselect.shapedrawable.HueShapeDrawable;
import com.github.zqhcxy.seekbarcolorselect.shapedrawable.MyShapeDrawable;

/**
 * seekbar的颜色渐变
 *
 * @author zqhcxy
 */
public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private SeekBar seekbar_s;
    private SeekBar seekbar_v;
    private SeekBar dial_seekbar;

    private TextView iv1;
    private ImageView show_iv;
    private Button button;

    private SeekBar.OnSeekBarChangeListener seekbarlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekbar_s = (SeekBar) findViewById(R.id.seekbar_s);
        seekbar_v = (SeekBar) findViewById(R.id.seekbar_v);
        dial_seekbar = (SeekBar) findViewById(R.id.dial_seekbar);
        iv1 = (TextView) findViewById(R.id.tv_color_bg);


        seekBar.setProgressDrawable(new HueShapeDrawable(getApplicationContext()));
        seekbar_s.setProgressDrawable(new MyShapeDrawable(this, new RectShape()));
        seekbar_v.setProgressDrawable(new MyShapeDrawable(this, new RectShape(), getVColor(Color.BLACK, 0.3f)));
        changeColor(seekBar);

        seekbarlistener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (!fromUser)
                    return;
                changeColor(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        seekBar.setMax(360);
        seekbar_s.setMax(100);
        seekbar_v.setMax(100);
        seekBar.setOnSeekBarChangeListener(seekbarlistener);
        seekbar_s.setOnSeekBarChangeListener(seekbarlistener);
        seekbar_v.setOnSeekBarChangeListener(seekbarlistener);
        show_iv = (ImageView) findViewById(R.id.show_iv);
        button = (Button) findViewById(R.id.take_bt);

    }

    /**
     * 颜色变化时要进行颜色的刷新
     *
     * @param mSeekBar
     */
    private void changeColor(SeekBar mSeekBar) {

        float[] hsv = {seekBar.getProgress(), ((float) seekbar_s.getProgress()) / 100f, ((float) seekbar_v.getProgress()) / 100f};//seekbar_v.getProgress()-100
        int color = Color.HSVToColor(hsv);

        iv1.setBackgroundColor(color);
        if (mSeekBar.getId() == R.id.seekbar) {
            float[] hsv1 = {seekBar.getProgress(), 1f, 1f};//seekbar_v.getProgress()-100
            int color1 = Color.HSVToColor(hsv1);
            ((MyShapeDrawable) seekbar_s.getProgressDrawable()).setChangeColor(color1);
            ((MyShapeDrawable) seekbar_v.getProgressDrawable()).setChangeColor(color1);
        }
    }

    /**
     * 使用GradientDrawable进行渐变
     *
     * @param color
     */
    private void createSeekbarColor(int color) {
        int[] mColor_v = new int[]{getVColor(Color.BLACK, 0.3f), color};//确定0.3f
        int[] mColor_S = new int[]{ContextCompat.getColor(this, R.color.c9), color};//c9确定
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mColor_v);
        GradientDrawable gradientDrawable_s = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mColor_S);
        Rect rect = seekbar_v.getProgressDrawable().getBounds();
        seekbar_s.setProgressDrawable(gradientDrawable_s);
        seekbar_v.setProgressDrawable(gradientDrawable);
        seekbar_s.getProgressDrawable().setBounds(rect);
        seekbar_v.getProgressDrawable().setBounds(rect);
    }

    /**
     * 获取按钮按下的颜色（改变明暗度）
     *
     * @param color 进行调整的颜色值
     * @return 返回经过0.9f明暗度调整后的颜色值
     */
    public static int getVColor(@ColorInt int color, float vf) {
        float[] v = new float[3];
        Color.colorToHSV(color, v);
        v[2] = vf;
        return Color.HSVToColor(v);
    }

    /**
     * 获取按钮无效时的颜色（改变饱和度）
     *
     * @param color 进行调整的颜色值
     * @return 返回经过0.3f饱和度调整后的颜色值
     */
    public static int getSColor(@ColorInt int color, float sf) {
        float[] s = new float[3];
        Color.colorToHSV(color, s);
        s[1] = sf;
        return Color.HSVToColor(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
