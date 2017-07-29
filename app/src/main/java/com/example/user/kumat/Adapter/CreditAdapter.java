package com.example.user.kumat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.kumat.R;

/**
 * Created by User on 30/07/2017.
 */

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {

    String[] listJudul;
    String[] listUrl;
    Context context;

    public CreditAdapter(String[] listJudul, String[] listUrl, Context context){
        this.listJudul = listJudul;
        this.listUrl = listUrl;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtJudul,txtUrl;

        public ViewHolder(View v) {
            super(v);
            txtJudul = (TextView)v.findViewById(R.id.txt_judul_credit);
            txtUrl = (TextView)v.findViewById(R.id.txt_url);
        }
    }

    @Override
    public CreditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_credit,parent,false);

        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        holder.txtJudul.setText(listJudul[position]+" -");
        holder.txtUrl.setText(listUrl[position]);

        holder.txtUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listUrl[position]));
                context.startActivity(browserIntent);
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
