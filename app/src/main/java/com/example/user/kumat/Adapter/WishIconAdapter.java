package com.example.user.kumat.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kumat.Database.WishlistDatabase;
import com.example.user.kumat.Listener.WishIconListener;
import com.example.user.kumat.Listener.WishlistListener;
import com.example.user.kumat.R;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by denail on 17/07/30.
 */

public class WishIconAdapter extends RecyclerView.Adapter<WishIconAdapter.ViewHolder> {
    private ArrayList<Integer> listIcon;
    private WishIconListener listener;
    private Context context;

    public WishIconAdapter(ArrayList<Integer> listIcon, WishIconListener listener, Context context) {
        this.listIcon = listIcon;
        this.listener = listener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIkon;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.ivIkon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }

    @Override
    public WishIconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_wishlist_icon, parent, false);
        WishIconAdapter.ViewHolder vh = new WishIconAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(WishIconAdapter.ViewHolder holder, int position) {
        Bitmap image;
        Log.d("TEST", String.valueOf(position));
        final int pos = position;
        switch(position) {
            case 0:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_add);
                image = WishlistDatabase.fitImageSize(image, 50000);
                holder.ivIkon.setImageBitmap(image);
                break;
            case 1:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.wishlist_ic_book);
                image = WishlistDatabase.fitImageSize(image, 50000);
                holder.ivIkon.setImageBitmap(image);
                break;
            case 2:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.wishlist_ic_laptop);
                image = WishlistDatabase.fitImageSize(image, 50000);
                holder.ivIkon.setImageBitmap(image);
                break;
            case 3:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.wishlist_ic_makanan);
                image = WishlistDatabase.fitImageSize(image, 50000);
                holder.ivIkon.setImageBitmap(image);
                break;
            case 4:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.wishlist_ic_mobil);
                image = WishlistDatabase.fitImageSize(image, 50000);
                holder.ivIkon.setImageBitmap(image);
                break;
            case 5:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.wishlist_ic_motor);
                image = WishlistDatabase.fitImageSize(image, 50000);
                holder.ivIkon.setImageBitmap(image);
                break;
            case 6:
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.wishlist_ic_tourism);
                image = WishlistDatabase.fitImageSize(image, 50000);
                holder.ivIkon.setImageBitmap(image);
                break;
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listIcon.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
