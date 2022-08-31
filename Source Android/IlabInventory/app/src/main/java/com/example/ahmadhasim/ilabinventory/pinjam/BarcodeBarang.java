package com.example.ahmadhasim.ilabinventory.pinjam;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by AHMAD HASIM on 9/1/2016.
 */
public class BarcodeBarang extends AppCompatActivity implements ZBarScannerView.ResultHandler{

    int success;

    private static final String TAG = PinjamBarang.class.getSimpleName();

    private static String url_insert_pinjam = Server.URL + "pinjam_insert.php";
    private static String url_get_sub_id    = Server.URL + "pinjam_get_data.php";
    private static String url_update_sedia  = Server.URL + "pinjam_update_barang.php";

    public static final String TAG_SUB_ID   = "sub_stuff_id";
    public static final String TAG_KONDISI  = "sub_stuff_borrow";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    private ZBarScannerView mScannerView;
    String nimx, tanggalx, notex, serial,sub_id, kondisi;;
    Button selesai;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.pinjam_barang_barcode);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

        Intent snd = getIntent();
        nimx        = snd.getStringExtra("nim");
        tanggalx    = snd.getStringExtra("tanggal");
        notex       = snd.getStringExtra("note");

        selesai = (Button) findViewById(R.id.btn_selesai_barcode);
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pp = new Intent(BarcodeBarang.this, PinjamMain.class);
                pp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pp);
            }
        });
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
        Toast.makeText(this, "ID Barang = " + rawResult.getContents(), Toast.LENGTH_SHORT).show();
        serial = rawResult.getContents();
        final String sn = rawResult.getContents();
        get_id_sub(sn);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeBarang.this);
            }
        }, 2000);
    }

    private void get_id_sub(final String sn) {

        StringRequest strReq = new StringRequest(Request.Method.POST, url_get_sub_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());

                        sub_id = jObj.getString(TAG_SUB_ID);
                        kondisi = jObj.getString(TAG_KONDISI);
                        int kondis = Integer.parseInt(kondisi);
                        if (kondis == 1) {
                            simpan();
                        } else {
                            Toast.makeText(BarcodeBarang.this, "Barang Sudah Ada Yang Pinjam", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(BarcodeBarang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BarcodeBarang.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sn);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void simpan(){

        final String id = nimx;
        final String tgl = tanggalx;
        final String no = notex;
        final String sub = sub_id;

        StringRequest strReq = new StringRequest(Request.Method.POST, url_insert_pinjam, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());
                        update_status();
                    } else {
                        Toast.makeText(BarcodeBarang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BarcodeBarang.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("borrow_borrower", id);
                params.put("borrow_date", tgl);
                params.put("borrow_note", no);
                params.put("sub_stuff_id", sub);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void update_status(){

        final String sub = sub_id;

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update_sedia, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());
                        Toast.makeText(BarcodeBarang.this, "Peminjaman Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BarcodeBarang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BarcodeBarang.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("sedia", "2");
                params.put("sub_stuff_id", sub);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

}
