package com.example.ahmadhasim.ilabinventory.perbaiki;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by AHMAD HASIM on 9/16/2016.
 */
public class PerbaikiFix extends AppCompatActivity {

    String repair_id, sub_stuff_id, broken_id, namax, serialx, orangx;
    String done_date, note, kondisi;
    ImageButton cek_done;
    EditText txt_tgl_done, txt_catatan, txt_nama, txt_serial, txt_org;
    Spinner condition;
    Button btn_fix;

    int success;
    String kondis[] = {"Baik", "Kurang Baik", "Rusak", "Hancur"};
    ArrayAdapter<String> adapterKondisiType;

    private static final String TAG = PerbaikiFix.class.getSimpleName();

    private static String url_get_data          = Server.URL + "perbaiki_get_data.php";
    private static String url_update            = Server.URL + "perbaiki_update.php";

    private static final String TAG_SUB_ID       = "sub_stuff_id";
    private static final String TAG_BROKEN_ID    = "broken_id";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    final Calendar now = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perbaiki_fix_form);

        txt_nama        = (EditText) findViewById(R.id.txt_repair_nama);
        txt_serial      = (EditText) findViewById(R.id.txt_repair_serial);
        txt_org         = (EditText) findViewById(R.id.txt_repair_org);
        txt_tgl_done    = (EditText) findViewById(R.id.txt_repair_tgl_selesai);
        txt_catatan     = (EditText) findViewById(R.id.txt_repair_note);
        cek_done        = (ImageButton) findViewById(R.id.cek_done_repair);
        condition       = (Spinner) findViewById(R.id.txt_kondisi_repair);
        btn_fix         = (Button) findViewById(R.id.btn_selesai_perbaiki);


        Intent kp   = getIntent();
        repair_id   = kp.getStringExtra("repair_id");
        namax       = kp.getStringExtra("nama");
        serialx     = kp.getStringExtra("serial");
        orangx      = kp.getStringExtra("orang");

        txt_nama.setText(namax);
        txt_serial.setText(serialx);
        txt_org.setText(orangx);

        done_date();

        adapterKondisiType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kondis);
        adapterKondisiType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        condition.setAdapter(adapterKondisiType);

        condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        btn_fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done_date   = txt_tgl_done.getText().toString();
                note        = txt_catatan.getText().toString();
                getRepair();
            }
        });

    }

    private void done_date(){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, monthOfYear);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myformat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
                txt_tgl_done.setText(sdf.format(now.getTime()));
            }
        };

        cek_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PerbaikiFix.this, date, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String myformat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        txt_tgl_done.setText(sdf.format(now.getTime()));
    }

    public void getRepair(){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_get_data, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        broken_id     = jObj.getString(TAG_BROKEN_ID);
                        sub_stuff_id  = jObj.getString(TAG_SUB_ID);
                        update_perbaiki();
                    } else {
                        Toast.makeText(PerbaikiFix.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PerbaikiFix.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", repair_id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void update_perbaiki(){

        StringRequest strReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Intent a = new Intent(PerbaikiFix.this, PerbaikiMain.class);
                        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(a);
                        Toast.makeText(PerbaikiFix.this, "Inventaris Sudah Diperbaiki", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(PerbaikiFix.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PerbaikiFix.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("repair_id", repair_id);
                params.put("done_date", done_date);
                params.put("catatan", note);
                params.put("sub_stuff_id", sub_stuff_id);
                params.put("kondisi", kondisi);
                params.put("broken_id", broken_id);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), PerbaikiMain.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), PerbaikiMain.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }
}
