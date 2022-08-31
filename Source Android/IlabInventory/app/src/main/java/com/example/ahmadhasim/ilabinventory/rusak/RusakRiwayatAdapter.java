package com.example.ahmadhasim.ilabinventory.rusak;

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
 * Created by AHMAD HASIM on 9/16/2016.
 */
public class RusakRiwayatAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<RusakData> items;

    public RusakRiwayatAdapter(Activity activity, List<RusakData> items) {
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
            convertView = inflater.inflate(R.layout.rusak_riwayat_list_row, null);

        TextView id             = (TextView) convertView.findViewById(R.id.rusak_riwayat_id);
        TextView name           = (TextView) convertView.findViewById(R.id.rusak_riwayat_name);
        TextView serial         = (TextView) convertView.findViewById(R.id.rusak_riwayat_serial);
        TextView tgl_rusak      = (TextView) convertView.findViewById(R.id.rusak_riwayat_tanggal_rusak);
        TextView tgl_perbaiki   = (TextView) convertView.findViewById(R.id.rusak_riwayat_tanggal_perbaikan);
        TextView repirer        = (TextView) convertView.findViewById(R.id.rusak_riwayat_yg_perbaiki);
        TextView kerusakan      = (TextView) convertView.findViewById(R.id.rusak_riwayat_kerusakan);
        TextView broken_id      = (TextView) convertView.findViewById(R.id.rusak_riwayat_broken_id);


        RusakData data = items.get(position);

        id.setText(data.getId());
        name.setText(data.getName());
        serial.setText(data.getSerial());
        tgl_rusak.setText(" : "+data.getTglRusak());
        tgl_perbaiki.setText(": "+data.getTglPerbaiki());
        repirer.setText(": "+data.getYgPerbaiki());
        kerusakan.setText(" : "+data.getRusak());
        broken_id.setText(data.getBrokenId());

        return convertView;
    }
}
