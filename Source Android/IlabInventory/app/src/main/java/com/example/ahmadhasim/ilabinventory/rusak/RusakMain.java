package com.example.ahmadhasim.ilabinventory.rusak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.ahmadhasim.ilabinventory.menu.MenuMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AHMAD HASIM on 9/2/2016.
 */
public class RusakMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    int success;
    ListView list;
    SwipeRefreshLayout swipe;
    List<RusakData> itemList = new ArrayList<RusakData>();
    RusakAdapter adapter;
    AlertDialog.Builder dialog;
    FloatingActionButton fab;

    private static final String TAG = RusakMain.class.getSimpleName();

    private static String url_select            = Server.URL + "rusak_select.php";
    private static String url_delete            = Server.URL + "rusak_delete.php";

    public static final String TAG_ID           = "broken_id";
    public static final String TAG_SUB_ID       = "sub_stuff_id";
    public static final String TAG_BARANG       = "stuff_name";
    public static final String TAG_SERIAL       = "sub_stuff_serial_number";
    public static final String TAG_TGL_RUSAK    = "broken_date";
    public static final String TAG_RUSAK        = "broken_problem";
    private static final String TAG_SUCCESS     = "success";
    private static final String TAG_MESSAGE     = "message";

    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rusak_main);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_rusak_main);
        fab     = (FloatingActionButton) findViewById(R.id.fab);
        adapter = new RusakAdapter(RusakMain.this, itemList);

        list = (ListView) findViewById(R.id.list_rusak);
        list.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(RusakMain.this, RusakTambah.class);
                startActivity(a);
            }
        });

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

                        RusakData item = new RusakData();

                        item.setId(obj.getString(TAG_ID));
                        item.setSub_id(obj.getString(TAG_SUB_ID));
                        item.setName(obj.getString(TAG_BARANG));
                        item.setSerial(obj.getString(TAG_SERIAL));
                        item.setTglRusak(obj.getString(TAG_TGL_RUSAK));
                        item.setRusak(obj.getString(TAG_RUSAK));

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

                final String sub, sub_id, namex, serialx;

                sub     = itemList.get(position).getId();
                sub_id  = itemList.get(position).getSub_id();
                namex   = itemList.get(position).getName();
                serialx = itemList.get(position).getSerial();

                Log.d("feed", sub);
                final CharSequence[] dialogitem = {"Perbaiki", "Hapus"};
                dialog = new AlertDialog.Builder(RusakMain.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent pk = new Intent(RusakMain.this, RusakPerbaiki.class);
                                pk.putExtra("broken_id", sub);
                                pk.putExtra("sub_stuff_id", sub_id);
                                pk.putExtra("nama", namex);
                                pk.putExtra("serial", serialx);
                                startActivity(pk);
                                break;
                            case 1:
                                delete(sub);
                                break;
                        }
                    }
                }).show();

                return false;
            }
        });
    }

    public void delete(final String sub){

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

                                Toast.makeText(RusakMain.this, "Inventaris Yang Rusak Telah Dihapus", Toast.LENGTH_SHORT).show();
                                itemList.clear();
                                adapter.notifyDataSetChanged();
                                select();

                            } else {
                                Toast.makeText(RusakMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(RusakMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters ke post url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", sub);

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
    public boolean onCreateOptionsMenu (Menu menu){
        // menambahkan icon menu pada toolbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), MenuMain.class);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){

        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), MenuMain.class);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            case R.id.search:
                Intent kl = new Intent(RusakMain.this, RusakCari.class);
                startActivity(kl);
                return true;
            case R.id.history:
                Intent pl = new Intent(getApplicationContext(), RusakRiwayat.class);
                startActivity(pl);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
