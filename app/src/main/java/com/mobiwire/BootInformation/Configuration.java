package com.mobiwire.BootInformation;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Aly on 10/07/2017.
 */

public class Configuration implements Serializable {

    private String userNAme;
    private String password;
    private APN apn;
    private String language;
    private String defaultTimeZone;
    private File [] files;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (!userNAme.equals(that.userNAme)) return false;
        if (!password.equals(that.password)) return false;
        if (!apn.equals(that.apn)) return false;
        if (!language.equals(that.language)) return false;
        if (!defaultTimeZone.equals(that.defaultTimeZone)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(files, that.files);

    }

    @Override
    public int hashCode() {
        int result = userNAme.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + apn.hashCode();
        result = 31 * result + language.hashCode();
        result = 31 * result + defaultTimeZone.hashCode();
        result = 31 * result + Arrays.hashCode(files);
        return result;
    }

    public String getUserNAme() {
        return userNAme;
    }

    public void setUserNAme(String userNAme) {
        this.userNAme = userNAme;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public APN getApn() {
        return apn;
    }

    public void setApn(APN apn) {
        this.apn = apn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDefaultTimeZone() {
        return defaultTimeZone;
    }

    public void setDefaultTimeZone(String defaultTimeZone) {
        this.defaultTimeZone = defaultTimeZone;
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "userNAme='" + userNAme + '\'' +
                ", password='" + password + '\'' +
                ", apn=" + apn +
                ", language='" + language + '\'' +
                ", defaultTimeZone='" + defaultTimeZone + '\'' +
                ",files'" + files.toString() + '\'' +
                '}';
    }
}
