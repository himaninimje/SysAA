package com.sem6.sysaa;

/**
 * Created by pd on 27-02-2017.
 */

import android.app.Application;
import com.firebase.client.Firebase;

public class SysAA extends Application{

    public void onCreate(){
        super.onCreate();

        Firebase.setAndroidContext(this);
    }


}
