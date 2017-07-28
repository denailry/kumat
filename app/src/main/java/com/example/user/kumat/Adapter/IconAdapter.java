package com.example.user.kumat.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.user.kumat.Listener.IconListener;
import com.example.user.kumat.R;

import java.util.ArrayList;

/**
 * Created by User on 09/07/2017.
 */

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.ViewHolder> {

    ArrayList<Integer> listIcon;

    IconListener listener;

    public IconAdapter(ArrayList<Integer> listIcon, IconListener listener){
        this.listIcon=listIcon;
        this.listener=listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        public ViewHolder(View v){
            super(v);
            imgIcon=(ImageView)v.findViewById(R.id.icon_quick);
        }
    }

    @Override
    public IconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        final int icon = listIcon.get(position);

        holder.imgIcon.setImageResource(icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickIcon(icon);
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

    public void refreshData(ArrayList<Integer> listIcon) {
        this.listIcon=listIcon;
        notifyDataSetChanged();
    }

}
