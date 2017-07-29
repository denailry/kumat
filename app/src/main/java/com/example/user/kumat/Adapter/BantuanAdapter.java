package com.example.user.kumat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kumat.CreditActivity;
import com.example.user.kumat.R;

/**
 * Created by User on 29/07/2017.
 */

public class BantuanAdapter extends RecyclerView.Adapter<BantuanAdapter.ViewHolder> {


    String[] listJudul;
    String[] listIsi;
    Context context;

    public BantuanAdapter(String[] listJudul, String[] listIsi, Context context){
        this.listJudul=listJudul;
        this.listIsi=listIsi;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtJudul,txtIsi;
        Button btnOpen;

        public ViewHolder(View v) {
            super(v);
            txtJudul = (TextView)v.findViewById(R.id.txt_bantuan);
            txtIsi = (TextView)v.findViewById(R.id.isi_bantuan);
            btnOpen = (Button)v.findViewById(R.id.btn_open_bantuan);
        }
    }

    @Override
    public BantuanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bantuan,parent,false);

        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        holder.txtJudul.setText(listJudul[position]);
        holder.txtIsi.setText(listIsi[position]);
        holder.txtIsi.setVisibility(View.GONE);
        holder.btnOpen.setBackground(context.getResources().getDrawable(R.drawable.v_bawah));

        if (position==listJudul.length-1){
            holder.txtIsi.setTextColor(context.getResources().getColor(R.color.pencet));
        }

        holder.txtIsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==listJudul.length-1){
                    Intent intent = new Intent(context, CreditActivity.class);
                    context.startActivity(intent);
                }
            }
        });

        holder.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txtIsi.getVisibility()==View.GONE){
                    holder.btnOpen.setBackground(context.getResources().getDrawable(R.drawable.v_atas));
                    holder.txtIsi.setVisibility(View.VISIBLE);
                }else{
                    holder.txtIsi.setVisibility(View.GONE);
                    holder.btnOpen.setBackground(context.getResources().getDrawable(R.drawable.v_bawah));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listJudul.length;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
