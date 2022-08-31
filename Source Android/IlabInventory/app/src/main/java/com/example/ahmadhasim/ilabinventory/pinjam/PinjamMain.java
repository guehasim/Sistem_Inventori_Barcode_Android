package com.example.ahmadhasim.ilabinventory.pinjam;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ahmadhasim.ilabinventory.R;
import com.example.ahmadhasim.ilabinventory.controller.AppController;
import com.example.ahmadhasim.ilabinventory.controller.Server;
import com.example.ahmadhasim.ilabinventory.menu.MenuMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AHMAD HASIM on 8/20/2016.
 */
public class PinjamMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    FloatingActionButton fab;

    int success;
    ListView list;
    SwipeRefreshLayout swipe;
    List<PinjamData> itemList = new ArrayList<PinjamData>();
    PinjamAdapter adapter;
    AlertDialog.Builder dialog;

    private static final String TAG = PinjamMain.class.getSimpleName();

    private static String url_select            = Server.URL + "pinjam_select.php";

    public static final String TAG_ID           = "borrow_id";
    public static final String TAG_SUB_ID       = "sub_stuff_id";
    public static final String TAG_BARANG       = "stuff_name";
    public static final String TAG_SERIAL       = "sub_stuff_serial_number";
    public static final String TAG_PEMINJAM     = "borrow_borrower";
    public static final String TAG_TGL_START    = "borrow_date";
    public static final String TAG_NOTE         = "borrow_note";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinjam_main);

        fab     = (FloatingActionButton) findViewById(R.id.fab);
        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_pinjam_main);

        adapter = new PinjamAdapter(PinjamMain.this, itemList);

        list = (ListView) findViewById(R.id.list_pinjam);
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warning();
            }
        });
    }

    private void tahan(){
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String sub, idx, namax, serialx, tglx;

                sub     = itemList.get(position).getSub_id();
                idx     = itemList.get(position).getPeminjam();
                namax   = itemList.get(position).getBarang();
                serialx = itemList.get(position).getSerial();
                tglx    = itemList.get(position).getTgl_pinjam();

                Log.d("feed",sub);
                final CharSequence[] dialogitem = {"Kembali"};
                dialog = new AlertDialog.Builder(PinjamMain.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent pk = new Intent(PinjamMain.this, PinjamKembali.class);
                                pk.putExtra("sub", sub);
                                pk.putExtra("id", idx);
                                pk.putExtra("nama", namax);
                                pk.putExtra("serial", serialx);
                                pk.putExtra("tgl", tglx);
                                startActivity(pk);
                                break;
                        }
                    }
                }).show();

                return false;
            }
        });
    }

    private void warning(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Penyimpanan");
        builder.setMessage("Pilih Salah Satu Fitur !");
        builder.setPositiveButton("MANUAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent bc = new Intent(PinjamMain.this, PinjamMHS.class);
                startActivity(bc);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("BARCODE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent cb = new Intent(PinjamMain.this, BarcodeKTM.class);
                startActivity(cb);
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
                        item.setTgl_pinjam(obj.getString(TAG_TGL_START));
                        item.setBarang(obj.getString(TAG_BARANG));
                        item.setSerial(obj.getString(TAG_SERIAL));
                        item.setPeminjam(obj.getString(TAG_PEMINJAM));
                        item.setSub_id(obj.getString(TAG_SUB_ID));
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

        itemList.clear();
        adapter.notifyDataSetChanged();
        select();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
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
                Intent pk = new Intent(PinjamMain.this, PinjamCari.class);
                startActivity(pk);
                return true;
            case R.id.history:
                Intent pl = new Intent(getApplicationContext(), PinjamRiwayat.class);
                startActivity(pl);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
