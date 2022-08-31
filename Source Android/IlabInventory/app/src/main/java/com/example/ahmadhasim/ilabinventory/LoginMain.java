package com.example.ahmadhasim.ilabinventory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmadhasim.ilabinventory.controller.AppController;
import com.example.ahmadhasim.ilabinventory.controller.Server;
import com.example.ahmadhasim.ilabinventory.inventaris.InventMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AHMAD HASIM on 8/20/2016.
 */
public class LoginMain extends Activity {

    int success;
    private static final String TAG = LoginMain.class.getSimpleName();

    private static String url_get = Server.URL + "login_select.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User = "userKey";
    public static final String Pass = "passKey";
    SharedPreferences sharedpreferences;

    EditText txt_user, txt_pass;
    Button btn_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        txt_user = (EditText)findViewById(R.id.username);
        txt_pass = (EditText)findViewById(R.id.password);
        btn_log = (Button)findViewById(R.id.btn_login);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_data();
            }
        });

    }

    private void get_data(){

        final String usr = txt_user.getText().toString();
        final String pas = txt_pass.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_get, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());

                        Intent invent = new Intent(LoginMain.this, InventMain.class);
                        invent.putExtra("code","1");
                        startActivity(invent);

                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(User, usr);
                        editor.putString(Pass, pas);
                        editor.commit();

                    } else {
                        Toast.makeText(LoginMain.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginMain.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", usr);
                params.put("pass", pas);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

