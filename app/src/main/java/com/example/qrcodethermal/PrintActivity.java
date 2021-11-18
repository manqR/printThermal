package com.example.qrcodethermal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

public class PrintActivity extends AppCompatActivity {
    // TODO: Rename and change types of parameters
    private ProgressBar pb;
    private TextView txtProgress;
    private String text;
    private String printer;
    private String srcFragment;
    private Integer sysNo;

    BluetoothServicePos mService = null;
    BluetoothDevice con_dev = null;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_print);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Print");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        text = extras.getString("text");
        printer = "";
        srcFragment = extras.getString("srcFragment");
        sysNo = extras.getInt("sysNo");

        mService = new BluetoothServicePos(getApplicationContext(), mHandler);
        pb = (ProgressBar) findViewById(R.id.pbPrint);
        txtProgress = (TextView) findViewById(R.id.txtProgressPrint);
        showProgressBar(0, "");
        TryConnectToDefaultDevice();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connect Successful", Toast.LENGTH_SHORT).show();
                            showProgressBar(0, "Connect Successful");
                            if (text.length() > 0) {
                                showProgressBar(1, "Printing...");
                                mService.sendMessage(text + "\n", "GBK");
                                long endTime = System.currentTimeMillis() + 1000;
                                while (System.currentTimeMillis() < endTime) {
                                }
                            }
                            close();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            showProgressBar(1, "Menyambung printer...");
                            break;
                        case BluetoothService.STATE_LISTEN:
                            showProgressBar(1, "Menunggu koneksi printer...");
                            break;
                        case BluetoothService.STATE_NONE:
                            showProgressBar(0, "");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    showProgressBar(0, "Koneksi ke printer terputus");
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    showProgressBar(1, "Gagal terhubung ke printer");
                    Toast.makeText(getApplicationContext(), "Gagal terhubung ke printer, silahkan pilih printer lainnya...", Toast.LENGTH_LONG).show();
                    Intent serverIntent = new Intent(PrintActivity.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    break;
            }
        }

    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                    showProgressBar(0, "Bluetooth open successful");
                    TryConnectToDefaultDevice();
                } else {
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    con_dev = mService.getDevByMac(address);

                    mService.connect(con_dev);
                } else {
                    mService.stop();
                    finish();
                }
                break;
        }
    }

    @SuppressLint("SdCardPath")
    private void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        sendData = pg.printDraw();
        mService.write(sendData);
    }

    private void close() {
        PrintActivity.this.finish();
        getFragmentManager().popBackStackImmediate("tag", 0);
    }

    private void TryConnectToDefaultDevice() {
        //If bluetooth not available exit activity
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "Bluetooth is not available on this device", Toast.LENGTH_LONG).show();
            showProgressBar(0, "Bluetooth is not available on this device");
            getFragmentManager().popBackStackImmediate("tag", 0);
            //finish();
        }
        //Request enable bluetooth if bluetooth not open
        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        //Trying connect to default device
        try {
            if (mService.getState() == BluetoothService.STATE_NONE) {
                //Toast.makeText(this, "Connecting to m"+printer+ ", please wait...", Toast.LENGTH_LONG).show();
                showProgressBar(1, "Connecting to " + printer + ", please wait...");
                con_dev = mService.getDevByName(printer);
                mService.connect(con_dev);
            }

        } catch (Exception ex) {
            if (mService.isBTopen() == true) {
                Intent serverIntent = new Intent(PrintActivity.this, DeviceListActivity.class);
                serverIntent.putExtra("srcFragment", srcFragment);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Bluetooth ok", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "no ok", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void showProgressBar(int isVisible, String msg) {
        pb.setVisibility(isVisible);
        txtProgress.setText(msg);
    }
}