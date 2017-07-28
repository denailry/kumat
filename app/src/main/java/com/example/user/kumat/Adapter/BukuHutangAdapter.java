package com.example.user.kumat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.kumat.Database.BukuHutangDatabase;
import com.example.user.kumat.Listener.BukuHutangListener;
import com.example.user.kumat.R;

import java.util.ArrayList;

/**
 * Created by User on 19/07/2017.
 */

public class BukuHutangAdapter extends RecyclerView.Adapter<BukuHutangAdapter.ViewHolder> {

    BukuHutangListener listener;
    ArrayList<BukuHutangDatabase> listBukuHutang;
    Context context;

    public BukuHutangAdapter (ArrayList<BukuHutangDatabase> listBukuHutang, BukuHutangListener listener, Context context){
        this.listBukuHutang=listBukuHutang;
        this.listener = listener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        Button btnBayar;
        TextView txtNama, txtNominal;

        public ViewHolder(View v){
            super(v);
            btnBayar=(Button)v.findViewById(R.id.btn_bayar);
            txtNama = (TextView)v.findViewById(R.id.txt_nama_hutang);
            txtNominal = (TextView)v.findViewById(R.id.txt_nominal_hutang);
        }
    }


    @Override
    public BukuHutangAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_buku_hutang,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final BukuHutangDatabase bukuHutangDatabase = listBukuHutang.get(position);

        //btn bayar setting text
        if (bukuHutangDatabase.getTipe()==0){
            holder.btnBayar.setText("bayar");
            holder.btnBayar.setBackgroundColor(context.getResources().getColor(R.color.kuning_muda_hutang));
            holder.btnBayar.setTextColor(context.getResources().getColor(R.color.kuning_hutang));
        }else{
            holder.btnBayar.setText("dibayar");
            holder.btnBayar.setBackgroundColor(context.getResources().getColor(R.color.biru_muda_hutang));
            holder.btnBayar.setTextColor(context.getResources().getColor(R.color.biru_hutang));
        }

        //setting nama
        holder.txtNama.setText(bukuHutangDatabase.getNama());
        holder.txtNominal.setText(editRupiah(String.valueOf(bukuHutangDatabase.getNominal())));


        //btnclick
        holder.btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickBayar(bukuHutangDatabase);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listBukuHutang.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(ArrayList<BukuHutangDatabase> listBukuHutang){
        this.listBukuHutang = listBukuHutang;
        notifyDataSetChanged();
    }
    private String editRupiah(String rupiah){
        String edtRuiah = rupiah;
        int length = edtRuiah.length();

        //100000
        while (length>3){
            edtRuiah = edtRuiah.substring(0,(length-3))+"."+edtRuiah.substring((length-3),edtRuiah.length());
            length = length-3;
        }

        edtRuiah = "Rp " + edtRuiah + ",-";

        return edtRuiah;
    }

}
