package com.infosys.tkmpoc;

import android.app.Application;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.service.BeaconManager;

public class TKMApplication extends Application {

    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        EstimoteSDK.initialize(getApplicationContext(), "", "");
        EstimoteSDK.enableDebugLogging(true);
    }

}
