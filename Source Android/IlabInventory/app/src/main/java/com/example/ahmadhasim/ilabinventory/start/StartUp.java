package com.example.ahmadhasim.ilabinventory.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.ahmadhasim.ilabinventory.LoginMain;
import com.example.ahmadhasim.ilabinventory.R;
import com.example.ahmadhasim.ilabinventory.inventaris.InventMain;

/**
 * Created by AHMAD HASIM on 10/1/2016.
 */
public class StartUp extends Activity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User = "userKey";
    public static final String Pass = "passKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(User)){

        }
        if (sharedpreferences.contains(Pass)){
//                    Toast.makeText(LoginMain.this, ""+Pass, Toast.LENGTH_SHORT).show();
        }

        if (sharedpreferences.getString(User,"") == ""){
            Intent lg = new Intent(getApplicationContext(), LoginMain.class);
            lg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(lg);
        }
        else if (sharedpreferences.getString(User, "") != ""){
            Intent mn = new Intent(getApplicationContext(), InventMain.class);
            mn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mn.putExtra("code", "1");
            startActivity(mn);
        }

    }
}
