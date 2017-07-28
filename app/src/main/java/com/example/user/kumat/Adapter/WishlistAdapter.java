package com.example.user.kumat.Adapter;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kumat.Database.WishlistDatabase;
import com.example.user.kumat.Listener.WishlistListener;
import com.example.user.kumat.R;

import java.util.ArrayList;

/**
 * Created by denail on 17/07/10.
 */

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    ArrayList<WishlistDatabase> listData;
    WishlistListener listener;
    ContentResolver resolver;

    public WishlistAdapter(ArrayList<WishlistDatabase> listData, WishlistListener listener,
                           ContentResolver resolver) {
        this.listData = listData;
        this.listener = listener;
        this.resolver = resolver;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama;
        ImageView ivIkon;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.tvNama = (TextView) view.findViewById(R.id.tv_nama);
            this.ivIkon = (ImageView) view.findViewById(R.id.iv_ikon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cardview_wishlist, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final WishlistDatabase wishlist = listData.get(position);
        Bitmap image = wishlist.getImage();
        if(image == null) {
            holder.ivIkon.setImageResource(R.drawable.icon_add);
        } else {
            holder.ivIkon.setImageBitmap(image);
        }
        holder.tvNama.setText(wishlist.getNama());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(wishlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void refreshData(ArrayList<WishlistDatabase> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        listData.remove(position);
        notifyDataSetChanged();
    }
}
