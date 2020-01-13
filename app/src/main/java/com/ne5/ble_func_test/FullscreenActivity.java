package com.ne5.ble_func_test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {

    View view_empty;

    private boolean isOpen=false, isSend=false;
    Button btn1;
    Button btn2;

    BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initPermission() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        view_empty = findViewById(R.id.view_empty);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);

        MyButton listener = new MyButton();
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);

        ImmersionBar.with(this)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
                .statusBarView(view_empty)
                .init();

        initPermission();


        if(blueadapter==null)
        {
            btn1.setEnabled(false);
            btn2.setEnabled(false);
            btn1.setText("手机不支持蓝牙");
            return ;
        }

        isOpen = blueadapter.isEnabled();
        if (isOpen) {
            btn1.setText("Close");
            btn2.setEnabled(true);
        }
        else {
            btn1.setText("Open");
            btn2.setEnabled(false);
        }
        btn2.setText("Send");

    }

    class MyButton implements View.OnClickListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch  (v.getId()) {
                case R.id.btn1:
                    isOpen = !isOpen;
                    if(isOpen != blueadapter.isEnabled())
                    {
                        if(isOpen)  blueadapter.enable();
                        else blueadapter.disable();
                    }
                    if (isOpen) {
                        btn1.setText("Close");
                        btn2.setEnabled(true);
                    }
                    else {
                        btn1.setText("Open");
                        btn2.setEnabled(false);
                        isSend = false;
                        btn2.setText("Send");
                    }
                    break;
                case R.id.btn2:
                    isSend = !isSend;
                    if (isSend) btn2.setText("Stop");
                    else   btn2.setText("Send");
                    break;
                default:
                    break;
            }
        }
    }

}
