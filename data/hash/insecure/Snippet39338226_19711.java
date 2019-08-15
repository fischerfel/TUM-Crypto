import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Vector;

public class Launcher extends Service implements View.OnClickListener, View.OnTouchListener{
WindowManager windowManager;
WindowManager.LayoutParams layoutparams;
RelativeLayout relativeLayout;
TextView text;
String password;
String password_saved;
String hint;
Vector<Long> hold_time;
long down_time[];
Vector<Vector<Float>> pressure;
Vector pressure_num[];
Vector<Vector<Float>> size;
Vector size_num[];
Button nums[]=null;
ImageButton clear;
ImageButton ok;
Vector<Vector<Float>> vecs;
Vector<Float> H_2;

@Override
public IBinder onBind(Intent intent){
    return null;
}

@Override
public void onCreate(){
    super.onCreate();
    Log.d("Launcher","onCreate()");

    KeyguardManager.KeyguardLock keyguardLock;
    KeyguardManager keyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
    keyguardLock=keyguardManager.newKeyguardLock("");
    keyguardLock.disableKeyguard();

    IntentFilter intentFilter=new IntentFilter("android.intent.action.SCREEN_OFF");
    intentFilter.addAction("android.intent.action.SCREEN_ON");
    registerReceiver(screenReceiver, intentFilter);
    setUpLayout();
}

@Override
public int onStartCommand(Intent intent,int flags,int startId){
    return Service.START_STICKY;
}

@Override
public void onDestroy(){
    unregisterReceiver(screenReceiver);
    Log.d("Service","Service stop!!!");
 //        startActivity(new Intent(Launcher.this,Launcher.class));
 //        KeyguardManager.KeyguardLock keyguardLock;
 //        KeyguardManager keyguardManager=(KeyguardManager)getSystemService(KEYGUARD_SERVICE);
 //        keyguardLock=keyguardManager.newKeyguardLock("");
 //        keyguardLock.reenableKeyguard();
     super.onDestroy();

}

private BroadcastReceiver screenReceiver=new BroadcastReceiver() {
    public boolean wasScreenOn=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        Log.d("Launcher","action is "+action);

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            wasScreenOn=false;
            launchLock();
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            wasScreenOn=true;
        }else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            launchLock();
        }
    }
};

public int dpToPx(int dp) {
    DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
}

public int spTpPx(int sp) {
    DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
    return Math.round(sp * displayMetrics.scaledDensity);
}

