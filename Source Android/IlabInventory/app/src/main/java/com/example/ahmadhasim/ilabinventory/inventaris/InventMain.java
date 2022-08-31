package com.example.ahmadhasim.ilabinventory.inventaris;

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
import com.example.ahmadhasim.ilabinventory.menu.MenuMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AHMAD HASIM on 8/20/2016.
 */
public class InventMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ListView list;
    SwipeRefreshLayout swipe;
    List<InventData> itemList = new ArrayList<InventData>();
    InventAdapter adapter;
    int success;
    AlertDialog.Builder dialog;
    TextView lokasi, id_stuff;
    ImageButton btn_back;
    String id;
    String ortu;

    private static final String TAG = InventMain.class.getSimpleName();

    private static String url_select        = Server.URL + "inventaris_select.php";
    private static String url_parent        = Server.URL + "inventaris_get_data_back.php";
    private static String url_edit          = Server.URL + "inventaris_get_data.php";
    private static String url_lokasi        = Server.URL + "inventaris_get_data_lokasi.php";
    private static String url_delete        = Server.URL + "inventaris_delete.php";
    private static String url_detail        = Server.URL + "inventaris_detail.php";

    public static final String TAG_ID       = "stuff_id";
    public static final String TAG_NAMA     = "stuff_name";
    public static final String TAG_BRAND    = "stuff_brand";
    public static final String TAG_MODEL    = "stuff_model";

    public static final String TAG_SUB_ID   = "sub_stuff_id";
    public static final String TAG_PARENT   = "parent_id";
    public static final String TAG_SERIAL   = "sub_stuff_serial_number";
    public static final String TAG_KONDISI  = "sub_stuff_condition";
    public static final String TAG_SEDIA    = "sub_stuff_borrow";
    public static final String TAG_TAHUN    = "sub_stuff_year_purchase";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe   = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) findViewById(R.id.list_invent);
        lokasi = (TextView) findViewById(R.id.parent_name);
        id_stuff = (TextView) findViewById(R.id.id_stuff);
        btn_back = (ImageButton) findViewById(R.id.cek_back);

        adapter = new InventAdapter(InventMain.this, itemList);
        list.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ma = new Intent(InventMain.this, InventTambah.class);
                ma.putExtra("parent", ortu);
                ma.putExtra("serial", "");
                startActivity(ma);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sub = id_stuff.getText().toString();
                ortu = sub;
                int ac = Integer.parseInt(sub);

                if (ac == 0){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                if (ac != 0){
                    if (ac == 1){
                        select(sub);
                        select_lokasi(sub);
                    }
                    if (ac != 1){
                        select(sub);
                        select_lokasi(sub);
                    }
                }
            }
        });
