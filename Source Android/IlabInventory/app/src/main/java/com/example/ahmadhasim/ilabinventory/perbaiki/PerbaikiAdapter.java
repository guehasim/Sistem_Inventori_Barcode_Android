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
 * Created by AHMAD HASIM on 9/16/2016.
 */
public class PerbaikiAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PerbaikiData> items;

    public PerbaikiAdapter(Activity activity, List<PerbaikiData> items) {
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
            convertView = inflater.inflate(R.layout.perbaiki_list_row, null);

        TextView id         = (TextView) convertView.findViewById(R.id.perbaiki_id);
        TextView name       = (TextView) convertView.findViewById(R.id.perbaiki_name);
        TextView serial     = (TextView) convertView.findViewById(R.id.perbaiki_serial);
        TextView tgl_rusak  = (TextView) convertView.findViewById(R.id.perbaiki_tanggal_rusak);
        TextView kerusakan  = (TextView) convertView.findViewById(R.id.perbaiki_kerusakan);
        TextView tgl_repair = (TextView) convertView.findViewById(R.id.perbaiki_tanggal_perbaikan);
        TextView repairer   = (TextView) convertView.findViewById(R.id.perbaiki_yg_perbaiki);
        TextView broken_id  = (TextView) convertView.findViewById(R.id.perbaiki_broken_id);
        TextView sub_id     = (TextView) convertView.findViewById(R.id.txt_sub_stuff_id);

        PerbaikiData data = items.get(position);

        id.setText(data.getId());
        name.setText(data.getName());
        serial.setText(data.getSerial());
        tgl_rusak.setText(" : "+data.getTglRusak());
        kerusakan.setText(" : "+data.getRusak());
        tgl_repair.setText(": "+data.getTglPerbaiki());
        repairer.setText(": "+data.getYgPerbaiki());
        broken_id.setText(data.getBrokenId());
        sub_id.setText(data.getSub_id());

        return convertView;
    }
}
