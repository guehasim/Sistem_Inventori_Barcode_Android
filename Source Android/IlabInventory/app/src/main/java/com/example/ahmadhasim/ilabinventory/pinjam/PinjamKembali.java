package com.example.ahmadhasim.ilabinventory.pinjam;

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
 * Created by AHMAD HASIM on 8/29/2016.
 */
public class PinjamKembali extends AppCompatActivity {

    int success;

    private static final String TAG = PinjamKembali.class.getSimpleName();
    private static String url_kembali           = Server.URL + "pinjam_kembali_barang.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    EditText tgl_kembali, txt_id, txt_nama, txt_serial, txt_tgl;
    ImageButton dates;
    Button kembali;
    Calendar now  = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinjam_kembali_form);

        txt_id      = (EditText) findViewById(R.id.txt_id_pinjam);
        txt_nama    = (EditText) findViewById(R.id.txt_nama_pinjam);
        txt_serial  = (EditText) findViewById(R.id.txt_serial_pinjam);
        txt_tgl     = (EditText) findViewById(R.id.txt_tgl_pinjam);
        tgl_kembali = (EditText) findViewById(R.id.txt_tanggal_kembali);
        dates       = (ImageButton) findViewById(R.id.cek_tgl_kembali);
        kembali     = (Button) findViewById(R.id.btn_kembali);

        Intent lp = getIntent();
        final String sub = lp.getStringExtra("sub");
        txt_id.setText(lp.getStringExtra("id"));
        txt_nama.setText(lp.getStringExtra("nama"));
        txt_serial.setText(lp.getStringExtra("serial"));
        txt_tgl.setText(lp.getStringExtra("tgl"));

        tanggal();

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kembali(sub);
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
                tgl_kembali.setText(sdf.format(now.getTime()));
            }
        };

        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PinjamKembali.this, date, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String myformat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tgl_kembali.setText(sdf.format(now.getTime()));
    }

    private void kembali(final String sub){

        final String tgl_back = tgl_kembali.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_kembali, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        Toast.makeText(PinjamKembali.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();

                        Intent lp = new Intent(PinjamKembali.this, PinjamMain.class);
                        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(lp);
                    } else {
                        Toast.makeText(PinjamKembali.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PinjamKembali.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("sedia", "1");
                params.put("sub_stuff_id", sub);
                params.put("tgl_kembali", tgl_back);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), PinjamMain.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), PinjamMain.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
