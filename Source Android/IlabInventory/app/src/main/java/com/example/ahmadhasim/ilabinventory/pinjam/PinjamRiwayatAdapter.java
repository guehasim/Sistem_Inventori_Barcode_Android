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
 * Created by AHMAD HASIM on 8/29/2016.
 */
public class PinjamRiwayatAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PinjamData> items;

    public PinjamRiwayatAdapter(Activity activity, List<PinjamData> items) {
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
            convertView = inflater.inflate(R.layout.pinjam_list_row_riwayat, null);

        TextView id             = (TextView) convertView.findViewById(R.id.rw_id);
        TextView barang         = (TextView) convertView.findViewById(R.id.rw_barang);
        TextView serial         = (TextView) convertView.findViewById(R.id.rw_serial);
        TextView tgl_start      = (TextView) convertView.findViewById(R.id.rw_tgl_pinjam);
        TextView tgl_kembali    = (TextView) convertView.findViewById(R.id.rw_tgl_kembali);
        TextView peminjam       = (TextView) convertView.findViewById(R.id.rw_peminjam);
        TextView catatan        = (TextView) convertView.findViewById(R.id.rw_note);

        PinjamData data = items.get(position);

        id.setText(data.getId());
        barang.setText(data.getBarang());
        serial.setText(data.getSerial());
        tgl_start.setText(": "+data.getTgl_pinjam());
        tgl_kembali.setText(": "+data.getTgl_kembali());
        peminjam.setText(": "+data.getPeminjam());
        catatan.setText(" : "+data.getCatatan());

        return convertView;
    }
}
