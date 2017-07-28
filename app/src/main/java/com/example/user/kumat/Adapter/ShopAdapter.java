package com.example.user.kumat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kumat.Database.ShopDatabase;
import com.example.user.kumat.Listener.ShopListener;
import com.example.user.kumat.R;

import java.util.ArrayList;

/**
 * Created by User on 24/07/2017.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    ArrayList<ShopDatabase> listShop = new ArrayList<>();
    ShopListener listener;

    public ShopAdapter(ArrayList<ShopDatabase> listShop, ShopListener listener){
        this.listShop = listShop;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtJudul, txtKoin;
        ImageView imgShop;
        Button btnBeli;

        public ViewHolder(View v){
            super(v);

            txtJudul = (TextView)v.findViewById(R.id.txt_judul_shop);
            txtKoin = (TextView)v.findViewById(R.id.koin_shop);
            imgShop = (ImageView)v.findViewById(R.id.img_foto_shop);
            btnBeli = (Button)v.findViewById(R.id.btn_beli);
        }
    }

    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_shop,parent,false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final ShopDatabase shop = listShop.get(position);

        holder.txtJudul.setText(shop.getJudul());
        holder.txtKoin.setText(String.valueOf(shop.getKoin()));
        holder.imgShop.setImageResource(shop.getFoto());
        holder.btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBeliClick(shop);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listShop.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(ArrayList<ShopDatabase> lisShop){
        this.listShop = lisShop;
        notifyDataSetChanged();
    }



}
