package com.example.user.kumat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kumat.Database.QuickButtonDatabase;
import com.example.user.kumat.Listener.DataQuickButtonListener;
import com.example.user.kumat.R;

import java.util.ArrayList;

/**
 * Created by User on 09/07/2017.
 */

public class DataQuickButtonAdapter extends RecyclerView.Adapter<DataQuickButtonAdapter.ViewHolder> {
    ArrayList<QuickButtonDatabase> listQuickButton;
    DataQuickButtonListener listener;

    public DataQuickButtonAdapter(ArrayList<QuickButtonDatabase> listQuickButton,DataQuickButtonListener listener){
        this.listQuickButton=listQuickButton;
        this.listener=listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView nama,harga;
        Button btnDelete;

        public ViewHolder(View v) {
            super(v);
            icon=(ImageView)v.findViewById(R.id.icon_quick_button_data);
            nama=(TextView)v.findViewById(R.id.txtNama_data);
            harga=(TextView)v.findViewById(R.id.txtHarga_data);
            btnDelete=(Button)v.findViewById(R.id.btn_delete_data);
        }
    }

    @Override
    public DataQuickButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_quick_button,parent,false);

        ViewHolder vh= new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        final QuickButtonDatabase quickButton = listQuickButton.get(position);

        holder.icon.setImageResource(quickButton.getIcon());
        holder.nama.setText(quickButton.getNama());
        holder.harga.setText(editRupiah(String.valueOf(quickButton.getHarga())));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickDataDelete(quickButton);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listQuickButton.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void refreshData(ArrayList<QuickButtonDatabase> listQuickButton){
        this.listQuickButton = listQuickButton;
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
}
