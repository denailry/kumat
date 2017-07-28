package com.example.user.kumat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kumat.Database.PengeluaranOtomatisDatabase;
import com.example.user.kumat.Listener.PengeluaranOtomatisListener;
import com.example.user.kumat.R;

import java.util.ArrayList;

/**
 * Created by User on 09/07/2017.
 */

public class PengeluaranOtomatisAdapter extends RecyclerView.Adapter<PengeluaranOtomatisAdapter.ViewHolder> {

    PengeluaranOtomatisListener listener;
    ArrayList<PengeluaranOtomatisDatabase> listPengeluaranOtomatis;

    public PengeluaranOtomatisAdapter (ArrayList<PengeluaranOtomatisDatabase> listPengeluaranOtomatis, PengeluaranOtomatisListener listener){
        this.listPengeluaranOtomatis=listPengeluaranOtomatis;
        this.listener=listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Button btnDelete;
        TextView txtNamaBarang;
        TextView txtHargaBarang;
        TextView txtWaktu;

        public ViewHolder(View v) {
            super(v);
            btnDelete = (Button)v.findViewById(R.id.btn_delete_otomatis);
            txtNamaBarang = (TextView)v.findViewById(R.id.txt_nama_barang_otomatis);
            txtHargaBarang = (TextView)v.findViewById(R.id.txt_harga_otomatis);
            txtWaktu = (TextView)v.findViewById(R.id.txt_data_waktu);

        }
    }

    @Override
    public PengeluaranOtomatisAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengeluaran_otomatis,parent,false);

        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final PengeluaranOtomatisDatabase pengeluaranOtomatisDatabase = listPengeluaranOtomatis.get(position);

        holder.txtNamaBarang.setText(String.valueOf(pengeluaranOtomatisDatabase.getNamaBarang()));
        holder.txtHargaBarang.setText(editRupiah(String.valueOf(pengeluaranOtomatisDatabase.getHargaBarang())));

        String jam,menit,tanggal;

        if (pengeluaranOtomatisDatabase.getJam()<10){
            jam = "0"+pengeluaranOtomatisDatabase.getJam();
        }else {
            jam = String.valueOf(pengeluaranOtomatisDatabase.getJam());
        }

        if (pengeluaranOtomatisDatabase.getMenit()<10){
            menit = "0"+pengeluaranOtomatisDatabase.getMenit();
        }else{
            menit = String.valueOf(pengeluaranOtomatisDatabase.getMenit());
        }

        if (pengeluaranOtomatisDatabase.getTanggal()<10){
            tanggal="0"+pengeluaranOtomatisDatabase.getTanggal();
        }else{
            tanggal=String.valueOf(pengeluaranOtomatisDatabase.getTanggal());
        }

        holder.txtWaktu.setText(jam+":"+menit+" | "+tanggal);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickDeleteOtomatis(pengeluaranOtomatisDatabase);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listPengeluaranOtomatis.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(ArrayList<PengeluaranOtomatisDatabase> listQuickButton){
        this.listPengeluaranOtomatis = listQuickButton;
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

        edtRuiah = "Rp " + edtRuiah;

        return edtRuiah;
    }

}
