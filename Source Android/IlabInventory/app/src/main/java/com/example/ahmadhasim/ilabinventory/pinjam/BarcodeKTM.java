package com.example.ahmadhasim.ilabinventory.pinjam;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmadhasim.ilabinventory.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by AHMAD HASIM on 8/31/2016.
 */
public class BarcodeKTM extends AppCompatActivity implements ZBarScannerView.ResultHandler{

    private ZBarScannerView mScannerView;
    String nimx, tanggalx, notex;
    EditText notes;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.pinjam_barcode);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
        Toast.makeText(this, "ID Peminjam = " + rawResult.getContents(), Toast.LENGTH_SHORT).show();
        nimx = rawResult.getContents();
        tampil();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeKTM.this);
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
        Intent po = new Intent(BarcodeKTM.this, PinjamMain.class);
        po.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(po);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent po = new Intent(BarcodeKTM.this, PinjamMain.class);
                po.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(po);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tampil(){
        Calendar now = Calendar.getInstance();
        String myformat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tanggalx = sdf.format(now.getTime());

        notes = (EditText) findViewById(R.id.txt_note_barcode);
        notex = notes.getText().toString();

        Intent lp = new Intent(BarcodeKTM.this, BarcodeBarang.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        lp.putExtra("nim",nimx);
        lp.putExtra("tanggal", tanggalx);
        lp.putExtra("note", notex);
        startActivity(lp);
    }
}
