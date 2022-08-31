package com.example.ahmadhasim.ilabinventory.inventaris;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmadhasim.ilabinventory.R;

import java.util.List;

/**
 * Created by AHMAD HASIM on 8/20/2016.
 */
public class InventAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<InventData> items;

    public InventAdapter(Activity activity, List<InventData> items) {
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
            convertView = inflater.inflate(R.layout.inventaris_list_row, null);

        TextView id         = (TextView) convertView.findViewById(R.id.stuff_id);
        TextView sub_id     = (TextView) convertView.findViewById(R.id.sub_stuff_id);
        TextView parent_id  = (TextView) convertView.findViewById(R.id.parent_id);
        TextView name       = (TextView) convertView.findViewById(R.id.stuff_name);
        TextView merk       = (TextView) convertView.findViewById(R.id.stuff_merk);
        TextView model      = (TextView) convertView.findViewById(R.id.stuff_model);
        TextView parents    = (TextView) convertView.findViewById(R.id.stuff_parent);
        TextView serial     = (TextView) convertView.findViewById(R.id.stuff_serial);
        TextView kondisi    = (TextView) convertView.findViewById(R.id.stuff_kondisi);
        TextView sedia      = (TextView) convertView.findViewById(R.id.stuff_sedia);
        TextView tahun      = (TextView) convertView.findViewById(R.id.stuff_tahun);

        InventData data = items.get(position);

        String a = data.getKondisi();
        int b = Integer.parseInt(a);

        String c = data.getSedia();
        int d = Integer.parseInt(c);

        id.setText(data.getId());
        sub_id.setText(data.getSub_id());
        parent_id.setText(data.getParent());
        name.setText(data.getName());

        if (data.getMerk().isEmpty()){
            merk.setText("  : -");
        }
        else if (data.getMerk() == "null"){
            merk.setText("  : -");
        }
        else {
            merk.setText("  : "+data.getMerk());
        }

        if (data.getModel().isEmpty()){
            model.setText("  : -");
        }
        else if(data.getModel() == "null"){
            model.setText("  : -");
        }
        else {
            model.setText(" : "+data.getModel());
        }

        parents.setText(data.getParent());
        serial.setText(data.getSerial());
        if(b == 1){
            kondisi.setText(": Baik            ");
            kondisi.setBackgroundResource(R.color.k_baik);
        }
        else if(b == 2){
            kondisi.setText(": Tidak Baik");
            kondisi.setBackgroundResource(R.color.k_t_baik);
        }
        else if(b == 3){
            kondisi.setText(": Rusak           ");
            kondisi.setBackgroundResource(R.color.k_rusak);
            kondisi.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else if(b == 4){
            kondisi.setText(": Hancur          ");
            kondisi.setBackgroundResource(R.color.k_hancur);
            kondisi.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if(d == 1){
            sedia.setText(": Ada             ");
            sedia.setBackgroundResource(R.color.s_ada);
        }
        else if(d == 2){
            sedia.setText(": Sedang Dipinjam ");
            sedia.setBackgroundResource(R.color.s_pinjam);
        }
        else if(d == 3){
            sedia.setText(": Sedang Diperbaiki");
            sedia.setBackgroundResource(R.color.s_perbaiki);
            sedia.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else if(d == 4){
            sedia.setText(": Hilang           ");
            sedia.setBackgroundResource(R.color.s_hilang);
            sedia.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else if(d == 5){
            sedia.setText(": Tidak Ada");
            sedia.setBackgroundResource(R.color.s_t_ada);
        }
        tahun.setText(data.getTahun());

        return convertView;
    }
}
