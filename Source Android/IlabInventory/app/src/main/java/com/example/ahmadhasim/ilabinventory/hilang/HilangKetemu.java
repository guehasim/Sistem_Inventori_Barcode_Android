package com.example.ahmadhasim.ilabinventory.hilang;

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
 * Created by AHMAD HASIM on 9/13/2016.
 */
public class HilangKetemu extends AppCompatActivity {

    int success;

    private static final String TAG             = HilangKetemu.class.getSimpleName();
    private static String url_insert            = Server.URL + "hilang_insert_ketemu.php";
    private static String url_update            = Server.URL + "hilang_update_ketemu.php";

    private static final String TAG_SUCCESS     = "success";
    private static final String TAG_MESSAGE     = "message";

    String tag_json_obj = "json_obj_req";

    EditText tgl_ketemu, catatan, txt_nama, txt_serial, txt_tgl;
    ImageButton dates;
    Button ketemu;
    String id, sub_id, namax, serialx, tglx;
    Calendar now  = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hilang_ketemu_form);

        txt_nama    = (EditText) findViewById(R.id.txt_nama_ketemu);
        txt_serial  = (EditText) findViewById(R.id.txt_serial_ketemu);
        txt_tgl     = (EditText) findViewById(R.id.txt_tgl_hilang);
        tgl_ketemu  = (EditText) findViewById(R.id.txt_tgl_ketemu);
        catatan     = (EditText) findViewById(R.id.txt_note_ketemu);
        dates       = (ImageButton) findViewById(R.id.cek_start_ketemu);
        ketemu      = (Button) findViewById(R.id.btn_ketemu);

        Intent lp   = getIntent();
        id          = lp.getStringExtra("broken_id");
        sub_id      = lp.getStringExtra("sub_stuff_id");
        namax       = lp.getStringExtra("nama");
        serialx     = lp.getStringExtra("serial");
        tglx        = lp.getStringExtra("tgl");

        txt_nama.setText(namax);
        txt_serial.setText(serialx);
        txt_tgl.setText(tglx);

        tanggal();

        ketemu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
    }

    private void tanggal(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, monthOfYear);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myformat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
                tgl_ketemu.setText(sdf.format(now.getTime()));
            }
        };

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HilangKetemu.this, date, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String myformat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tgl_ketemu.setText(sdf.format(now.getTime()));
    }

    private void simpan(){

        final String broken_id  = id;
        final String tgl_done   = tgl_ketemu.getText().toString();
        final String note       = catatan.getText().toString();
        StringRequest strReq = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());
                        update();
                    } else {
                        Toast.makeText(HilangKetemu.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HilangKetemu.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("broken_id", broken_id);
                params.put("ketemu_date", tgl_done);
                params.put("ketemu_note", note);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void update(){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Toast.makeText(HilangKetemu.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                        Intent lp = new Intent(HilangKetemu.this, HilangMain.class);
                        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(lp);
                    } else {
                        Toast.makeText(HilangKetemu.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HilangKetemu.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("sub_stuff_id", sub_id);
                params.put("broken_id", id);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), HilangMain.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), HilangMain.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