private void setUpLayout(){


    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    layoutparams= new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.RGBA_8888
    );

    nums=new Button[10];
    relativeLayout = new RelativeLayout(this);
    relativeLayout.setBackgroundResource(R.drawable.back);
    int marginPix=dpToPx(-16);
    int paddingPix=dpToPx(20);
    RelativeLayout.LayoutParams params[]=new RelativeLayout.LayoutParams[10];

    for (int i=0;i<10;i++){
        params[i]=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params[i].bottomMargin=marginPix;
        nums[i]=new Button(this);
        nums[i].setId(i);
        nums[i].setPadding(paddingPix, paddingPix, paddingPix, paddingPix);
        nums[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        nums[i].setTypeface(null, Typeface.NORMAL);
        nums[i].setTextColor(Color.parseColor("#a0333333"));
        nums[i].setBackgroundColor(Color.parseColor("#00000000"));
        nums[i].setOnClickListener(this);
        nums[i].setOnTouchListener(this);
    }
    params[2].addRule(RelativeLayout.CENTER_IN_PARENT);
    nums[2].setText(R.string.num_2);
    params[5].addRule(RelativeLayout.CENTER_HORIZONTAL);
    params[5].addRule(RelativeLayout.BELOW, 2);
    nums[5].setText(R.string.num_5);
    params[1].addRule(RelativeLayout.LEFT_OF, 2);
    params[1].addRule(RelativeLayout.ABOVE,5);
    nums[1].setText(R.string.num_1);
    params[3].addRule(RelativeLayout.RIGHT_OF, 2);
    params[3].addRule(RelativeLayout.ABOVE, 5);
    nums[3].setText(R.string.num_3);
    params[4].addRule(RelativeLayout.LEFT_OF, 2);
    params[4].addRule(RelativeLayout.BELOW,2);
    nums[4].setText(R.string.num_4);
    params[6].addRule(RelativeLayout.RIGHT_OF, 2);
    params[6].addRule(RelativeLayout.BELOW, 2);
    nums[6].setText(R.string.num_6);
    params[7].addRule(RelativeLayout.LEFT_OF, 5);
    params[7].addRule(RelativeLayout.BELOW,5);
    nums[7].setText(R.string.num_7);
    params[8].addRule(RelativeLayout.BELOW, 5);
    params[8].addRule(RelativeLayout.RIGHT_OF,4);
    nums[8].setText(R.string.num_8);
    params[9].addRule(RelativeLayout.BELOW, 5);
    params[9].addRule(RelativeLayout.RIGHT_OF,5);
    nums[9].setText(R.string.num_9);
    params[0].addRule(RelativeLayout.BELOW, 8);
    params[0].addRule(RelativeLayout.RIGHT_OF,7);
    nums[0].setText(R.string.num_0);

    RelativeLayout.LayoutParams clear_param=new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    clear_param.addRule(RelativeLayout.BELOW,8);
    clear_param.addRule(RelativeLayout.LEFT_OF, 8);
    clear_param.setMargins(0, dpToPx(6), dpToPx(12), 0);
    clear=new ImageButton(this);
    clear.setId(R.id.clear);
    clear.setImageResource(R.drawable.ic_action_cancel);
    clear.setBackgroundColor(Color.parseColor("#00000000"));
    clear.setPadding(paddingPix, paddingPix, paddingPix, paddingPix);
    clear.setLayoutParams(clear_param);
    clear.setOnClickListener(this);

    RelativeLayout.LayoutParams ok_param=new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    ok_param.addRule(RelativeLayout.BELOW,8);
    ok_param.addRule(RelativeLayout.RIGHT_OF, 8);
    ok_param.setMargins(dpToPx(12), dpToPx(6), 0, 0);
    ok=new ImageButton(this);
    ok.setId(R.id.ok);
    ok.setImageResource(R.drawable.ic_action_accept);
    ok.setBackgroundColor(Color.parseColor("#00000000"));
    ok.setPadding(paddingPix, paddingPix, paddingPix, paddingPix);
    ok.setLayoutParams(ok_param);
    ok.setOnClickListener(this);

    RelativeLayout.LayoutParams text_param=new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    text_param.addRule(RelativeLayout.CENTER_HORIZONTAL);
    text_param.addRule(RelativeLayout.ABOVE,2);
    text=new TextView(this);
    text.setId(R.id.text);
    text.setLayoutParams(text_param);
    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
    text.setTypeface(null, Typeface.NORMAL);
    text.setTextColor(Color.parseColor("#a0333333"));
    text.setBackgroundColor(Color.parseColor("#00000000"));
    text.setPadding(dpToPx(30),dpToPx(30),dpToPx(30),dpToPx(30));

    for (int i=0;i<10;i++){
        nums[i].setLayoutParams(params[i]);
    }
    for (int i=0;i<10;i++){
        relativeLayout.addView(nums[i]);
    }
    relativeLayout.addView(clear);
    relativeLayout.addView(ok);
    relativeLayout.addView(text);
}

