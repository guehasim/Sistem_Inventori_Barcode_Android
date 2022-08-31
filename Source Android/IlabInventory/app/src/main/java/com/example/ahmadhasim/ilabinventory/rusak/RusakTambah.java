package com.example.ahmadhasim.ilabinventory.rusak;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmadhasim.ilabinventory.R;
import com.example.ahmadhasim.ilabinventory.controller.AppController;
import com.example.ahmadhasim.ilabinventory.controller.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by AHMAD HASIM on 11/23/2016.
 */
public class RusakTambah extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    String idx, tanggalx, serialx, sebabx;
    EditText penyebabx ;
    int success;

    private static final String TAG = RusakTambah.class.getSimpleName();
    private static String url_lapor_rusak   = Server.URL + "inventaris_insert_lapor_rusak.php";
    private static String url_get_sub_id    = Server.URL + "rusak_get_invent.php";
    private static String url_update_rusak  = Server.URL + "inventaris_update_lapor_rusak.php";

    public static final String TAG_SUB_ID   = "sub_stuff_id";
    public static final String TAG_SERIAL   = "sub_stuff_serial_number";
    public static final String TAG_KONDISI  = "sub_stuff_condition";
    public static final String TAG_SEDIA    = "sub_stuff_borrow";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.rusak_tambah_barcode);
        penyebabx = (EditText) findViewById(R.id.txt_penyebab);
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

        String a = penyebabx.getText().toString();

        if (a.isEmpty()){
            Toast.makeText(RusakTambah.this, "Isi dulu penyebabnya", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "ID Inventaris = " + rawResult.getContents(), Toast.LENGTH_SHORT).show();
            serialx = rawResult.getContents();
            get_id_sub(serialx);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(RusakTambah.this);
            }
        }, 2000);
    }

    public void setupToolbar() {
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void get_id_sub(final String serialx) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url_get_sub_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());

                        String kondisi  = jObj.getString(TAG_KONDISI);
                        String sedia    = jObj.getString(TAG_SEDIA);

                        idx = jObj.getString(TAG_SUB_ID);

                        if (kondisi == "3" ){
                            Toast.makeText(RusakTambah.this, "Inventaris Sudah Pernah Rusak, Silahkan Cek!!", Toast.LENGTH_SHORT).show();
                        }
                        else if (kondisi == "3"  && sedia == "3"){
                            Toast.makeText(RusakTambah.this, "Inventaris Sedang Diperbaiki", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            simpan();
                            update_done();
                        }

                    } else {
                        Toast.makeText(RusakTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(RusakTambah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("serial", serialx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void simpan(){

        Calendar now = Calendar.getInstance();
        String myformat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tanggalx = sdf.format(now.getTime());
        sebabx = penyebabx.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_lapor_rusak, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Toast.makeText(RusakTambah.this, "Penambahan Selesai, Inventaris Menjadi Rusak", Toast.LENGTH_LONG).show();

                        Intent a = new Intent(RusakTambah.this, RusakMain.class);
                        startActivity(a);
                    } else {
                        Toast.makeText(RusakTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(RusakTambah.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("sub_stuff_id", idx);
                params.put("broken_problem", sebabx);
                params.put("broken_date", tanggalx);
                params.put("broken_status", "0");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void update_done(){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_update_rusak, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                    } else {
                        Toast.makeText(RusakTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(RusakTambah.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", idx);
                params.put("sub_stuff_condition", "3");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), RusakMain.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), RusakMain.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
