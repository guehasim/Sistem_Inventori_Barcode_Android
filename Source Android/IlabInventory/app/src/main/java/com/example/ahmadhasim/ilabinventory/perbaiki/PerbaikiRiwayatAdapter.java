package com.example.ahmadhasim.ilabinventory.perbaiki;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmadhasim.ilabinventory.R;

import java.util.List;

/**
 * Created by AHMAD HASIM on 9/17/2016.
 */
public class PerbaikiRiwayatAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PerbaikiData> items;

    public PerbaikiRiwayatAdapter(Activity activity, List<PerbaikiData> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.perbaiki_riwayat_list_row, null);

        TextView id         = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_id);
        TextView name       = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_name);
        TextView serial     = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_serial);
        TextView tgl_rusak  = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_tanggal_rusak);
        TextView kerusakan  = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_kerusakan);
        TextView tgl_repair = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_tanggal_perbaikan);
        TextView repairer   = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_yg_perbaiki);
        TextView tgl_fix    = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_tanggal_selesai);
        TextView kondisi    = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_kondisi);
        TextView note       = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_catatan);
        TextView broken_id  = (TextView) convertView.findViewById(R.id.perbaiki_riwayat_broken_id);


        PerbaikiData data = items.get(position);

        id.setText(data.getId());
        name.setText(data.getName());
        serial.setText(data.getSerial());
        tgl_rusak.setText(": "+data.getTglRusak());
        kerusakan.setText(" : "+data.getRusak());
        tgl_repair.setText(": "+data.getTglPerbaiki());
        repairer.setText(": "+data.getYgPerbaiki());
        tgl_fix.setText(": "+data.getTglSelesai());

        String pkl = data.getStlPerbaikan();
        int pk = Integer.parseInt(pkl);

        if(pk == 1){
            kondisi.setText(" : Baik");
            kondisi.setBackgroundResource(R.color.k_baik);
        }
        else if(pk == 2){
            kondisi.setText(" : Tidak Baik");
            kondisi.setBackgroundResource(R.color.k_t_baik);
        }
        else if(pk == 3){
            kondisi.setText(" : Rusak");
            kondisi.setBackgroundResource(R.color.k_rusak);
        }
        else if(pk == 4){
            kondisi.setText(" : Hancur");
            kondisi.setBackgroundResource(R.color.k_hancur);
        }
        else{
            kondisi.setText(" : kosong");
        }

        note.setText(" : "+data.getNote());
        broken_id.setText(data.getBrokenId());

        return convertView;
    }
}
