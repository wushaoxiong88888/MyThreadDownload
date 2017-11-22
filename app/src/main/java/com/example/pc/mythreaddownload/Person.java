package com.example.pc.mythreaddownload;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pc on 2017/11/22.
 */

@Entity
public class Person {
    private String progress;

    @Generated(hash = 1649989104)
    public Person(String progress) {
        this.progress = progress;
    }

    @Generated(hash = 1024547259)
    public Person() {
    }

    public String getProgress() {
        return this.progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
