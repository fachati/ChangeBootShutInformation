package com.mobiwire.BootInformation;

import java.io.Serializable;

/**
 * Created by Aly on 12/07/2017.
 */

public class File implements Serializable{

    private java.io.File name;
    private String hashKey;


    public File() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        if (!name.equals(file.name)) return false;
        return hashKey.equals(file.hashKey);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + hashKey.hashCode();
        return result;
    }

    public java.io.File getName() {
        return name;
    }

    public void setName(java.io.File name) {
        this.name = name;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    @Override
    public String toString() {
        return "File{" +
                "name=" + name.getAbsolutePath() +
                ", hashKey='" + hashKey + '\'' +
                '}';
    }
}
