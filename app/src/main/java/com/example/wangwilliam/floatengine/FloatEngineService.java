package com.example.wangwilliam.floatengine;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangwilliam on 2/19/16.
 */
public class FloatEngineService extends Service {
    //定义浮动窗口布局
    private LinearLayout mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private TextView mFloatView;

    private Timer timer;

    private boolean iSFloatViewCreated = false;

    private Handler handler = new Handler();

    private HTTPDataProvider dataProvider = null;

    private static final String TAG = "FloatEngineService";

    private SharedPreferences sp = null;
    private SharedPreferences.OnSharedPreferenceChangeListener listener = null;
    private EngineSettings engineSettings = null;

    private StringBuilder sb = new StringBuilder();

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(TAG, "oncreat");
        //createFloatView();
        //Toast.makeText(FxService.this, "create FxService", Toast.LENGTH_LONG);
        if (timer == null){
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 100, 500);
        }

        sp = getSharedPreferences(EngineConstants.spFileName, MODE_PRIVATE);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                engineSettings.Load();
                if (iSFloatViewCreated) {
                    wmParams.x = engineSettings.positionX;
                    wmParams.y = engineSettings.positionY;
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                    dataProvider.SetEngineSettings(engineSettings);
                }
            }
        };
        sp.registerOnSharedPreferenceChangeListener(listener);

        engineSettings = new EngineSettings(sp);
        engineSettings.Load();

        dataProvider = new HTTPDataProvider(engineSettings);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();

        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        //TYPE_SYSTEM_OVERLAY 可以让悬浮窗口在下拉通知中，锁屏中，状态栏中显示。但是不能响应onTouch事件
        //TYPE_SYSTEM_ALERT 有些应用打开时不能显示出来，下拉通知及锁屏都不能显示出来，可以响应onTouch事件
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSPARENT;
        //wmParams.format = PixelFormat.OPAQUE;
        //wmParams.alpha = 0.7f;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        // FLAG_LAYOUT_IN_SCREEN for full screen, include the status bar.
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = engineSettings.positionX; // 430 for Vibrate & Wifi & percentage
        wmParams.y = engineSettings.positionY; // 10 for the margin from the top

        //DisplayMetrics displayMetrics = new DisplayMetrics();
        //mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        //wmParams.y = displayMetrics.heightPixels - 500;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

        Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
        Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
        Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
        Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());

        dataProvider.doWork();

        //浮动窗口文本框
        mFloatView = (TextView)mFloatLayout.findViewById(R.id.textView_floatText);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);
        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new OnTouchListener() {
            private float screenX;
            private float screenY;
            private float viewX;
            private float viewY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "======onTouch=======");
                screenX = event.getRawX(); //获得view在屏幕中的相对位置
                screenY = event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewX = event.getX(); //获得view在父窗口中的相对位置
                        viewY = event.getY();
                        Log.i(TAG, "MotionEvent.ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updateViewPostion();
                        Log.i(TAG, "MotionEvent.ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        updateViewPostion();
                        viewX = 0;
                        viewY = 0;
                        Log.i(TAG, "MotionEvent.ACTION_DOWN");
                        break;
                }

                return false; //此处必须返回false，否则事件不再传递，OnClickListener获取不到监听
            }

            private void updateViewPostion() {
                wmParams.x = (int) (screenX - viewX);
                wmParams.y = (int) (screenY - viewY);
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
            }
        });

        mFloatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "======onClick=======");
            }
        });

        iSFloatViewCreated = true;
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();

        if(mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
            iSFloatViewCreated = false;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        sp.unregisterOnSharedPreferenceChangeListener(listener);

        dataProvider.shutdown();
    }

    private void updateFloatView(){
        String currentRatio = "*" + new java.util.Date().toString().substring(10,20) + "*";

        if (dataProvider.connect(null)){
            currentRatio = dataProvider.getRatioListString();
        }

        sb.delete(0, sb.length());
        int sec = Calendar.getInstance().get(Calendar.SECOND);
        if (sec < 10) sb.append("0");
        sb.append(sec);
        sb.append(" ");
        sb.append(currentRatio);

        mFloatView.setText(sb.toString());
        dataProvider.disconnect();
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (iSFloatViewCreated){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mFloatView != null) {
                            updateFloatView();
                            //mFloatView.setText("*" + new java.util.Date().toString().substring(10,20) + "*");
                        }
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        createFloatView();
                    }
                });
            }
        }
    }
}
