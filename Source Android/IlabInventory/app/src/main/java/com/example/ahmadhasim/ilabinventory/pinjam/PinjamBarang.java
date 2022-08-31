package com.example.ahmadhasim.ilabinventory.pinjam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmadhasim.ilabinventory.R;
import com.example.ahmadhasim.ilabinventory.controller.AppController;
import com.example.ahmadhasim.ilabinventory.controller.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AHMAD HASIM on 8/27/2016.
 */
public class PinjamBarang extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Handler mHandler = new Handler();

    int success;
    ListView list;
    SwipeRefreshLayout swipe;
    List<PinjamData> itemList = new ArrayList<PinjamData>();
    PinjamManualAdapter adapter;

    private static final String TAG = PinjamBarang.class.getSimpleName();

    private static String url_insert_pinjam = Server.URL + "pinjam_insert.php";
    private static String url_get_sub_id    = Server.URL + "pinjam_get_data.php";
    private static String url_select_barang = Server.URL + "pinjam_select_barang.php";
    private static String url_update_sedia  = Server.URL + "pinjam_update_barang.php";

    public static final String TAG_SUB_ID   = "sub_stuff_id";
    public static final String TAG_SERIAL   = "sub_stuff_serial_number";
    public static final String TAG_KONDISI  = "sub_stuff_borrow";
    public static final String TAG_BARANG   = "stuff_name";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    String nim, tgl_start, notes, serial, sub_id, kondisi;
    EditText serialx;
    Button tambah, selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinjam_barang_form);

        Intent ok   = getIntent();
        nim         = ok.getStringExtra("id");
        tgl_start   = ok.getStringExtra("tanggal");
        notes       = ok.getStringExtra("note");

        serialx     = (EditText) findViewById(R.id.txt_id_bar);
        tambah      = (Button) findViewById(R.id.btn_tambah_barang);
        selesai     = (Button) findViewById(R.id.btn_simpan_barang);


        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.list_pinjam_barang);

        adapter = new PinjamManualAdapter(PinjamBarang.this, itemList);
        list.setAdapter(adapter);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serial = serialx.getText().toString();
                final String sn = serial;
                get_id_sub(sn);
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bc = new Intent(PinjamBarang.this, PinjamMain.class);
                bc.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(bc);
            }
        });

    }

    private void kosong(){
        serialx.setText("");
    }

    private void get_id_sub(final String sn){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_get_sub_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());

                        sub_id     = jObj.getString(TAG_SUB_ID);
                        kondisi    = jObj.getString(TAG_KONDISI);
                        int kondis = Integer.parseInt(kondisi);
                        if(kondis == 1){
//                            Toast.makeText(PinjamBarang.this, nim, Toast.LENGTH_SHORT).show();
                            simpan();
                        }else{
                            Toast.makeText(PinjamBarang.this, "Penyimpanan Tidak Bisa dilakukan", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(PinjamBarang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PinjamBarang.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        final String id = nim;
        final String tgl = tgl_start;
        final String no = notes;
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
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(PinjamBarang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PinjamBarang.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

    private void select(final String idz){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_select_barang, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        PinjamData item = new PinjamData();
                        item.setBarang(jsonObject.getString(TAG_BARANG));
                        item.setSerial(jsonObject.getString(TAG_SERIAL));
                        itemList.add(item);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(PinjamBarang.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idz);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void update_status(){

        final String sub = sub_id;
//        Toast.makeText(PinjamBarang.this, sub, Toast.LENGTH_SHORT).show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update_sedia, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());
                        final String idz = nim;
                        select(idz);
                        kosong();
                        Toast.makeText(PinjamBarang.this, "Penambahan Sukses!!", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(PinjamBarang.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PinjamBarang.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan SIMPAN untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
