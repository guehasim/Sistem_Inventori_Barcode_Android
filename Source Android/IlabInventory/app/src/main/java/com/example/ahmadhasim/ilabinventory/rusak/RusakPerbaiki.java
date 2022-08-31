package com.example.ahmadhasim.ilabinventory.rusak;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by AHMAD HASIM on 9/9/2016.
 */
public class RusakPerbaiki extends AppCompatActivity {

    EditText txt_tgl_perbaiki, txt_org, txt_name, txt_serial;
    Spinner sp_kondisi;
    Button perbaiki;
    ImageButton img_start_date;
    String broken_id, sub_stuff_id, start_date, repairer, namex, serialx;

    final Calendar now = Calendar.getInstance();

    int success;

    private static final String TAG = RusakPerbaiki.class.getSimpleName();

    private static String url_insert = Server.URL + "rusak_insert_perbaiki.php";
    private static String url_update = Server.URL + "rusak_update.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    ArrayAdapter<String> adapterKondisiType;
    String kondisitype[]= {"Baik", "Kurang Baik", "Rusak", "Hancur"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rusak_perbaiki_form);

        Intent pk   = getIntent();
        broken_id   = pk.getStringExtra("broken_id");
        sub_stuff_id= pk.getStringExtra("sub_stuff_id");
        namex       = pk.getStringExtra("nama");
        serialx     = pk.getStringExtra("serial");

        txt_name            = (EditText) findViewById(R.id.txt_name_perbaiki);
        txt_serial          = (EditText) findViewById(R.id.txt_serial_perbaiki);
        txt_tgl_perbaiki    = (EditText) findViewById(R.id.txt_rusak_tgl_perbaikan);
        txt_org             = (EditText) findViewById(R.id.txt_rusak_yg_perbaiki);
        perbaiki            = (Button) findViewById(R.id.btn_rusak_perbaiki);

        img_start_date      = (ImageButton) findViewById(R.id.cek_start_repair);

        txt_name.setText(namex);
        txt_serial.setText(serialx);

        start_date();

        perbaiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

    }

    private void start_date(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, monthOfYear);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myformat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
                txt_tgl_perbaiki.setText(sdf.format(now.getTime()));
            }
        };

        img_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RusakPerbaiki.this, date, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String myformat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        txt_tgl_perbaiki.setText(sdf.format(now.getTime()));
    }

    private void simpan(){

        start_date  = txt_tgl_perbaiki.getText().toString();
        repairer    = txt_org.getText().toString();
        final String pidi = broken_id;

        StringRequest strReq = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {

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
                        Toast.makeText(RusakPerbaiki.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RusakPerbaiki.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("repair_start_date", start_date);
                params.put("broken_id", pidi);
                params.put("repair_repairer", repairer);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void update_status(){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Intent kl = new Intent(RusakPerbaiki.this, RusakMain.class);
                        kl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(kl);
                        Toast.makeText(RusakPerbaiki.this, "Inventaris Rusak Mulai Diperbaiki", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RusakPerbaiki.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RusakPerbaiki.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("sedia", "3");
                params.put("status", "2");
                params.put("broken_id", broken_id);
                params.put("sub_stuff_id", sub_stuff_id);
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
        switch (item.getItemId()) {
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
