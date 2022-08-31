package com.example.ahmadhasim.ilabinventory.inventaris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.Map;

/**
 * Created by AHMAD HASIM on 8/21/2016.
 */
public class InventDetail extends AppCompatActivity {


    int success;

    private static final String TAG = InventDetail.class.getSimpleName();

    private static String url_detail        = Server.URL + "inventaris_get_detail.php";
    private static String url_get_detail    = Server.URL + "inventaris_get_detail_spec.php";

    public static final String TAG_ID       = "stuff_id";
    public static final String TAG_NAMA     = "stuff_name";
    public static final String TAG_BRAND    = "stuff_brand";
    public static final String TAG_MODEL    = "stuff_model";

    public static final String TAG_PARENT   = "parent_id";
    public static final String TAG_SERIAL   = "sub_stuff_serial_number";
    public static final String TAG_KONDISI  = "sub_stuff_condition";
    public static final String TAG_SEDIA    = "sub_stuff_borrow";
    public static final String TAG_TAHUN    = "sub_stuff_year_purchase";

    public static final String TAG_SUB_ID   = "sub_stuff_id";
    public static final String TAG_MEJA     = "spec_table";
    public static final String TAG_PROCESSOR= "spec_processor";
    public static final String TAG_RAM      = "spec_ram";
    public static final String TAG_HARDISK  = "spec_harddisk";
    public static final String TAG_SO       = "spec_operating_system";
    public static final String TAG_IP       = "spec_ip_address";
    public static final String TAG_PC       = "spec_computer_name";
    public static final String TAG_VGA      = "spec_vga_card";
    public static final String TAG_LAN      = "spec_lan_card";
    public static final String TAG_DVD      = "spec_dvd_rom";
    public static final String TAG_MONITOR  = "spec_monitor";
    public static final String TAG_KEYBOARD = "spec_keyboard";
    public static final String TAG_MOUSE    = "spec_mouse";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    String tag_json_obj = "json_obj_req";

    LinearLayout meja, processor, ram, hardisk, so, pc, ip, vga, lan, dvd, monitor, keyboard, mouse;
    TextView nama_label, nama, merk, model, serial, kondisi, sedia, tahun;
    TextView dt_meja, dt_processor, dt_ram, dt_hardisk, dt_so, dt_ip, dt_pc, dt_vga, dt_lan, dt_dvd, dt_monitor, dt_keyboard, dt_mouse;
    Button tambah;

    String sub_id, parent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventaris_detail);

        tambah = (Button) findViewById(R.id.btn_tambah_detail);

        meja        = (LinearLayout) findViewById(R.id.layout_meja);
        processor   = (LinearLayout) findViewById(R.id.layout_processor);
        ram         = (LinearLayout) findViewById(R.id.layout_ram);
        hardisk     = (LinearLayout) findViewById(R.id.layout_hardisk);
        so          = (LinearLayout) findViewById(R.id.layout_so);
        pc          = (LinearLayout) findViewById(R.id.layout_pc);
        ip          = (LinearLayout) findViewById(R.id.layout_ip);
        vga         = (LinearLayout) findViewById(R.id.layout_vga);
        lan         = (LinearLayout) findViewById(R.id.layout_lan);
        dvd         = (LinearLayout) findViewById(R.id.layout_dvd);
        monitor     = (LinearLayout) findViewById(R.id.layout_monitor);
        keyboard    = (LinearLayout) findViewById(R.id.layout_keyboard);
        mouse       = (LinearLayout) findViewById(R.id.layout_mouse);

        meja.setVisibility(View.GONE);
        processor.setVisibility(View.GONE);
        ram.setVisibility(View.GONE);
        hardisk.setVisibility(View.GONE);
        so.setVisibility(View.GONE);
        pc.setVisibility(View.GONE);
        ip.setVisibility(View.GONE);
        vga.setVisibility(View.GONE);
        lan.setVisibility(View.GONE);
        dvd.setVisibility(View.GONE);
        monitor.setVisibility(View.GONE);
        keyboard.setVisibility(View.GONE);
        mouse.setVisibility(View.GONE);

        tambah.setVisibility(View.GONE);

