package com.example.ahmadhasim.ilabinventory.inventaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AHMAD HASIM on 8/22/2016.
 */
public class InventDetailTambah extends AppCompatActivity {

    int success;
//    InventAdapter adapter;

    private static final String TAG = InventDetailTambah.class.getSimpleName();
    private static String url_insert_detail = Server.URL + "inventaris_insert_detail.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    EditText meja, processor, ram, hardisk, so, ip, pc, dvd, monitor, keyboard, mouse;
    CheckBox vga, lan;
    Button tambah;

    String mejax, processorx, ramx, hardiskx, sox, ipx, pcx, vgax, lanx, dvdx, monitorx, keyboardx, mousex;

    String sub_id, tgl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventaris_detail_form);

        pindah();
    }

    private void pindah(){
        Intent a    = getIntent();
        sub_id      = a.getStringExtra("sub_id");
        tgl         = a.getStringExtra("tgl");

        simpan_start();
    }

    private void simpan_start(){
        meja        = (EditText) findViewById(R.id.txt_meja);
        processor   = (EditText) findViewById(R.id.txt_processor);
        ram         = (EditText) findViewById(R.id.txt_ram);
        hardisk     = (EditText) findViewById(R.id.txt_hardisk);
        so          = (EditText) findViewById(R.id.txt_so);
        ip          = (EditText) findViewById(R.id.txt_ip);
        pc          = (EditText) findViewById(R.id.txt_pc);
        vga         = (CheckBox) findViewById(R.id.cek_vga);
        lan         = (CheckBox) findViewById(R.id.cek_lan);
        dvd         = (EditText) findViewById(R.id.txt_dvd);
        monitor     = (EditText) findViewById(R.id.txt_monitor);
        keyboard    = (EditText) findViewById(R.id.txt_keyboard);
        mouse       = (EditText) findViewById(R.id.txt_mouse);
        tambah      =(Button) findViewById(R.id.btn_tambah_form_detail);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mejax       = meja.getText().toString();
                processorx  = processor.getText().toString();
                ramx        = ram.getText().toString();
                hardiskx    = hardisk.getText().toString();
                sox         = so.getText().toString();
                ipx         = ip.getText().toString();
                pcx         = pc.getText().toString();

                if(vga.isChecked()){
                    vgax    = "1";
                }else{
                    vgax    = "0";
                }

                if(lan.isChecked()){
                    lanx    = "1";
                }else{
                    lanx    = "0";
                }
                dvdx        = dvd.getText().toString();
                monitorx    = monitor.getText().toString();
                keyboardx   = keyboard.getText().toString();
                mousex      = mouse.getText().toString();

                simpan_done();
            }
        });
    }

    private void simpan_done(){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_insert_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Toast.makeText(InventDetailTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                        Intent po = new Intent(InventDetailTambah.this, InventDetail.class);
                        po.putExtra("id",sub_id);
                        po.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(po);

//                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(InventDetailTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventDetailTambah.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("sub_stuff_id", sub_id);
                params.put("log_spec_date", tgl);
                params.put("spec_table", mejax);
                params.put("spec_processor", processorx);
                params.put("spec_ram", ramx);
                params.put("spec_harddisk", hardiskx);
                params.put("spec_operating_system", sox);
                params.put("spec_computer_name", pcx);
                params.put("spec_ip_address", ipx);
                params.put("spec_vga_card", vgax);
                params.put("spec_lan_card", lanx);
                params.put("spec_dvd_rom", dvdx);
                params.put("spec_monitor", monitorx);
                params.put("spec_keyboard", keyboardx);
                params.put("spec_mouse", mousex);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), InventDetail.class);
        lp.putExtra("id",sub_id);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), InventDetail.class);
                lp.putExtra("id",sub_id);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
