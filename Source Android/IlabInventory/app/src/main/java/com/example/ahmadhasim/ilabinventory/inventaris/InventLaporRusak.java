package com.example.ahmadhasim.ilabinventory.inventaris;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

/**
 * Created by AHMAD HASIM on 8/24/2016.
 */
public class InventLaporRusak extends AppCompatActivity {

    int success;

    private static final String TAG = InventDetailTambah.class.getSimpleName();
    private static String url_lapor_rusak   = Server.URL + "inventaris_insert_lapor_rusak.php";
    private static String url_update_rusak  = Server.URL + "inventaris_update_lapor_rusak.php";
    private static String url_load          = Server.URL + "inventaris_get_detail.php";

    public static final String TAG_PARENT   = "parent_id";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    EditText namax, serialx, tgl_rusak, note_rusak;
    Button tambah;
    ImageButton cek_tgl;
    final Calendar now = Calendar.getInstance();

    String sub_idx, tglx, notex, parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventaris_lapor_rusak);

        Intent in = getIntent();
        sub_idx = in.getStringExtra("id");
        String abb = in.getStringExtra("nama");
        String acc = in.getStringExtra("serial");


        namax       = (EditText) findViewById(R.id.txt_name_rusak);
        serialx     = (EditText) findViewById(R.id.txt_serial_rusak);
        tgl_rusak   = (EditText) findViewById(R.id.txt_tgl_rusak);
        note_rusak  = (EditText) findViewById(R.id.txt_note_rusak);
        cek_tgl     = (ImageButton) findViewById(R.id.cek_tgl_rusak);
        tambah      = (Button) findViewById(R.id.btn_tambah_rusak);

        namax.setText(""+abb);
        serialx.setText(""+acc);

        pilih_tgl();
        tampil_parent();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tglx = tgl_rusak.getText().toString();
                notex = note_rusak.getText().toString();
                simpan_done();
                update_done();
            }
        });
    }

    private void tampil_parent(){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_load, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        parent      = jObj.getString(TAG_PARENT);

                    } else {
                        Toast.makeText(InventLaporRusak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventLaporRusak.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sub_idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void pilih_tgl(){


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, monthOfYear);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myformat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
                tgl_rusak.setText(sdf.format(now.getTime()));
            }
        };

        cek_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InventLaporRusak.this, date, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String myformat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tgl_rusak.setText(sdf.format(now.getTime()));
    }

    private void loadid(){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_load, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx      = jObj.getString(TAG_PARENT);

                        Intent po = new Intent(InventLaporRusak.this, InventMain.class);
                        po.putExtra("code", idx);
                        po.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(po);

                    } else {
                        Toast.makeText(InventLaporRusak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventLaporRusak.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sub_idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void simpan_done(){

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

                        Toast.makeText(InventLaporRusak.this, "Inventaris Menjadi Rusak", Toast.LENGTH_SHORT).show();

                        loadid();

                    } else {
                        Toast.makeText(InventLaporRusak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventLaporRusak.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("sub_stuff_id", sub_idx);
                params.put("broken_problem", notex);
                params.put("broken_date", tglx);
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
                        Toast.makeText(InventLaporRusak.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventLaporRusak.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", sub_idx);
                params.put("sub_stuff_condition", "3");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), InventMain.class);
        lp.putExtra("code",parent);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), InventMain.class);
                lp.putExtra("code",parent);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
