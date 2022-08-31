package com.example.ahmadhasim.ilabinventory.inventaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

/**
 * Created by AHMAD HASIM on 9/8/2016.
 */
public class InventTambah extends AppCompatActivity {


    InventAdapter adapter;
    int success;

    private static final String TAG = InventTambah.class.getSimpleName();

    private static String url_insert        = Server.URL + "inventaris_insert.php";

    public static final String TAG_ID       = "stuff_id";
    public static final String TAG_NAMA     = "stuff_name";
    public static final String TAG_BRAND    = "stuff_brand";
    public static final String TAG_MODEL    = "stuff_model";

    public static final String TAG_SUB_ID   = "sub_stuff_id";
    public static final String TAG_PARENT   = "parent_id";
    public static final String TAG_SERIAL   = "sub_stuff_serial_number";
    public static final String TAG_KONDISI  = "sub_stuff_condition";
    public static final String TAG_SEDIA    = "sub_stuff_borrow";
    public static final String TAG_TAHUN    = "sub_stuff_year_purchase";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    EditText txt_id, txt_parent, txt_nama,txt_merk, txt_model, txt_serial, txt_tahun;
    ImageButton scan;
    Spinner txt_kondisi, txt_sedia;
    Button btn_tambah_invent, btn_edit_invent;

    String parent, nama, merk, model, serial, kondisi, sedia, tahun;

    String kondisitype[]= {"Baik", "Kurang Baik", "Rusak", "Hancur"};
    String sediatype [] = {"Ada", "Sedang Dipinjam", "Sedang Diperbaiki", "Hilang", "Tidak Tersedia"};

    ArrayAdapter<String> adapterKondisiType;
    ArrayAdapter<String> adapterSediaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventaris_form);

        txt_parent  = (EditText) findViewById(R.id.txt_parent);
        txt_nama    = (EditText) findViewById(R.id.txt_name);
        txt_merk    = (EditText) findViewById(R.id.txt_merk);
        txt_model   = (EditText) findViewById(R.id.txt_model);
        txt_serial  = (EditText) findViewById(R.id.txt_serial);
        txt_tahun   = (EditText) findViewById(R.id.txt_tahun);

        scan        = (ImageButton) findViewById(R.id.cek_barcode);

        txt_kondisi = (Spinner) findViewById(R.id.txt_kondisi);
        txt_sedia   = (Spinner) findViewById(R.id.txt_sedia);

        btn_tambah_invent   = (Button) findViewById(R.id.btn_tambah_invent);
        btn_edit_invent     = (Button) findViewById(R.id.btn_edit_invent);
        btn_edit_invent.setVisibility(View.GONE);

        Intent po   = getIntent();
        parent      = po.getStringExtra("parent");
        String sn   = po.getStringExtra("serial");
        if (sn.isEmpty()){
            txt_serial.setText("");
        }else {
            txt_serial.setText(sn);
        }

            adapterKondisiType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kondisitype);
            adapterKondisiType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            txt_kondisi.setAdapter(adapterKondisiType);

            txt_kondisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String kondisix = parent.getItemAtPosition(position).toString();

                    if (kondisix == "Baik") {
                        kondisi = "1";
                    } else if (kondisix == "Kurang Baik") {
                        kondisi = "2";
                    } else if (kondisix == "Rusak") {
                        kondisi = "3";
                    } else if (kondisix == "Hancur") {
                        kondisi = "4";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            adapterSediaType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sediatype);
            adapterSediaType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            txt_sedia.setAdapter(adapterSediaType);

            txt_sedia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String sediax = parent.getItemAtPosition(position).toString();

                    if (sediax == "Ada") {
                        sedia = "1";
                    } else if (sediax == "Sedang Dipinjam") {
                        sedia = "2";
                    } else if (sediax == "Sedang Diperbaiki") {
                        sedia = "3";
                    } else if (sediax == "Hilang") {
                        sedia = "4";
                    } else if (sediax == "Tidak Tersedia") {
                        sedia = "5";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent il = new Intent(InventTambah.this, InventScanSerial.class);
                il.putExtra("parent", parent);
                il.putExtra("status","0");
                startActivity(il);
            }
        });

            btn_tambah_invent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nama = txt_nama.getText().toString();
                    merk = txt_merk.getText().toString();
                    model = txt_model.getText().toString();
                    serial = txt_serial.getText().toString();
                    tahun = txt_tahun.getText().toString();
                    if (nama.isEmpty()){
                        Toast.makeText(InventTambah.this, "Nama Inventaris Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show();
                    }
                    else if (serial.isEmpty()){
                        Toast.makeText(InventTambah.this, "Serial Inventaris Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show();
                    }
                    else if (tahun.isEmpty()){
                        Toast.makeText(InventTambah.this, "Tahun Inventaris Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        simpan();
                    }

                }
            });

    }

    private void simpan(){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Intent ma = new Intent(InventTambah.this, InventMain.class);
                        ma.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ma.putExtra("code", parent);
                        startActivity(ma);

                        Toast.makeText(InventTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(InventTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventTambah.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("stuff_name", nama);
                params.put("stuff_brand", merk);
                params.put("stuff_model", model);
                params.put("parent_id", parent);
                params.put("sub_stuff_serial_number", serial);
                params.put("sub_stuff_condition", kondisi);
                params.put("sub_stuff_borrow", sedia);
                params.put("sub_stuff_year_purchase", tahun);

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