//
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formatdate = df.format(c.getTime());

                Intent oc = new Intent(getApplicationContext(), InventDetailTambah.class);
                oc.putExtra("tgl",formatdate);
                oc.putExtra("sub_id", sub_id);
                startActivity(oc);
            }
        });
        component();

    }

    private void component(){
        Intent a = getIntent();
        String x = a.getStringExtra("id");
        final String idx = x;
        tampil(idx);
    }

    private void tampil(final String idx){

        nama_label  = (TextView) findViewById(R.id.detail_name_label);
        nama        = (TextView) findViewById(R.id.detail_name);
        merk        = (TextView) findViewById(R.id.detail_merk);
        model       = (TextView) findViewById(R.id.detail_model);
        serial      = (TextView) findViewById(R.id.detail_serial);
        kondisi     = (TextView) findViewById(R.id.detail_kondisi);
        sedia       = (TextView) findViewById(R.id.detail_sedia);
        tahun       = (TextView) findViewById(R.id.detail_tahun);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx      = jObj.getString(TAG_ID);
                        String namax    = jObj.getString(TAG_NAMA);
                        String brandx   = jObj.getString(TAG_BRAND);
                        String modelx   = jObj.getString(TAG_MODEL);
                        String sub_idx  = jObj.getString(TAG_SUB_ID);
                        String serialx  = jObj.getString(TAG_SERIAL);
                        parent          = jObj.getString(TAG_PARENT);
                        String kondisix = jObj.getString(TAG_KONDISI);
                        String sediax   = jObj.getString(TAG_SEDIA);
                        String tahunx   = jObj.getString(TAG_TAHUN);

                        sub_id = sub_idx;
                        nama_label.setText(namax);
                        nama.setText(namax);
                        merk.setText(brandx);
                        model.setText(modelx);
                        serial.setText(serialx);

                        int b = Integer.parseInt(kondisix);
                        int d = Integer.parseInt(sediax);

                        if(b == 1){
                            kondisi.setText("Baik");
                        }
                        else if(b == 2){
                            kondisi.setText("Tidak Baik");
                        }
                        else if(b == 3){
                            kondisi.setText("Rusak");
                        }
                        else if(b == 4){
                            kondisi.setText("Hancur");
                        }

                        if(d == 1){
                            sedia.setText("Ada");
                        }
                        else if(d == 2){
                            sedia.setText("Sedang Dipinjam");
                        }
                        else if(d == 3){
                            sedia.setText("Sedang Diperbaiki");
                        }
                        else if(d == 4){
                            sedia.setText("Hilang");
                        }
                        else if(d == 5){
                            sedia.setText("Tidak Ada");
                        }

                        tahun.setText(tahunx);

                        final String idy = sub_idx;
                        detail(idy);

                    } else {
                        Toast.makeText(InventDetail.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(InventDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idx);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void detail(final String idy){

        meja        = (LinearLayout) findViewById(R.id.layout_meja);
        processor   = (LinearLayout) findViewById(R.id.layout_processor);
        ram         = (LinearLayout) findViewById(R.id.layout_ram);
        hardisk     = (LinearLayout) findViewById(R.id.layout_hardisk);
        so          = (LinearLayout) findViewById(R.id.layout_so);
        pc          = (LinearLayout) findViewById(R.id.layout_pc);
        ip          = (LinearLayout) findViewById(R.id.layout_ip);
        vga         = (LinearLayout) findViewById(R.id.layout_vga);
        lan         = (LinearLayout) findViewById(R.id.layout_lan);
        dvd         = (LinearLayout) findViewById(R.id.layout_dvd);
        monitor     = (LinearLayout) findViewById(R.id.layout_monitor);
        keyboard    = (LinearLayout) findViewById(R.id.layout_keyboard);
        mouse       = (LinearLayout) findViewById(R.id.layout_mouse);
        tambah      = (Button) findViewById(R.id.btn_tambah_detail);

        dt_meja     = (TextView) findViewById(R.id.detail_meja);
        dt_processor= (TextView) findViewById(R.id.detail_processor);
        dt_ram      = (TextView) findViewById(R.id.detail_ram);
        dt_hardisk  = (TextView) findViewById(R.id.detail_hardisk);
        dt_so       = (TextView) findViewById(R.id.detail_so);
        dt_pc       = (TextView) findViewById(R.id.detail_pc);
        dt_ip       = (TextView) findViewById(R.id.detail_ip);
        dt_vga      = (TextView) findViewById(R.id.detail_vga);
        dt_lan      = (TextView) findViewById(R.id.detail_lan);
        dt_dvd      = (TextView) findViewById(R.id.detail_dvd);
        dt_monitor  = (TextView) findViewById(R.id.detail_monitor);
        dt_keyboard = (TextView) findViewById(R.id.detail_keyboard);
        dt_mouse    = (TextView) findViewById(R.id.detail_mouse);


        StringRequest strReq = new StringRequest(Request.Method.POST, url_get_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());

                        tambah.setVisibility(View.GONE);

                        String mejax        = jObj.getString(TAG_MEJA);
                        String processorx   = jObj.getString(TAG_PROCESSOR);
                        String ramx         = jObj.getString(TAG_RAM);
                        String hardiskx     = jObj.getString(TAG_HARDISK);
                        String sox          = jObj.getString(TAG_SO);
                        String pcx          = jObj.getString(TAG_PC);
                        String ipx          = jObj.getString(TAG_IP);
                        String vgax         = jObj.getString(TAG_VGA);
                        String lanx         = jObj.getString(TAG_LAN);
                        String dvdx         = jObj.getString(TAG_DVD);
                        String monitorx     = jObj.getString(TAG_MONITOR);
                        String keyboardx    = jObj.getString(TAG_KEYBOARD);
                        String mousex       = jObj.getString(TAG_MOUSE);

                        dt_meja.setText(mejax);
                        dt_processor.setText(processorx);
                        dt_ram.setText(ramx);
                        dt_hardisk.setText(hardiskx);
                        dt_so.setText(sox);
                        dt_pc.setText(pcx);
                        dt_ip.setText(ipx);

                        String vg = vgax;
                        int gv = Integer.parseInt(vg);
                        String la = lanx;
                        int al = Integer.parseInt(la);
                        if (gv == 1){
                            dt_vga.setText("Ada");
                        }else{
                            dt_vga.setText("-");
                        }

                        if (al == 1){
                            dt_lan.setText("Ada");
                        }else{
                            dt_lan.setText("-");
                        }
//
                        dt_dvd.setText(dvdx);
                        dt_monitor.setText(monitorx);
                        dt_keyboard.setText(keyboardx);
                        dt_mouse.setText(mousex);

                        meja.setVisibility(View.VISIBLE);
                        processor.setVisibility(View.VISIBLE);
                        ram.setVisibility(View.VISIBLE);
                        hardisk.setVisibility(View.VISIBLE);
                        so.setVisibility(View.VISIBLE);
                        pc.setVisibility(View.VISIBLE);
                        ip.setVisibility(View.VISIBLE);
                        vga.setVisibility(View.VISIBLE);
                        lan.setVisibility(View.VISIBLE);
                        dvd.setVisibility(View.VISIBLE);
                        monitor.setVisibility(View.VISIBLE);
                        keyboard.setVisibility(View.VISIBLE);
                        mouse.setVisibility(View.VISIBLE);

                    } else {
                        meja.setVisibility(View.GONE);
                        processor.setVisibility(View.GONE);
                        ram.setVisibility(View.GONE);
                        hardisk.setVisibility(View.GONE);
                        so.setVisibility(View.GONE);
                        pc.setVisibility(View.GONE);
                        ip.setVisibility(View.GONE);
                        vga.setVisibility(View.GONE);
                        lan.setVisibility(View.GONE);
                        dvd.setVisibility(View.GONE);
                        monitor.setVisibility(View.GONE);
                        keyboard.setVisibility(View.GONE);
                        mouse.setVisibility(View.GONE);

                        tambah.setVisibility(View.VISIBLE);

//                        Toast.makeText(InventDetail.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(InventDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idy);

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
