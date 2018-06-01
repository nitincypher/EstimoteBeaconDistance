package com.infosys.tkmpoc;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

public class AlertActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private BeaconRegion region;
    private TextView textView;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        textView = findViewById(R.id.distance);
        constraintLayout = findViewById(R.id.rootView);
        //Check for permissions
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Double distance = round(getDistance(list.get(0)), 2);
                    if(distance >= 1.5){
                        constraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        textView.setText("PPE out of Range!");
                    } else {
                        constraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                        textView.setText(String.valueOf(distance) + " m");
                    }
                } else {
                    constraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    textView.setText("PPE out of Range!");
                }
            }
        });
        region = new BeaconRegion("factory",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);
        super.onPause();
    }

    double getDistance(Beacon beacon) {
        return Math.pow(10d, ((double) beacon.getMeasuredPower() - beacon.getRssi()) / (10 * 2));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
