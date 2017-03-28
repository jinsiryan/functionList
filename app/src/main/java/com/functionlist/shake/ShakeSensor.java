package com.functionlist.shake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by imo on 2016/9/28.
 */
public class ShakeSensor implements SensorEventListener {
    public ShakeSensor(){}
    private static final String TAG = ShakeSensor.class.getSimpleName();
    private static final int SPEED_SHRESHOLD = 4800;
    public static float fShakeMinXDetection = 11.0f;
    public static float fShakeMinYDetection = 10.0f;
    public static float fShakeMinZDetection = 19.0f;
    private Context mContext;
    private SensorManager mSensormanager;
    private Sensor mSensor;//加速度传感器
    private long last_time;//
    private float last_x;//记录的最后的一次的值
    private float last_y;//记录的最后的一次的值
    private float last_z;//记录的最后的一次的值

    private OnShakeListenr mOnShakeListenr;
    public ShakeSensor(Context context){
        mContext = context;
    }
    public void init(){
        mSensormanager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //注册传感器
        mSensormanager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME);
    }
    public void unRegisterListener(){
        mSensormanager.unregisterListener(this,mSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long currentTime = System.currentTimeMillis();

        if(currentTime -last_time > 10){
            //两次摇动的时间间隔
            long timeDistance = currentTime - last_time;
            last_time = currentTime;
            //当前
            float x = sensorEvent.values[0];//x轴变化的值
            float y = sensorEvent.values[1];//y轴变化的值
            float z = sensorEvent.values[2];//z轴变化的值
            Log.e(TAG,x +"--" + y +"--"+z);
            //速度的阈值
            double speed;

            double absValue = Math.abs(x+y+z-(last_x+last_y+last_y));
            speed= absValue/timeDistance*1000;
            if(speed > SPEED_SHRESHOLD){
                //当x/y/z达到一定的值进行后续操作
                if(mOnShakeListenr != null){
                    mOnShakeListenr.onShake();
                }
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public interface OnShakeListenr{
        void onShake();
    }

    public void setOnShakeListenr(OnShakeListenr mOnShakeListenr) {
        this.mOnShakeListenr = mOnShakeListenr;
    }
}
