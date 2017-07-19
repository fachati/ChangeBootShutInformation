package com.mobiwire.BootInformation;

import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Aly on 13/07/2017.
 */

public class Utils {

    public static String[] concat(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c= new String[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static String[] getApkToInstall(){
        String[] cmdInstall;
        java.io.File sdcard = Environment.getExternalStorageDirectory();
        java.io.File directory = new java.io.File(sdcard,"/Download/Configuration/apk");
        java.io.File[] files = directory.listFiles();
        cmdInstall=new String[files.length];
        for (int i = 0; i < files.length; i=i+1){
            cmdInstall[i]="pm install " + files[i].getName();
        }
        return cmdInstall;
    }

    public static com.mobiwire.BootInformation.File[] getListHashFiles() {
        com.mobiwire.BootInformation.File[] tabFile;

        java.io.File sdcard = Environment.getExternalStorageDirectory();
        java.io.File folder = new java.io.File(sdcard,"/Download/Configuration");

        List<java.io.File> inFiles = new ArrayList<>();
        Queue<java.io.File> files = new LinkedList<>();
        files.addAll(Arrays.asList(folder.listFiles()));
        while (!files.isEmpty()) {
            java.io.File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            } else {
                if(!file.getName().contains("config"))
                    inFiles.add(file);
            }
        }
        tabFile=new com.mobiwire.BootInformation.File[inFiles.size()];
        for(int i=0;i<tabFile.length;i++){
            com.mobiwire.BootInformation.File fileTmp=new com.mobiwire.BootInformation.File();
            fileTmp.setName(inFiles.get(i));
            fileTmp.setHashKey(hashFile(inFiles.get(i),"SHA-256"));
            tabFile[i]=fileTmp;
        }

        return tabFile;
    }

    public static boolean comparaFile(com.mobiwire.BootInformation.File []fileA,com.mobiwire.BootInformation.File []fileB){
        boolean []isOk=new boolean[fileA.length];

        if(fileA.length!=fileB.length)
            return false;

        for(int i=0;i<fileA.length;i++){
            boolean isFound=false;
            Log.e("A",fileA[i].toString()+"\n");
            for(int j=0;j<fileB.length;j++){
                Log.e("B", fileB[j].toString());
                if(fileA[i].getHashKey().equals(fileB[j].getHashKey()))
                    isFound=true;
            }
            isOk[i]=isFound;
        }

        for(int i=0;i<isOk.length;i++){
            if(isOk[i]==false)
                return false;
        }
        return true;
    }

    private static String hashFile(java.io.File file, String algorithm) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    public static Configuration readConfigurationFromJsonFile(){
        java.io.File sdcard = Environment.getExternalStorageDirectory();
        java.io.File file = new java.io.File(sdcard,"/Download/Configuration/configuration.json");
        Log.e("filename",file.getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper();
        Configuration conf = null;
        try{

            conf = mapper.readValue(file, Configuration.class);
            Log.e("json",conf.toString());
        } catch (JsonGenerationException ex) {
            Log.e("json",ex.toString());
        } catch (JsonMappingException ex) {
            Log.e("json",ex.toString());
        } catch (IOException ex) {
            Log.e("json",ex.toString());
        }

        return conf;
    }
}
