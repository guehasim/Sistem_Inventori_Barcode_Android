package com.example.ahmadhasim.ilabinventory.pinjam;

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
 * Created by AHMAD HASIM on 8/29/2016.
 */
public class PinjamRiwayat extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    SwipeRefreshLayout swipe;
    List<PinjamData> itemList = new ArrayList<PinjamData>();
    PinjamRiwayatAdapter adapter;
    AlertDialog.Builder dialog;

    int success;

    private static final String TAG = PinjamRiwayat.class.getSimpleName();

    private static String url_select            = Server.URL + "pinjam_riwayat.php";
    private static String url_delete            = Server.URL + "pinjam_delete_riwayat.php";

    public static final String TAG_ID           = "borrow_id";
    public static final String TAG_SUB_ID       = "sub_stuff_id";
    public static final String TAG_BARANG       = "stuff_name";
    public static final String TAG_SERIAL       = "sub_stuff_serial_number";
    public static final String TAG_PEMINJAM     = "borrow_borrower";
    public static final String TAG_TGL_START    = "borrow_date";
    public static final String TAG_TGL_FINISH   = "borrow_return_date";
    public static final String TAG_NOTE         = "borrow_note";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinjam_riwayat);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_riwayat);

        adapter = new PinjamRiwayatAdapter(PinjamRiwayat.this, itemList);

        list = (ListView) findViewById(R.id.list_riwayat);
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

    private void tahan(){
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String idn = itemList.get(position).getId();

                Log.d("feed", idn);
                final CharSequence[] dialoitem = {"Hapus"};
                dialog = new AlertDialog.Builder(PinjamRiwayat.this);
                dialog.setCancelable(true);
                dialog.setItems(dialoitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                hapus(idn);
                                break;
                        }
                    }
                }).show();

                return false;
            }
        });
    }

    public void hapus(final String idn){

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
                                select();
                                Toast.makeText(PinjamRiwayat.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(PinjamRiwayat.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(PinjamRiwayat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters ke post url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", idn);

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

                        PinjamData item = new PinjamData();

                        item.setId(obj.getString(TAG_ID));
                        item.setBarang(obj.getString(TAG_BARANG));
                        item.setSerial(obj.getString(TAG_SERIAL));
                        item.setTgl_pinjam(obj.getString(TAG_TGL_START));
                        item.setTgl_kembali(obj.getString(TAG_TGL_FINISH));
                        item.setPeminjam(obj.getString(TAG_PEMINJAM));
                        item.setCatatan(obj.getString(TAG_NOTE));

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

    @Override
    public void onRefresh() {
        select();
        itemList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.search:
                Intent pl = new Intent(PinjamRiwayat.this, PinjamRiwayatCari.class);
                startActivity(pl);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
