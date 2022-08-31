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
 * Created by AHMAD HASIM on 9/9/2016.
 */
public class RusakAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<RusakData> items;

    public RusakAdapter(Activity activity, List<RusakData> items) {
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
            convertView = inflater.inflate(R.layout.rusak_list_row, null);

        TextView id         = (TextView) convertView.findViewById(R.id.rusak_id);
        TextView name       = (TextView) convertView.findViewById(R.id.rusak_name);
        TextView serial     = (TextView) convertView.findViewById(R.id.rusak_serial);
        TextView tgl_rusak  = (TextView) convertView.findViewById(R.id.rusak_tanggal);
        TextView kerusakan  = (TextView) convertView.findViewById(R.id.rusak_kerusakan);
        TextView sub_id     = (TextView) convertView.findViewById(R.id.rusak_sub_id);


        RusakData data = items.get(position);

        id.setText(data.getId());
        name.setText(data.getName());
        serial.setText(data.getSerial());
        tgl_rusak.setText(": "+data.getTglRusak());
        kerusakan.setText(": "+data.getRusak());
        sub_id.setText(data.getSub_id());

        return convertView;
    }
}
