package com.example.ade.beritasurabaya.views;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ade.beritasurabaya.MainActivity;
import com.example.ade.beritasurabaya.R;
import com.example.ade.beritasurabaya.data.PesanUser;

import java.util.ArrayList;

/**
 * Created by widianta on 20-Aug-16.
 */
public class PesanList extends ArrayAdapter<PesanUser> {
    private Activity _context;
    private MainActivity _mainActivity;

    public PesanList(MainActivity mainActivity, ArrayList<PesanUser> pesanUsers) {
        super((Activity) mainActivity, R.layout.pesanuseritem, pesanUsers);
        _context = mainActivity;
        _mainActivity = mainActivity;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = _context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.pesanuseritem, null,true);

        PesanUser pesanUserData = getItem(position);
        ((TextView)rowView.findViewById(R.id.judulpesanuseritem)).setText(pesanUserData.getJudul());
        if (pesanUserData.getPesan() != null && !pesanUserData.getPesan().isEmpty()) {
            TextView pesanuserpesan = (TextView) rowView.findViewById(R.id.descriptionpesanuseritem);
            pesanuserpesan.setText(pesanUserData.getPesan());
            pesanuserpesan.setVisibility(View.VISIBLE);

            Button button = (Button) rowView.findViewById(R.id.hapusPesanUser);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ListView listView = (ListView) v.getParent().getParent();
                            PesanList pesanList = (PesanList) listView.getAdapter();
                            PesanUser pesanUsers = pesanList.getItem(position);
                            _mainActivity.deletePesanUser(pesanUsers.getId());
                            pesanList.remove(pesanUsers);
                            pesanList.notifyDataSetInvalidated();
                        }
                    }
            );
        }

        return rowView;
    }
}
