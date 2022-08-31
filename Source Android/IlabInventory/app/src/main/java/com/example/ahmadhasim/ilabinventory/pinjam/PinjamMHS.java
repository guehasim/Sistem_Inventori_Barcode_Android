package com.example.ahmadhasim.ilabinventory.pinjam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ahmadhasim.ilabinventory.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AHMAD HASIM on 8/26/2016.
 */
public class PinjamMHS extends AppCompatActivity {

    EditText id, tgl_st, note;
    Button pindah;
    ImageButton img_tgl;
    final Calendar now = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinjam_form);

        id      = (EditText) findViewById(R.id.jancok);
        tgl_st  = (EditText) findViewById(R.id.txt_tgl_start);
        note    = (EditText) findViewById(R.id.txt_note);
        img_tgl = (ImageButton) findViewById(R.id.cek_tgl_start);
        pindah  = (Button) findViewById(R.id.button_tambah_org);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, monthOfYear);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myformat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
                tgl_st.setText(sdf.format(now.getTime()));
            }
        };

        img_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PinjamMHS.this, date, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String myformat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myformat, Locale.US);
        tgl_st.setText(sdf.format(now.getTime()));


        pindah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nim = id.getText().toString();
                String tgl = tgl_st.getText().toString();
                String not = note.getText().toString();

                if (nim.isEmpty()){
                    Toast.makeText(PinjamMHS.this, "Kolom Identitas Tidak Boleh Kosong!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent a = new Intent(PinjamMHS.this, PinjamBarang.class);
                    a.putExtra("id", nim);
                    a.putExtra("tanggal", tgl);
                    a.putExtra("note", not);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                }

            }
        });
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
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
