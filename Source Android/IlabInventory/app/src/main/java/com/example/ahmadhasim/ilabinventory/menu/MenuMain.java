package com.example.ahmadhasim.ilabinventory.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ahmadhasim.ilabinventory.R;
import com.example.ahmadhasim.ilabinventory.hilang.HilangMain;
import com.example.ahmadhasim.ilabinventory.inventaris.InventMain;
import com.example.ahmadhasim.ilabinventory.perbaiki.PerbaikiMain;
import com.example.ahmadhasim.ilabinventory.pinjam.PinjamMain;
import com.example.ahmadhasim.ilabinventory.rusak.RusakMain;

/**
 * Created by AHMAD HASIM on 10/5/2016.
 */
public class MenuMain extends AppCompatActivity {

    RoundImage roundedImage;
    ImageView imageView1;
    LinearLayout bt_rusak, bt_perbaiki, bt_pinjam, bt_hilang;
    TextView name;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User = "userKey";
    public static final String Pass = "passKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_inventory);

        bt_rusak    = (LinearLayout) findViewById(R.id.btn_rusak);
        bt_perbaiki = (LinearLayout) findViewById(R.id.btn_perbaiki);
        bt_pinjam   = (LinearLayout) findViewById(R.id.btn_pinjam);
        bt_hilang   = (LinearLayout) findViewById(R.id.btn_hilang);

        name = (TextView) findViewById(R.id.login_name);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(User)){
            name.setText(sharedpreferences.getString(User,""));
        }

        bt_rusak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MenuMain.this, RusakMain.class);
                startActivity(a);
            }
        });

        bt_perbaiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MenuMain.this, PerbaikiMain.class);
                startActivity(a);
            }
        });

        bt_pinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MenuMain.this, PinjamMain.class);
                startActivity(a);
            }
        });

        bt_hilang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MenuMain.this, HilangMain.class);
                startActivity(a);
            }
        });

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.admin);
        roundedImage = new RoundImage(bm);
        imageView1.setImageDrawable(roundedImage);
    }

    @Override
    public void onBackPressed() {
        Intent lp = new Intent(getApplicationContext(), InventMain.class);
        lp.putExtra("code","1");
        lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(lp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent lp = new Intent(getApplicationContext(), InventMain.class);
                lp.putExtra("code","1");
                lp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lp);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
