package com.example.ahmadhasim.ilabinventory.pinjam;

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
 * Created by AHMAD HASIM on 9/20/2016.
 */
public class PinjamManualAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PinjamData> items;

    public PinjamManualAdapter(Activity activity, List<PinjamData> items) {
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
            convertView = inflater.inflate(R.layout.pinjam_list_row_barang, null);

        TextView nama       = (TextView) convertView.findViewById(R.id.pinjam_name_bar);
        TextView serial     = (TextView) convertView.findViewById(R.id.pinjam_serial_bar);

        PinjamData data = items.get(position);

        nama.setText(data.getBarang());
        serial.setText(data.getSerial());

        return convertView;
    }
}
