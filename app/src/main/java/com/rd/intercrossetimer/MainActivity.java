package com.rd.intercrossetimer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isRunning, isFinished, fiveSec, popupAct, vibration, volume  = false;

    private TextView smlTimeVw, timeVw;
    private View playIcn, timeOutIcn, twoMinIcn, fiveMinIcn;

    private CountDownTimer timer = null;

    private Button closePopupBtn;
    private PopupWindow popupWindow;
    private RelativeLayout linearLayout1;

    private long mLastClickTimeCTime, mLastClickTimeVTime, mLastClickTimeVibr, mLastClickTimeSwt, mLastClickTimeTTime = 0;

    private int hlColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        linearLayout1 = findViewById(R.id.layout_main);
        hlColor = R.color.activated;
        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                hlColor = R.color.activatedDark;
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                //doStuff();
                break;
        }


        smlTimeVw = findViewById(R.id.timer_smalltextview);
        timeVw = findViewById(R.id.timer_textview);
        timeVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTimeVTime < 200)return;
                mLastClickTimeVTime = SystemClock.elapsedRealtime();
                if(!isRunning){
                    if(isFinished){
                        restartTimer();
                    }else{
                        startTimer();
                    }
                }else{
                    stopTimer();
                }
            }
        });
        smlTimeVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTimeVTime < 200)return;
                mLastClickTimeVTime = SystemClock.elapsedRealtime();
                if(!isRunning){
                    if(isFinished){
                        restartTimer();
                    }else{
                        startTimer();
                    }
                }else{
                    stopTimer();
                }
            }
        });


        playIcn = findViewById(R.id.viewPlay);
        playIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTimeVTime < 200)return;
                mLastClickTimeVTime = SystemClock.elapsedRealtime();
                if(!isRunning){
                    if(isFinished) resetTimer();
                    startTimer();
                }else{
                    stopTimer();
                    resetTimer();
                }
            }
        });

        timeOutIcn = findViewById(R.id.viewTimeout);
        timeOutIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTimeTTime < 1000)return;
                mLastClickTimeTTime = SystemClock.elapsedRealtime();
                timeout();
            }
        });
        twoMinIcn = findViewById(R.id.viewTwoMin);
        twoMinIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTimeTTime < 1000)return;
                mLastClickTimeTTime = SystemClock.elapsedRealtime();
                twoMinBrake();
            }
        });
        fiveMinIcn = findViewById(R.id.viewFiveMin);
        fiveMinIcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTimeTTime < 1000)return;
                mLastClickTimeTTime = SystemClock.elapsedRealtime();
                fiveMinBrake();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement

        if (id == R.id.action_item_volume) {
            if (SystemClock.elapsedRealtime() - mLastClickTimeSwt < 3000)return true;
            mLastClickTimeSwt = SystemClock.elapsedRealtime();
            if(volume){
                volume = false;
                Toast.makeText(getApplicationContext(),"Schalter ausgeschaltet", Toast.LENGTH_LONG).show();
                findViewById(R.id.action_item_volume).setBackground(null);
            }else{
                volume = true;
                Toast.makeText(getApplicationContext(),"Schalter eingeschaltet", Toast.LENGTH_LONG).show();
                findViewById(R.id.action_item_volume).setBackgroundColor(hlColor);
            }
            return true;
        }
        if (id == R.id.action_item_vibration) {
            if (SystemClock.elapsedRealtime() - mLastClickTimeVibr < 3000)return true;
            mLastClickTimeVibr = SystemClock.elapsedRealtime();
            if(vibration){
                vibration = false;
                Toast.makeText(getApplicationContext(),"Vibration ausgeschaltet", Toast.LENGTH_LONG).show();
                findViewById(R.id.action_item_vibration).setBackground(null);
            }else{
                vibration = true;
                Toast.makeText(getApplicationContext(),"Vibration eingeschaltet", Toast.LENGTH_LONG).show();
                findViewById(R.id.action_item_vibration).setBackgroundColor(hlColor);
            }
            return true;
        }

        if (id == R.id.action_item_time) {
            if (SystemClock.elapsedRealtime() - mLastClickTimeCTime < 1000)return true;
            mLastClickTimeCTime = SystemClock.elapsedRealtime();
            if(fiveSec){
                fiveSec = false;
                setTitle(R.string.titel_30sec);
                timeVw.setText("30");
            }else{
                fiveSec = true;
                setTitle(R.string.titel_5sec);
                timeVw.setText("5");
            }
            smlTimeVw.setText(".0");
            isFinished=false;
            return true;
        }
        if (id == R.id.action_item_one) {
             if(!popupAct) {
                 popupAct = true;
                 LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 View customView = layoutInflater.inflate(R.layout.popup_help, null);

                 closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);

                 popupWindow = new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                 popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

                 closePopupBtn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         popupWindow.dismiss();
                         popupAct = false;
                     }
                 });
             }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(volume) {
            int action = event.getAction();
            int keyCode = event.getKeyCode();
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (action == KeyEvent.ACTION_DOWN) {
                        if(!isRunning){
                            if(isFinished){
                                restartTimer();
                            }else{
                                startTimer();
                            }
                        }else{
                            stopTimer();
                        }
                    }
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (action == KeyEvent.ACTION_DOWN) {
                        restartTimer();
                    }
                    return true;
                default:
                    return super.dispatchKeyEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
    }



    public void startTimer(){
        if(isRunning||isFinished) return;
        isRunning=true;
        int sec = Integer.valueOf(String.valueOf(timeVw.getText())) ;
        if((String.valueOf(smlTimeVw.getText())).charAt(0)==':') {
            sec = ((Integer.valueOf(String.valueOf(timeVw.getText())) * 60) + (Integer.valueOf(String.valueOf(smlTimeVw.getText()).replace(":",""))));
        }
        sec = sec * 1000;
        playIcn.setBackgroundResource(R.drawable.ic_baseline_stop_24);
        timer = new CountDownTimer(sec,10){
            public void onTick(long millisUntilFinished){
                if(millisUntilFinished > 60000) {
                    timeVw.setText(String.valueOf(millisUntilFinished / 60000));
                    smlTimeVw.setText(":" + String.valueOf((millisUntilFinished / 1000) % 60));
                } else{
                    timeVw.setText(String.valueOf(millisUntilFinished / 1000));
                    smlTimeVw.setText("."+String.valueOf((millisUntilFinished % 1000)/100 ));
                }
                final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if((vibration&&millisUntilFinished<5100&&((millisUntilFinished % 1000)/100)==0))vibrator.vibrate(100);
            }
            @SuppressLint("ResourceAsColor")
            public void onFinish(){
                timeVw.setText("0");
                smlTimeVw.setText(".0");
                final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(vibration)vibrator.vibrate(800);
                playIcn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                isRunning=false;
                isFinished=true;
            }

        };
        timer.start();
    }


    public void stopTimer(){
        if(isRunning){
            timer.cancel();
            isRunning=false;
            playIcn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    public void resetTimer(){
        stopTimer();
        String sec = "30";
        if(fiveSec) sec ="5";
        timeVw.setText(sec);
        smlTimeVw.setText(".0");
        isFinished=false;
    }

    public void restartTimer(){
        resetTimer();
        startTimer();
    }

    public void timeout(){
        stopTimer();
        startTimerAt(60);
    }

    public void twoMinBrake(){
        stopTimer();
        startTimerAt(120);
    }
    public void fiveMinBrake(){
        stopTimer();
        startTimerAt(300);
    }

    public void startTimerAt(int sec){
        if(isRunning) return;
        isRunning=true;
        sec = sec *1000;
        playIcn.setBackgroundResource(R.drawable.ic_baseline_stop_24);
        timer = new CountDownTimer(sec,10){
            public void onTick(long millisUntilFinished){
                if(millisUntilFinished > 60000) {
                    timeVw.setText(String.valueOf(millisUntilFinished / 60000));
                    smlTimeVw.setText(":" + String.valueOf((millisUntilFinished / 1000) % 60));
                } else{
                    timeVw.setText(String.valueOf(millisUntilFinished / 1000));
                    smlTimeVw.setText("."+String.valueOf((millisUntilFinished % 1000)/100 ));
                }
                final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if((vibration&&millisUntilFinished<5100&&((millisUntilFinished % 1000)/100)==0))vibrator.vibrate(100);
            }
            @SuppressLint("ResourceAsColor")
            public void onFinish(){
                timeVw.setText("0");
                smlTimeVw.setText(".0");
                final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(vibration)vibrator.vibrate(800);
                playIcn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                isRunning=false;
                isFinished=true;
            }

        };
        timer.start();
    }
}