private void launchLock(){
    try {
        //load password and vecs from file
        FileInputStream fileInputStream=openFileInput("password");
        ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);

        password_saved=(String)objectInputStream.readObject();
        vecs=(Vector<Vector<Float>>)objectInputStream.readObject();
        H_2=(Vector<Float>)objectInputStream.readObject();

        Log.d("password_saved",password_saved);
        Log.d("vecs",vecs.toString());
        Log.d("H_2",H_2.toString());

        objectInputStream.close();
        fileInputStream.close();
    } catch (FileNotFoundException e) {
        Log.d("window manager", "file not found");
        try{
            windowManager.removeView(relativeLayout);
        }catch (IllegalArgumentException e1){
            Log.d("window manager","has no layout");
        }
        return;
    } catch (StreamCorruptedException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }

    password="";
    hint="";
    hold_time=new Vector<Long>();
    down_time=new long[10];
    pressure=new Vector<Vector<Float>>();
    pressure_num=new Vector[10];
    size=new Vector<Vector<Float>>();
    size_num=new Vector[10];

    text.setText(hint);

    for (int i=0;i<10;i++){
        pressure_num[i]=new Vector<Float>();
        size_num[i]=new Vector<Float>();
    }

    try {
        windowManager.addView(relativeLayout,layoutparams);
    }catch (RuntimeException e){
//            e.printStackTrace();
        Log.d("window manager","layout has already been added");
    }
}

@Override
public void onClick(View view) {
    int id=view.getId();
    if (id==R.id.ok){
        Log.d("launcher","ok");
        Log.d("password",password);
        Log.d("hold time",hold_time.toString());
        Log.d("pressure",pressure.toString());
        Log.d("size",size.toString());
        try {
            //turn password to md5
            byte[] bytes = password.getBytes("UTF-8");
            MessageDigest md=MessageDigest.getInstance("MD5");
            bytes=md.digest(bytes);
            password=new String(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Vector<Float> vec=new Vector<Float>();
        for (long value:hold_time){
            vec.add((float) value);
        }
        for (Vector<Float> values:pressure){
            vec.add(Collections.max(values));
        }
        for (Vector<Float> values:size){
            vec.add(Collections.max(values));
        }
        Log.d("vec",vec.toString());
        Log.d("MD5",password);
        boolean quit=true;
        //if password is equal
        Vector<Float> probs=new Vector<Float>();
        if (password.equals(password_saved)){
            for (int i=0;i<H_2.size();i++){
                float p=0;
                for (int j=0;j<vecs.size();j++){
                    //np.exp(-1./2.*(v_test[i]-v_train[i])**2/H_2[i])
                    p+=Math.exp(-1./2.*
                            (vec.elementAt(i)-vecs.elementAt(j).elementAt(i))*
                            (vec.elementAt(i)-vecs.elementAt(j).elementAt(i))/
                            H_2.elementAt(i)
                    );
                }
                p/=vecs.size();
                probs.add(p*p);
            }
            Log.d("probs ",probs.toString());
            Float sum_probs= new Float(0);
            for (int i=0;i<probs.size()/3*2;i++){
                sum_probs+=probs.elementAt(i);
            }
            Log.d("sum_probs ",sum_probs.toString());
            if (sum_probs<0.25*probs.size()/3*2)
                quit=false;

        }else{
            quit=false;
        }
        if (quit)
            windowManager.removeView(relativeLayout);
        else{
            //if fail, then init
            text.setText("Wrong password");
            hint="";
            password="";
            hold_time=new Vector<Long>();
            pressure=new Vector<Vector<Float>>();
            size=new Vector<Vector<Float>>();

        }
    }else if (id==R.id.clear){
        text.setText("Please enter again");
        hint="";
        password="";
        hold_time=new Vector<Long>();
        pressure=new Vector<Vector<Float>>();
        size=new Vector<Vector<Float>>();
    }else {
        text.setText(hint+="*");
        password+=Integer.toString(id);
        Log.d("password",password);
    }
}

@Override
public boolean onTouch(View view, MotionEvent motionEvent) {
    int id=view.getId();
    if(motionEvent.getActionMasked()==MotionEvent.ACTION_DOWN){
        down_time[id]= SystemClock.uptimeMillis();
        pressure_num[id]=new Vector<Float>();
        size_num[id]=new Vector<Float>();
    }else if(motionEvent.getActionMasked()==MotionEvent.ACTION_UP){
        hold_time.add(SystemClock.uptimeMillis()-down_time[id]);
        pressure.add((Vector<Float>)pressure_num[id]);
        size.add((Vector<Float>)size_num[id]);
    }
    pressure_num[id].add(motionEvent.getPressure());
    size_num[id].add(motionEvent.getSize());
    return false;
}
}
