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
public class InventEdit extends AppCompatActivity {

    int success;

    private static final String TAG = InventTambah.class.getSimpleName();

    private static String url_edit        = Server.URL + "inventaris_update.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    EditText txt_id, txt_parent, txt_nama,txt_merk, txt_model, txt_serial, txt_tahun;
    Spinner txt_kondisi, txt_sedia;
    Button btn_tambah_invent, btn_edit_invent;

    ImageButton cek;

    String parent, nama, merk, model, serial, kondisi, sedia, tahun;
    String idx, namax, merkx, modelx, serialx, kondisix, sediax, tahunx;

    String kondisitype[]= {"Baik", "Kurang Baik", "Rusak", "Hancur"};
    String sediatype [] = {"Ada", "Sedang Dipinjam", "Sedang Diperbaiki", "Hilang", "Tidak Tersedia"};

    ArrayAdapter<String> adapterKondisiType;
    ArrayAdapter<String> adapterSediaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventaris_form);

        Intent po   = getIntent();
        idx         = po.getStringExtra("id");
        namax       = po.getStringExtra("nama");
        merkx       = po.getStringExtra("brand");
        modelx      = po.getStringExtra("model");
        parent      = po.getStringExtra("parent");
        serialx     = po.getStringExtra("serial");
        kondisix    = po.getStringExtra("kondisi");
        sediax      = po.getStringExtra("sedia");
        tahunx      = po.getStringExtra("tahun");

        txt_id      = (EditText) findViewById(R.id.txt_id);
        txt_nama    = (EditText) findViewById(R.id.txt_name);
        txt_merk    = (EditText) findViewById(R.id.txt_merk);
        txt_model   = (EditText) findViewById(R.id.txt_model);
        txt_serial  = (EditText) findViewById(R.id.txt_serial);
        txt_tahun   = (EditText) findViewById(R.id.txt_tahun);

        txt_kondisi = (Spinner) findViewById(R.id.txt_kondisi);
        txt_sedia   = (Spinner) findViewById(R.id.txt_sedia);

        btn_tambah_invent   = (Button) findViewById(R.id.btn_tambah_invent);
        btn_edit_invent     = (Button) findViewById(R.id.btn_edit_invent);
        btn_tambah_invent.setVisibility(View.GONE);
        cek = (ImageButton) findViewById(R.id.cek_barcode);
        cek.setVisibility(View.GONE);

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

        btn_edit_invent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = txt_nama.getText().toString();
                merk = txt_merk.getText().toString();
                model = txt_model.getText().toString();
                serial = txt_serial.getText().toString();
                tahun = txt_tahun.getText().toString();
                if (nama.isEmpty()){
                    Toast.makeText(InventEdit.this, "Nama Inventaris Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show();
                }
                else if (serial.isEmpty()){
                    Toast.makeText(InventEdit.this, "Serial Inventaris Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show();
                }
                else if (tahun.isEmpty()){
                    Toast.makeText(InventEdit.this, "Tahun Inventaris Tidak Boleh Kosong !!", Toast.LENGTH_SHORT).show();
                }
                else {
                    edit();
                }
            }
        });

        txt_id.setText(idx);
        txt_nama.setText(namax);
        txt_merk.setText(merkx);
        txt_model.setText(modelx);
        txt_serial.setText(serialx);

        int kon = Integer.parseInt(kondisix);
        if (kon == 1){
            txt_kondisi.setSelection(0);
        }
        else if (kon == 2){
            txt_kondisi.setSelection(1);
        }
        else if (kon == 3){
            txt_kondisi.setSelection(2);
        }
        else if (kon == 4){
            txt_kondisi.setSelection(3);
        }

        int sed = Integer.parseInt(sediax);
        if (sed == 1){
            txt_sedia.setSelection(0);
        }
        else if (sed == 2){
            txt_sedia.setSelection(1);
        }
        else if (sed == 3){
            txt_sedia.setSelection(2);
        }
        else if (sed == 4){
            txt_sedia.setSelection(3);
        }
        else if (sed == 5){
            txt_sedia.setSelection(4);
        }
        txt_tahun.setText(tahunx);

    }

    private void edit(){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Intent ma = new Intent(InventEdit.this, InventMain.class);
                        ma.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ma.putExtra("code", parent);
                        startActivity(ma);

                        Toast.makeText(InventEdit.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(InventEdit.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventEdit.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("stuff_id", idx);
                params.put("stuff_name", nama);
                params.put("stuff_brand", merk);
                params.put("stuff_model", model);
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