//        press();
        tahan_klik();
        banding();

        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                itemList.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void banding(){
        Intent a = getIntent();
        String x = a.getStringExtra("code");
        int b = Integer.parseInt(x);
        if(b == 1){
            final String sub = "1";
            select(sub);
            select_lokasi(sub);
            ortu = sub;
        }else{
            final String sub = x;
            ortu = x;
            select(sub);
            select_lokasi(sub);
        }
    }

    private void tahan_klik(){
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub

                final String idz = itemList.get(position).getId();
                final String sub = itemList.get(position).getSub_id();
                final String par = itemList.get(position).getParent();
                final String idn = itemList.get(position).getSerial();

                ortu = sub;

                Log.d("feed ", idz);
                final CharSequence[] dialogitem = {"Pilih", "Detail", "Lapor Rusak", "Lapor Hilang", "Edit", "Hapus"};
                dialog = new AlertDialog.Builder(InventMain.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                select(sub);
                                select_lokasi(sub);
                                break;
                            case 1:
                                detail(idn);
                                break;
                            case 2:
                                LaporRusak(idn);
                                break;
                            case 3:
                                LaporHilang(idn);
                                break;
                            case 4:
                                edit(idz);
                                break;
                            case 5:
                                delete(sub);
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }

    public void detail(final String idn){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx      = jObj.getString(TAG_SUB_ID);

                        Intent ol = new Intent(InventMain.this, InventDetail.class);
                        ol.putExtra("id", idx);
                        startActivity(ol);

                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(InventMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("serial", idn);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void LaporRusak(final String idn){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx      = jObj.getString(TAG_SUB_ID);
                        String namax    = jObj.getString(TAG_NAMA);
                        String serialx  = jObj.getString(TAG_SERIAL);

                        Intent ol = new Intent(InventMain.this, InventLaporRusak.class);
                        ol.putExtra("id", idx);
                        ol.putExtra("serial", serialx);
                        ol.putExtra("nama", namax);
                        startActivity(ol);

                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(InventMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("serial", idn);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void LaporHilang(final String idn){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String idx      = jObj.getString(TAG_SUB_ID);
                        String namax    = jObj.getString(TAG_NAMA);
                        String serialx  = jObj.getString(TAG_SERIAL);

                        Intent ol = new Intent(InventMain.this, InventLaporHilang.class);
                        ol.putExtra("id", idx);
                        ol.putExtra("serial", serialx);
                        ol.putExtra("nama", namax);
                        startActivity(ol);

                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(InventMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("serial", idn);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public void select(final String sub) {

        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        StringRequest strReq = new StringRequest(Request.Method.POST, url_select, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        InventData item = new InventData();
                        item.setId(jsonObject.getString(TAG_ID));
                        item.setSub_id(jsonObject.getString(TAG_SUB_ID));
                        item.setParent(jsonObject.getString(TAG_PARENT));
                        item.setName(jsonObject.getString(TAG_NAMA));
                        item.setMerk(jsonObject.getString(TAG_BRAND));
                        item.setModel(jsonObject.getString(TAG_MODEL));
                        item.setParent(jsonObject.getString(TAG_PARENT));
                        item.setSerial(jsonObject.getString(TAG_SERIAL));
                        item.setKondisi(jsonObject.getString(TAG_KONDISI));
                        item.setSedia(jsonObject.getString(TAG_SEDIA));
                        item.setTahun(jsonObject.getString(TAG_TAHUN));

                        // menambah item ke array
                        itemList.add(item);
                        //   Toast.makeText(Tampil.this, "stuff_name : "+jsonObject.getString("stuff_name"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    public void select_lokasi(final String sub) {

        int pij = Integer.parseInt(sub);

        if (pij == 1) {
            btn_back.setVisibility(View.GONE);

            StringRequest strReq = new StringRequest(Request.Method.POST, url_lokasi, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        // Cek error node pada json
                        if (success == 1) {
                            Log.d("get edit data", jObj.toString());

                            String parentx  = jObj.getString(TAG_PARENT);
                            String namax    = jObj.getString(TAG_NAMA);
                            id_stuff.setText(parentx);
                            lokasi.setText(namax);

                        } else {
                            Toast.makeText(InventMain.this, "Nama error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

        }

        if (pij != 1){

            btn_back.setVisibility(View.VISIBLE);

            StringRequest strReq = new StringRequest(Request.Method.POST, url_lokasi, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        // Cek error node pada json
                        if (success == 1) {
                            Log.d("get edit data", jObj.toString());

                            String parentx  = jObj.getString(TAG_PARENT);
                            String namax    = jObj.getString(TAG_NAMA);
                            id_stuff.setText(parentx);
                            lokasi.setText(namax);

                        } else {
                            Toast.makeText(InventMain.this, "Lokasi Error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        }
    }

    public void edit(final String idx){
            StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        // Cek error node pada json
                        if (success == 1) {
                            Log.d("get edit data", jObj.toString());
                            String idx      = jObj.getString(TAG_ID);
                            String namax    = jObj.getString(TAG_NAMA);
                            String brandx   = jObj.getString(TAG_BRAND);
                            String modelx   = jObj.getString(TAG_MODEL);
                            String parentx  = jObj.getString(TAG_PARENT);
                            String serialx  = jObj.getString(TAG_SERIAL);
                            String kondisix = jObj.getString(TAG_KONDISI);
                            String sediax   = jObj.getString(TAG_SEDIA);
                            String tahunx   = jObj.getString(TAG_TAHUN);

                            Intent pl = new Intent(InventMain.this, InventEdit.class);
                            pl.putExtra("id", idx);
                            pl.putExtra("nama", namax);
                            pl.putExtra("brand", brandx);
                            pl.putExtra("model", modelx);
                            pl.putExtra("parent", parentx);
                            pl.putExtra("serial", serialx);
                            pl.putExtra("kondisi", kondisix);
                            pl.putExtra("sedia", sediax);
                            pl.putExtra("tahun", tahunx);
                            startActivity(pl);

//                        adapter.notifyDataSetChanged();


                        } else {
                            Toast.makeText(InventMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters ke post url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", idx);

                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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

                                Toast.makeText(InventMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                                onRefresh();

                            } else {
                                Toast.makeText(InventMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(InventMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        banding();
        itemList.clear();
        adapter.notifyDataSetChanged();
//        swipe.setRefreshing(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invent, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        final String sub = id_stuff.getText().toString();
        ortu = sub;
        int ac = Integer.parseInt(sub);

        if (ac == 0){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (ac != 0){
            if (ac == 1){
                select(sub);
                select_lokasi(sub);
            }
            if (ac != 1){
                select(sub);
                select_lokasi(sub);
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent kl = new Intent(InventMain.this, InventCari.class);
                kl.putExtra("parent", ortu);
                startActivity(kl);
                return true;
            case R.id.menu_pin:
                Intent pl = new Intent(InventMain.this, MenuMain.class);
                startActivity(pl);
                return true;
            case R.id.menu_pindah:
                Intent lp = new Intent(InventMain.this, InventPindahBarcode1.class);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
