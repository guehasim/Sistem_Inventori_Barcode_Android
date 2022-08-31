package com.example.ahmadhasim.ilabinventory.inventaris;

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
public class InventCari extends Activity {

    ListView list;
    List<InventData> itemList = new ArrayList<InventData>();
    InventAdapter adapter;
    AlertDialog.Builder dialog;
    String id, ortu;
    String parent_id;

    int success;

    private static final String TAG = InventCari.class.getSimpleName();

    private static String url_select        = Server.URL + "inventaris_select_cari.php";
    private static String url_edit          = Server.URL + "inventaris_get_data.php";
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
        setContentView(R.layout.inventaris_cari);

        list    = (ListView) findViewById(R.id.list_invent);
        adapter = new InventAdapter(InventCari.this, itemList);
        list.setAdapter(adapter);

        Intent ab = getIntent();
        parent_id = ab.getStringExtra("parent");

        tahan_klik();

        ImageButton ko = (ImageButton) findViewById(R.id.cek_back);
        ko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lp = new Intent(getApplicationContext(), InventMain.class);
                lp.putExtra("code", parent_id);
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
            }
        });
        final EditText ok = (EditText) findViewById(R.id.txt_cari_inventaris);
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
                Toast.makeText(InventCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", idz);
                params.put("id", parent_id);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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

                ortu = idz;

                Log.d("feed ", idz);
                final CharSequence[] dialogitem = { "Detail", "Lapor Rusak", "Lapor Hilang", "Edit", "Hapus"};
                dialog = new AlertDialog.Builder(InventCari.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        switch (which) {
                            case 0:
                                detail(idn);
                                break;
                            case 1:
                                LaporRusak(idn);
                                break;
                            case 2:
                                LaporHilang(idn);
                                break;
                            case 3:
                                edit(idz);
                                break;
                            case 4:
                                delete(idz);
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

                        Toast.makeText(InventCari.this, idx, Toast.LENGTH_SHORT).show();

                        Intent ol = new Intent(InventCari.this, InventDetail.class);
                        ol.putExtra("id", idx);
                        startActivity(ol);

                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(InventCari.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

                        Intent ol = new Intent(InventCari.this, InventLaporRusak.class);
                        ol.putExtra("id", idx);
                        startActivity(ol);

                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(InventCari.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(InventCari.this, idx, Toast.LENGTH_SHORT).show();

                        Intent ol = new Intent(InventCari.this, InventLaporHilang.class);
                        ol.putExtra("id", idx);
                        startActivity(ol);

                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(InventCari.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

                        Intent pl = new Intent(InventCari.this, InventEdit.class);
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
                        Toast.makeText(InventCari.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InventCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void delete(final String idz){

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

                                final String idz;
                                idz = ortu;

                                Toast.makeText(InventCari.this, idz, Toast.LENGTH_SHORT).show();
                                select(idz);

                                Toast.makeText(InventCari.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(InventCari.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(InventCari.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters ke post url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", idz);

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
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), InventMain.class);
        lp.putExtra("code", parent_id);
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }
}
