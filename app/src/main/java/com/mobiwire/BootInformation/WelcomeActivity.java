package com.mobiwire.BootInformation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;

public class WelcomeActivity extends Activity {


    private String password;
    private TextView textPassword;
    private LinearLayout layoutPinLock;
    private LinearLayout layoutTextWelcome;
    private LinearLayout layoutProgress;
    private GeometricProgressView progressView;
    private TextView textError;
    private ImageView imageError;


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Service.ACTION_RESULT_VERIFY_FILE)) {

                String resultDecrypt=intent.getStringExtra("result");
                String username=intent.getStringExtra("username");
                password=intent.getStringExtra("password");

                Log.e("information",resultDecrypt+" - "+username+" - "+password);

                if(resultDecrypt.equals("true")) {
                    layoutPinLock.setVisibility(View.VISIBLE);
                    layoutTextWelcome.setVisibility(View.VISIBLE);
                    layoutProgress.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.profile_name)).setText("Welcome \n" + username.toUpperCase());
                }else{

                    layoutPinLock.setVisibility(View.GONE);
                    layoutTextWelcome.setVisibility(View.GONE);
                    progressView.setVisibility(View.GONE);
                    textError.setText(getString(R.string.errorMessageWelcome));
                    imageError.setVisibility(View.VISIBLE);

                }



            }
        }
    };


    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.e("TAG", "Pin complete: " + pin);
            textPassword.setText(pin);
            if(pin.equals(password)){
                Intent intentToConfigurationActivity=new Intent(WelcomeActivity.this,SetConfigurationActivity.class);
                startActivity(intentToConfigurationActivity);
            }
        }

        @Override
        public void onEmpty() {
            Log.e("TAG", "Pin empty");
            textPassword.setText("");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.e("TAG", "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
            textPassword.setText(intermediatePin);
        }
    };

    PinLockView mPinLockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Service.ACTION_RESULT_VERIFY_FILE);
        registerReceiver(mReceiver, filter);

        textPassword=(TextView)findViewById(R.id.textPassword);
        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);
        layoutPinLock=(LinearLayout)findViewById(R.id.layoutLockPin);
        layoutTextWelcome=(LinearLayout)findViewById(R.id.layoutTextWelcome);
        textError=(TextView)findViewById(R.id.textError);
        imageError=(ImageView)findViewById(R.id.imageError);
        layoutProgress=(LinearLayout)findViewById(R.id.layoutProgressWelcome);
        progressView = (GeometricProgressView) findViewById(R.id.progressViewWelcome);
        progressView.setType(TYPE.TRIANGLE);
        progressView.setNumberOfAngles(4);
        progressView.setColor(Color.parseColor("#ffffff"));
        progressView.setDuration(1000);

        Intent intentStart = new Intent(WelcomeActivity.this, Service.class);
        intentStart.setAction(Service.ACTION_VERIFY_FILES);
        startService(intentStart);

        //apnSetting();
    }



    public void apnSetting(){

        Log.e("Current APN ",APNSwitcher.getCurrAPNInfo(this).toString()+"\n\n");
        for(int i=0;i<APNSwitcher.getAvailableAPNList(this).size();i++){
            Log.e("APN List :"+i,APNSwitcher.getAvailableAPNList(this).get(i).toString());
        }
        //APNSwitcher.addAPN(this,configuration.getApn());
        //APNSwitcher.setPreferAPN(this,configuration.getApn().getId(),configuration.getApn().getApn());
        Log.e("Current APN ",APNSwitcher.getCurrAPNInfo(this).toString()+"\n\n");

    }


}
