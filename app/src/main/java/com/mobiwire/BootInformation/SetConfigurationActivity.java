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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bohush.geometricprogressview.GeometricProgressView;
import net.bohush.geometricprogressview.TYPE;


public class SetConfigurationActivity extends Activity {

    private Configuration configuration;
    private GeometricProgressView progressView;
    private TextView textStatus;
    private LinearLayout layoutProgress;
    private Button buttonStartFlash;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Service.ACTION_SEND_STAUTUS_INFORMATION)) {

                String status=intent.getStringExtra("status");
                textStatus.setText(status.substring(3,status.length()));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_set_configuration);

        initUI();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Service.ACTION_SEND_STAUTUS_INFORMATION);
        registerReceiver(mReceiver, filter);

        configuration= (Configuration) getIntent().getSerializableExtra("conf");
        //Log.e("Current APN ",APNSwitcher.getCurrAPNInfo(this).toString()+"\n\n");
        /*for(int i=0;i<APNSwitcher.getAvailableAPNList(this).size();i++){
            Log.e("APN List :"+i,APNSwitcher.getAvailableAPNList(this).get(i).toString());
        }*/
    }

    public void clickStartFlash(View v) {

        buttonStartFlash.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.VISIBLE);
        Intent intentStart = new Intent(SetConfigurationActivity.this, Service.class);
        intentStart.setAction(Service.ACTION_START);
        intentStart.putExtra("conf",configuration);
        startService(intentStart);
    }


    public void initUI(){

        layoutProgress = (LinearLayout)findViewById(R.id.layoutProgress);
        buttonStartFlash=(Button)findViewById(R.id.buttonStartFlash);
        textStatus = (TextView)findViewById(R.id.textStatus);
        progressView = (GeometricProgressView) findViewById(R.id.progressView);
        progressView.setType(TYPE.KITE);
        progressView.setNumberOfAngles(20);
        progressView.setColor(Color.parseColor("#ffffff"));
        progressView.setDuration(1000);
    }



}
