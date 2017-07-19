package com.mobiwire.BootInformation;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Aly on 11/07/2017.
 */

public class Service extends android.app.Service {

    private Configuration configuration;
    private String TAG="Service";

    public static final String ACTION_VERIFY_FILES= "com.mobiwire.VERIFY_FILES";
    public static final String ACTION_START = "com.mobiwire.START_CHANGE";
    public static final String ACTION_SEND_STAUTUS_INFORMATION = "com.mobiwire.SEND_STATUS";
    public static final String ACTION_RESULT_VERIFY_FILE = "com.mobiwire.RESULT_VERIFY_FILE";

    public static volatile ScheduledExecutorService sPool = Executors.newScheduledThreadPool(2);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Service start");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_START)) {
                Log.e("service","ACTION_START");
                sPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setConfiguration(configuration);
                            //apnSetting();
                        } catch (Exception e) {
                            Log.e(TAG, "Exception: Discover", e);
                        }
                    }
                });
            }

            if (intent.getAction().equals(ACTION_VERIFY_FILES)) {
                Log.e("service","service");
                sPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            decrypt();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }
        return START_STICKY;
    }



    public void setConfiguration(Configuration configuration){

        sendStatus(ACTION_SEND_STAUTUS_INFORMATION,"-- Change screen background");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        changeScreen();

        String language_lg=configuration.getLanguage().substring(0,2);
        String language_ctr=configuration.getLanguage().substring(3,5);
        String timeZone=configuration.getDefaultTimeZone();
        Log.e("language",language_lg+"-"+language_ctr);

        String[] commands =new String[]{
                //"-- mount System"
                //,"mount -o remount,rw /system" // mount system

                "-- Change boot animation"
                ,"cd /sdcard/Download/Configuration"            // change boot annimation
                ,"rm /system/media/bootanimation.zip"
                ,"cp bootanimation.zip /system/media"
                ,"chmod 644 /system/media/bootanimation.zip"


                ,"-- Change shut animation"
                ,"cd /sdcard/Download/Configuration"            // change boot annimation
                ,"rm /system/media/shutanimation.zip"
                ,"cp shutanimation.zip /system/media"
                ,"chmod 644 /system/media/shutanimation.zip"


                ,"-- Change laguage "+configuration.getLanguage()
                ,"setprop persist.sys.language "+language_lg    // language
                ,"setprop persist.sys.country "+language_ctr


                ,"-- Change time zone "+configuration.getDefaultTimeZone()
                ,"settings put global auto_time 0"              // time zone
                ,"settings put global auto_time_zone 0"
                ,"setprop persist.sys.timezone "+timeZone


                ,"cd /system/app"

                ,"-- remove Calendar application","rm Calendar.apk"
                ,"-- remove Email application","rm Email.apk"
                ,"-- remove FileManager application","rm FileManager.apk"
                ,"-- remove Gallery2 application","rm Gallery2.apk"
                ,"-- remove videoPlayer application","rm videoPlayer.apk"
                ,"-- remove videoPlayer application","rm videoPlayer.apk"
                ,"-- remove Browser application","rm Browser.apk"
                ,"-- remove Contacts application","rm Contacts.apk"
                ,"-- remove DeskClock application","rm DeskClock.apk"
                //notes app not found

                ,"cd /sdcard/Download/Configuration/apk"

                ,"-- install applications"
        };



        String[] listCommands=Utils.concat(commands,Utils.getApkToInstall());
        listCommands =Utils.concat(listCommands,new String[]{"-- reboot","-c", "reboot"});
        runShellCommand(listCommands);
        //runShellCommand(new String[]{"shutdown"});
    }


    public void runShellCommand(String[] commands){

        try {

            Process suProcess = Runtime.getRuntime().exec("ls -l");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

            for(int i=0;i<commands.length;i++){
                Log.e("show","-----------------------------------------------"+commands[i]);
                if(commands[i].contains("--")) {
                    Log.e("show",commands[i]);
                    sendStatus(ACTION_SEND_STAUTUS_INFORMATION,commands[i]);
                    Thread.sleep(1000);
                }
                else{
                    Log.e("print",commands[i]);
                    os.writeBytes(commands[i]+"\n");
                    os.flush();
                }
            }

            //os.close();

            suProcess.waitFor();
            os.writeBytes("exit\n");
            os.flush();

        Log.e("runcommands","finish");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changeScreen(){
        WallpaperManager wallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard,"/Download/Configuration/screen.png");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(file), options);
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendStatus(String action,String status){
        Intent  intent = new Intent(action);
        intent.putExtra("status", status);
        sendBroadcast(intent);
    }

    public void decrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"/Download/Configuration/configuration.json");
        File file2 = new File(sdcard,"/Download/Configuration/configuration");

        FileInputStream fis = new FileInputStream(file2);

        FileOutputStream fos = new FileOutputStream(file);
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
        configuration=Utils.readConfigurationFromJsonFile();
        boolean resultComparHashFile=Utils.comparaFile(configuration.getFiles(),Utils.getListHashFiles());

        Intent  intent = new Intent(ACTION_RESULT_VERIFY_FILE);
        intent.putExtra("password", configuration.getPassword());
        intent.putExtra("username", configuration.getUserNAme());
        intent.putExtra("result", resultComparHashFile+"");
        sendBroadcast(intent);
    }




}
