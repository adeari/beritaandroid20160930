package com.example.ade.beritasurabaya.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ade.beritasurabaya.MainActivity;
import com.example.ade.beritasurabaya.R;
import com.example.ade.beritasurabaya.data.Alamat;
import com.example.ade.beritasurabaya.data.PesanUser;

import java.util.ArrayList;

/**
 * Created by widianta on 20-Aug-16.
 */
public class AlamatKantorList extends ArrayAdapter<Alamat> {
    private Activity _context;
    private MainActivity _mainActivity;

    public AlamatKantorList(MainActivity mainActivity, ArrayList<Alamat> alamat) {
        super((Activity) mainActivity, R.layout.alamatkantoritem, alamat);
        _context = mainActivity;
        _mainActivity = mainActivity;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = _context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.alamatkantoritem, null,true);

        Alamat alamatData = getItem(position);
        ((TextView)rowView.findViewById(R.id.kotaalamatkantor)).setText(alamatData.getKota());
        ((TextView)rowView.findViewById(R.id.alamatalamatkantor)).setText(alamatData.getAlamat());
        ((TextView)rowView.findViewById(R.id.teleponalamatkantor)).setText(alamatData.getTelepon());
        ((TextView)rowView.findViewById(R.id.emailalamatkantor)).setText(alamatData.getEmail());

        return rowView;
    }
}
