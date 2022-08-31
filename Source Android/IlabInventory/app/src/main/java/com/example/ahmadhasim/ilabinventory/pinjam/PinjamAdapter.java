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
 * Created by AHMAD HASIM on 8/25/2016.
 */
public class PinjamAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<PinjamData> items;

    public PinjamAdapter(Activity activity, List<PinjamData> items) {
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
            convertView = inflater.inflate(R.layout.pinjam_list_row, null);

        TextView id         = (TextView) convertView.findViewById(R.id.pinjam_id);
        TextView nama       = (TextView) convertView.findViewById(R.id.pinjam_name);
        TextView tanggal    = (TextView) convertView.findViewById(R.id.pinjam_tanggal);
        TextView sub_id     = (TextView) convertView.findViewById(R.id.pinjam_sub_id);
        TextView barang     = (TextView) convertView.findViewById(R.id.pinjam_barang);
        TextView serial     = (TextView) convertView.findViewById(R.id.pinjam_serial);
        TextView catatan    = (TextView) convertView.findViewById(R.id.pinjam_catatan);

        PinjamData data = items.get(position);

        id.setText(data.getId());
        nama.setText(data.getPeminjam());
        tanggal.setText(": "+data.getTgl_pinjam());
        sub_id.setText(data.getSub_id());
        barang.setText(": " + data.getBarang());
        serial.setText(data.getSerial());
        catatan.setText(" : "+data.getCatatan());

        return convertView;
    }
}
