package com.example.ahmadhasim.ilabinventory.rusak;

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
 * Created by AHMAD HASIM on 9/16/2016.
 */
public class RusakCari extends Activity {

    ListView list;
    List<RusakData> itemList = new ArrayList<RusakData>();
    RusakAdapter adapter;
    AlertDialog.Builder dialog;

    private static final String TAG = RusakMain.class.getSimpleName();

    private static String url_select            = Server.URL + "rusak_select_cari.php";

    public static final String TAG_ID           = "broken_id";
    public static final String TAG_SUB_ID       = "sub_stuff_id";
    public static final String TAG_BARANG       = "stuff_name";
    public static final String TAG_SERIAL       = "sub_stuff_serial_number";
    public static final String TAG_TGL_RUSAK    = "broken_date";
    public static final String TAG_RUSAK        = "broken_problem";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rusak_cari);

        adapter = new RusakAdapter(RusakCari.this, itemList);

        list = (ListView) findViewById(R.id.list_rusak);
        list.setAdapter(adapter);

        tahan();

        ImageButton ko = (ImageButton) findViewById(R.id.cek_back);
        ko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lp = new Intent(getApplicationContext(), RusakMain.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
            }
        });
        final EditText ok = (EditText) findViewById(R.id.txt_cari_rusak);
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
                        JSONObject obj = jsonArray.getJSONObject(i);

                        RusakData item = new RusakData();

                        item.setId(obj.getString(TAG_ID));
                        item.setSub_id(obj.getString(TAG_SUB_ID));
                        item.setName(obj.getString(TAG_BARANG));
                        item.setSerial(obj.getString(TAG_SERIAL));
                        item.setTglRusak(obj.getString(TAG_TGL_RUSAK));
                        item.setRusak(obj.getString(TAG_RUSAK));

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
                Toast.makeText(RusakCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                final String sub_id = itemList.get(position).getSub_id();

                Log.d("feed", sub);
                final CharSequence[] dialogitem = {"Perbaiki", "Hapus"};
                dialog = new AlertDialog.Builder(RusakCari.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent pk = new Intent(RusakCari.this, RusakPerbaiki.class);
                                pk.putExtra("broken_id", sub);
                                pk.putExtra("sub_stuff_id", sub_id);
                                startActivity(pk);
                                break;
                            case 1:
                                RusakMain rs = new RusakMain();
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
        Intent lp = new Intent(getApplicationContext(), RusakMain.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }
}
