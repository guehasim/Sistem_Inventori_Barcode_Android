package com.example.ahmadhasim.ilabinventory.inventaris;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmadhasim.ilabinventory.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by AHMAD HASIM on 9/19/2016.
 */
public class InventScanSerial extends AppCompatActivity implements ZBarScannerView.ResultHandler{

    private ZBarScannerView mScannerView;
    String parent_id, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventaris_scan_serial);

        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

        Intent pkl = getIntent();
        parent_id  = pkl.getStringExtra("parent");
        status     = pkl.getStringExtra("status");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
        Toast.makeText(this, "Serial = " + result.getContents(), Toast.LENGTH_SHORT).show();
        String serial = result.getContents();

        Intent it = new Intent(InventScanSerial.this, InventTambah.class);
        it.putExtra("parent", parent_id);
        it.putExtra("serial",serial);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(InventScanSerial.this);
            }
        }, 2000);

    }

    public void setupToolbar() {
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), InventTambah.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        lp.putExtra("parent", parent_id);
        lp.putExtra("serial", "");
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), InventTambah.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                lp.putExtra("parent", parent_id);
                lp.putExtra("serial", "");
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
