package com.example.ahmadhasim.ilabinventory.hilang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
 * Created by AHMAD HASIM on 10/5/2016.
 */
public class HilangRiwayatCari extends Activity {

    ListView list;
    List<HilangData> itemList = new ArrayList<HilangData>();
    HilangRiwayatAdapter adapter;
    AlertDialog.Builder dialog;

    private static final String TAG = HilangMain.class.getSimpleName();

    private static String url_select            = Server.URL + "hilang_select_riwayat_cari.php";

    public static final String TAG_ID           = "found_id";
    public static final String TAG_TGL_KETEMU   = "found_date";
    public static final String TAG_NOTE         = "found_note";
    public static final String TAG_BARANG       = "stuff_name";
    public static final String TAG_SERIAL       = "sub_stuff_serial_number";
    public static final String TAG_TGL_HILANG   = "broken_date";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hilang_riwayat_cari);

        adapter = new HilangRiwayatAdapter(HilangRiwayatCari.this, itemList);

        list = (ListView) findViewById(R.id.list_riwayat_hilang);
        list.setAdapter(adapter);

        ImageButton ko = (ImageButton) findViewById(R.id.cek_back);
        ko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lp = new Intent(getApplicationContext(), HilangRiwayat.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
            }
        });

        final EditText ok = (EditText) findViewById(R.id.txt_cari_hilang);
        ok.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String idz = ok.getText().toString();
                    select(idz);
                }
                return false;
            }
        });

        tahan();
    }

    private void select(final String idz) {

        itemList.clear();
        adapter.notifyDataSetChanged();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        HilangData item = new HilangData();

                        item.setId(jsonObject.getString(TAG_ID));
                        item.setTglKetemu(jsonObject.getString(TAG_TGL_KETEMU));
                        item.setNote(jsonObject.getString(TAG_NOTE));
                        item.setName(jsonObject.getString(TAG_BARANG));
                        item.setSerial(jsonObject.getString(TAG_SERIAL));
                        item.setTglHilang(jsonObject.getString(TAG_TGL_HILANG));

                        itemList.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(HilangRiwayatCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", idz);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void tahan(){
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String sub = itemList.get(position).getId();

                Log.d("feed", sub);
                final CharSequence[] dialogitem = {"Hapus"};
                dialog = new AlertDialog.Builder(HilangRiwayatCari.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                HilangRiwayat rs = new HilangRiwayat();
                                rs.delete(sub);
                                break;
                        }
                    }
                }).show();

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), HilangRiwayat.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }
}
