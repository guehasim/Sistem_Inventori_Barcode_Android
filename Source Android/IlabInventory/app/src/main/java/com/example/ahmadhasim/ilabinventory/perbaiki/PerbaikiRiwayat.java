package com.example.ahmadhasim.ilabinventory.perbaiki;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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
public class PerbaikiRiwayat extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    int success;
    ListView list;
    SwipeRefreshLayout swipe;
    List<PerbaikiData> itemList = new ArrayList<PerbaikiData>();
    PerbaikiRiwayatAdapter adapter;
    AlertDialog.Builder dialog;

    private static final String TAG = PerbaikiMain.class.getSimpleName();

    private static String url_select            = Server.URL + "perbaiki_riwayat_select.php";
    private static String url_delete            = Server.URL + "perbaiki_riwayat_delete.php";

    public static final String TAG_ID           = "repair_id";
    public static final String TAG_START_REPAIR = "repair_start_date";
    public static final String TAG_DONE_REPAIR  = "repair_done_date";
    public static final String TAG_BROKEN_ID    = "broken_id";
    public static final String TAG_KONDISI      = "repair_condition_result";
    public static final String TAG_REPAIRER     = "repair_repairer";
    public static final String TAG_NOTE         = "repair_note";
    public static final String TAG_SUB_ID       = "sub_stuff_id";
    public static final String TAG_PROBLEM      = "broken_problem";
    public static final String TAG_BROKEN_DATE  = "broken_date";
    public static final String TAG_SERIAL       = "sub_stuff_serial_number";
    public static final String TAG_BARANG       = "stuff_name";
    private static final String TAG_SUCCESS     = "success";
    private static final String TAG_MESSAGE     = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perbaiki_riwayat);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_perbaiki_riwayat_main);


        adapter = new PerbaikiRiwayatAdapter(PerbaikiRiwayat.this, itemList);

        list = (ListView) findViewById(R.id.list_riwayat_perbaiki);
        list.setAdapter(adapter);

        tahan();

        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                itemList.clear();
                adapter.notifyDataSetChanged();
                select();
            }
        });
    }

    private void select(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        PerbaikiData item = new PerbaikiData();

                        item.setId(obj.getString(TAG_ID));
                        item.setName(obj.getString(TAG_BARANG));
                        item.setSerial(obj.getString(TAG_SERIAL));
                        item.setTglRusak(obj.getString(TAG_BROKEN_DATE));
                        item.setRusak(obj.getString(TAG_PROBLEM));
                        item.setTglPerbaiki(obj.getString(TAG_START_REPAIR));
                        item.setYgPerbaiki(obj.getString(TAG_REPAIRER));
                        item.setTglSelesai(obj.getString(TAG_DONE_REPAIR));
                        item.setBrokenID(obj.getString(TAG_BROKEN_ID));
                        item.setStlPerbaikan(obj.getString(TAG_KONDISI));
                        item.setNote(obj.getString(TAG_NOTE));

                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        AppController.getInstance().addToRequestQueue(jArr);
    }

    private void tahan(){
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String rp = itemList.get(position).getId();

                Log.d("feed", rp);
                final CharSequence[] dialogitem = {"Hapus"};
                dialog = new AlertDialog.Builder(PerbaikiRiwayat.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                delete(rp);
                                break;
                        }
                    }
                }).show();

                return false;
            }
        });
    }

    public void delete(final String rp){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Hapus");
        builder.setMessage("Anda yakin data ini dihapus?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.d("delete", jObj.toString());

                                Toast.makeText(PerbaikiRiwayat.this, "Riwayat Inventaris Yang Diperbaiki Telah Dihapus", Toast.LENGTH_SHORT).show();
                                itemList.clear();
                                adapter.notifyDataSetChanged();
                                select();

                            } else {
                                Toast.makeText(PerbaikiRiwayat.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(PerbaikiRiwayat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters ke post url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", rp);

                        return params;
                    }

                };

                AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //isi
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        select();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), PerbaikiMain.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), PerbaikiMain.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            case R.id.search:
                Intent pl = new Intent(PerbaikiRiwayat.this, PerbaikiRiwayatCari.class);
                startActivity(pl);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
