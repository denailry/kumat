package com.example.user.kumat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.kumat.Database.AktivitasKeuanganDatabase;
import com.example.user.kumat.Listener.AktivitasKeuanganListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by User on 05/07/2017.
 */

public class AktivitasKeuanganAdapter extends RecyclerView.Adapter<AktivitasKeuanganAdapter.ViewHolder> {
    ArrayList<AktivitasKeuanganDatabase> listAktivitasKeuangan;
    AktivitasKeuanganListener listener;
    Context context;

    public AktivitasKeuanganAdapter(ArrayList<AktivitasKeuanganDatabase> listAktivitasKeuangan, AktivitasKeuanganListener listener, Context context){
        this.listAktivitasKeuangan = listAktivitasKeuangan;
        this.listener = listener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout background;
        Button btnDelete;
        TextView txtDetail,txtHarga,txtTanggal;

        public ViewHolder(View v){
            super(v);
            background=(RelativeLayout)v.findViewById(R.id.backgroud_list);
            btnDelete = (Button)v.findViewById(R.id.btn_delete);
            txtDetail = (TextView)v.findViewById(R.id.txt_detail);
            txtHarga = (TextView)v.findViewById(R.id.txt_harga_detail);
            txtTanggal = (TextView)v.findViewById(R.id.txt_tanggal_detail);
        }
    }

    @Override
    public AktivitasKeuanganAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_aktivitas_keuangan,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        final AktivitasKeuanganDatabase aktivitasKeuanganDatabase = listAktivitasKeuangan.get(position);

        holder.btnDelete.setBackground(context.getResources().getDrawable(R.drawable.v_bawah));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnDelete.setBackground(context.getResources().getDrawable(R.drawable.v_atas));
                listener.onClickDelete(aktivitasKeuanganDatabase);
            }
        });

        holder.txtDetail.setText(edtNama(aktivitasKeuanganDatabase.getNamaBarang()));
        holder.txtHarga.setText(editRupiah(String.valueOf(aktivitasKeuanganDatabase.getHargaBarang())));

        int day = aktivitasKeuanganDatabase.getTanggal();
        int month = aktivitasKeuanganDatabase.getBulan();
        int year = aktivitasKeuanganDatabase.getTahun();

        year = year % 100;

        if ((day<10)&&(month+1<10)){
            holder.txtTanggal.setText("0"+day+"/0"+(month)+"/"+year);
        }else{
            if (day<10){
                holder.txtTanggal.setText("0"+day+"/"+(month)+"/"+year);
            }else if (month+1<10){
                holder.txtTanggal.setText(day+"/0"+(month)+"/"+year);
            }else {
                holder.txtTanggal.setText(day+"/"+(month)+"/"+year);
            }
        }

        if (aktivitasKeuanganDatabase.getTipe()==0 || aktivitasKeuanganDatabase.getTipe()==2 || aktivitasKeuanganDatabase.getTipe()==4){
            holder.background.setBackground(context.getResources().getDrawable(R.drawable.aktivitas_keuangan_pengeluaran));
        }else {
            holder.background.setBackground(context.getResources().getDrawable(R.drawable.aktivitas_keuangan_pemasukan));
        }

    }

    @Override
    public int getItemCount() {
        return listAktivitasKeuangan.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(ArrayList<AktivitasKeuanganDatabase> listAktivitasKeuangan){
        this.listAktivitasKeuangan = listAktivitasKeuangan;
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

        edtRuiah = "Rp " + edtRuiah+",-";

        return edtRuiah;
    }

    private String edtNama(String item){
        String edtItem = item;

        if (edtItem.length()>10){
            edtItem = edtItem.substring(0,10)+"..";
        }

        return edtItem;

    }



}
