package com.example.ahmadhasim.ilabinventory.hilang;

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
 * Created by AHMAD HASIM on 9/15/2016.
 */
public class HilangRiwayatAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<HilangData> items;

    public HilangRiwayatAdapter(Activity activity, List<HilangData> items) {
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
            convertView = inflater.inflate(R.layout.hilang_riwayat_list_row, null);

        TextView id         = (TextView) convertView.findViewById(R.id.hilang_riwayat_id);
        TextView name       = (TextView) convertView.findViewById(R.id.hilang_riwayat_name);
        TextView serial     = (TextView) convertView.findViewById(R.id.hilang_riwayat_serial);
        TextView tgl_hilang = (TextView) convertView.findViewById(R.id.hilang_riwayat_tanggal_hilang);
        TextView tgl_ketemu = (TextView) convertView.findViewById(R.id.hilang_riwayat_tanggal_ketemu);
        TextView note       = (TextView) convertView.findViewById(R.id.hilang_riwayat_note);


        HilangData data = items.get(position);

        id.setText(data.getId());
        name.setText(data.getName());
        serial.setText(data.getSerial());
        tgl_hilang.setText(": "+data.getTglHilang());
        tgl_ketemu.setText(": "+data.getTglKetemu());
        note.setText(": "+data.getNote());

        return convertView;
    }
}
