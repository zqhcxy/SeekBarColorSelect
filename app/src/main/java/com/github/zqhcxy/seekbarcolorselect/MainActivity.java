package com.github.zqhcxy.seekbarcolorselect;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private SeekBar seekbar_s;
    private SeekBar seekbar_v;
    private SeekBar dial_seekbar;

    private TextView iv1;
    private ImageView show_iv;
    private Button button;
    Utils utils;

    private SeekBar.OnSeekBarChangeListener seekbarlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekbar_s = (SeekBar) findViewById(R.id.seekbar_s);
        seekbar_v = (SeekBar) findViewById(R.id.seekbar_v);
        dial_seekbar=(SeekBar)findViewById(R.id.dial_seekbar);
        iv1 = (TextView) findViewById(R.id.tv_color_bg);

//        seekBar.setProgressBackgroundColor(Color.TRANSPARENT);
        int[] mColor_h = new int[]{ContextCompat.getColor(this,R.color.cr_hue_1),ContextCompat.getColor(this,R.color.cr_hue_2),
                ContextCompat.getColor(this,R.color.cr_hue_3),ContextCompat.getColor(this,R.color.cr_hue_4),
                ContextCompat.getColor(this,R.color.cr_hue_5),ContextCompat.getColor(this,R.color.cr_hue_6),ContextCompat.getColor(this,R.color.cr_hue_7)};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mColor_h);
        seekBar.setProgressDrawable(gradientDrawable);
        changeColor(seekBar);
                /**
                 * v21以上setProgressDrawable无效，但是setBackgroundDrawable有效，
                 * v21以下setProgressDrawable有效，但是setBackgroundDrawable无效。
                 */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//api大于21就执行
//            seekbar_s.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.progress_fill_2));
//            seekbar_s.setProgressDrawableTiled(ContextCompat.getDrawable(this, R.drawable.hue_s));

//            Rect bounds = seekbar_s.getProgressDrawable().getBounds();
//            seekbar_s.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.hue));
//            seekbar_s.getProgressDrawable().setBounds(bounds);
//            seekbar_s.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.hue_s));
//            seekBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.hue));
//        } else {
//            seekbar_s.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.hue_s3));
//            seekBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.hue));
//        }

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(utils==null){
//                    utils=new Utils();
//                }
//                utils.tackPic();
            }
        });

        seekbarlistener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if(!fromUser)
                    return;
//                switch (seekBar.getId()) {
//                    case R.id.seekbar:
//                        break;
//                }
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    private void changeColor(SeekBar mSeekBar) {

        float[] hsv = {seekBar.getProgress(), ((float) seekbar_s.getProgress()) / 100f, ((float) seekbar_v.getProgress()) / 100f};//seekbar_v.getProgress()-100
        int color = Color.HSVToColor(hsv);

        iv1.setBackgroundColor(color);
        if (mSeekBar.getId() == R.id.seekbar) {
            float[] hsv1 = {seekBar.getProgress(), 1f, 1f};//seekbar_v.getProgress()-100
            int color1 = Color.HSVToColor(hsv1);
            createSeekbarColor(color1);
        }
    }

    private void createSeekbarColor(int color) {
        int[] mColor_v = new int[]{getVColor(Color.BLACK,0.3f),color};//确定0.3f
        int[] mColor_S = new int[]{ContextCompat.getColor(this,R.color.c9), color};//c9确定
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mColor_v);
        GradientDrawable gradientDrawable_s = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mColor_S);
        Rect rect=seekbar_s.getProgressDrawable().getBounds();
        seekbar_s.setProgressDrawable((Drawable) gradientDrawable_s);
        seekbar_v.setProgressDrawable((Drawable) gradientDrawable);
        seekbar_s.getProgressDrawable().setBounds(rect);
        seekbar_v.getProgressDrawable().setBounds(rect);
    }
    public final static int[][] mPressedEnableStates = new int[][]{
            new int[]{android.R.attr.state_pressed},
        new int[]{android.R.attr.state_enabled},
            new int[]{-android.R.attr.state_enabled}};
    public final static int[][] mTineStates = new int[][]{
            new int[]{android.R.attr.startColor},

            new int[]{-android.R.attr.endColor}};

//    private void initSeekbar(SeekBarCompat s_seekbar, SeekBarCompat v_seekbar, int progress) {
//        float[] hsv = {progress, 1, 1};//seekbar_v.getProgress()-100
//        int color = Color.HSVToColor(hsv);
//
//        int[] colors = new int[3];
//        colors[0] = color;
//        colors[1] = color;
//        colors[1] = color;

        //代码实现的shape
//        int[] mColor=new int[]{R.color.white,color};
//        GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,mColor);
//        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//        gradientDrawable.setCornerRadius(2);
//        gradientDrawable.setStroke(10,-1);
//        s_seekbar.setProgressDrawableTiled(gradientDrawable);
//        v_seekbar.setProgressDrawableTiled(gradientDrawable);
//    }


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
        if (utils != null) {
            utils.destroy();
        }
        super.onDestroy();
    }
}
