package com.testcore.ui;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.testcore.R;

import java.io.IOException;

import rx.functions.Action0;
import rx.functions.Action1;

public class RxPermissionsActivity extends AppCompatActivity {

    private static final String TAG = "RxPermissionsSample";

    private Camera camera;
    private SurfaceView surfaceView;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = RxPermissions.getInstance(this);
        rxPermissions.setLogging(true);

        setContentView(R.layout.rx_act_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        rxPermissions.ensure(Manifest.permission.CAMERA);

//        RxPermissions.getInstance(this)
//                .request(Manifest.permission.CAMERA)
//                .subscribe(granted -> {
//                    if (granted) { // Always true pre-M
//                        // I can control the camera now
//                    } else {
//                        // Oups permission denied
//                    }
//                });

//        RxView.clicks(findViewById(R.id.enableCamera))
//                .compose(RxPermissions.getInstance(this).ensure(Manifest.permission.CAMERA))
//                .subscribe(granted -> {
//                    // R.id.enableCamera has been clicked
//                });

//        RxPermissions.getInstance(this)
//                .request(Manifest.permission.CAMERA,
//                        Manifest.permission.READ_PHONE_STATE)
//                .subscribe(granted -> {
//                    if (granted) {
//                        // All requested permissions are granted
//                    } else {
//                        // At least one permission is denied
//                    }
//                });

//        RxPermissions.getInstance(this)
//                .requestEach(Manifest.permission.CAMERA,
//                        Manifest.permission.READ_PHONE_STATE)
//                .subscribe(permission -> { // will emit 2 Permission objects
//                    if (permission.granted) {
//                        // `permission.name` is granted !
//                    }
//                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
