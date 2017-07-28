package com.example.user.kumat.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.kumat.Adapter.ShopAdapter;
import com.example.user.kumat.Database.ProfilDatabase;
import com.example.user.kumat.Database.ProfilDatabase_Table;
import com.example.user.kumat.Database.ShopDatabase;
import com.example.user.kumat.Listener.ShopListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KumatShopFragment extends Fragment implements ShopListener {

    RecyclerView rvShop;
    ShopAdapter adapter;
    ArrayList<ShopDatabase> listShop;


    public KumatShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_kumat_shop, container, false);

        rvShop = (RecyclerView)v.findViewById(R.id.rv_shop);
        listShop = new ArrayList<>();

        return  v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        rvShop.setHasFixedSize(true);
        rvShop.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new ShopAdapter(listShop,this);
        rvShop.setAdapter(adapter);

        callDataShop();

    }

    private void callDataShop() {
        listShop = new ArrayList<>();
        /*List<ShopDatabase> shopSearch = new Select()
                .from(ShopDatabase.class)
                .queryList();
        for (ShopDatabase list : shopSearch){
            ShopDatabase sho = new ShopDatabase();
            sho.setId(list.getId());
            sho.setFoto(list.getFoto());
            sho.setJudul(list.getJudul());
            sho.setKoin(list.getKoin());
            listShop.add(sho);
        }*/

        ShopDatabase sho = new ShopDatabase();
        sho.setId(1);
        sho.setFoto(R.drawable.icon_add);
        sho.setJudul("FITUR BUKU TABUNGAN");
        sho.setKoin(100);
        listShop.add(sho);

        adapter.refreshData(listShop);
    }

    @Override
    public void onBeliClick(ShopDatabase shop) {

        ProfilDatabase profil = new Select()
                .from(ProfilDatabase.class)
                .where(ProfilDatabase_Table.id.is(1))
                .querySingle();
        if (profil.isBukuHutang()){
            Toast.makeText(getContext(), "Kamu Sudah Membeli ini", Toast.LENGTH_SHORT).show();
        }else{
            if (profil.getKoin()>=shop.getKoin()){

                profil.setKoin(profil.getKoin()-shop.getKoin());
                profil.setBukuHutang(true);
                profil.save();
                Toast.makeText(getContext(), "Kamu Berhasil membeli ini", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).setUpHeader();
            }else{
                Toast.makeText(getContext(), "Koin Kamu Belum Cukup Untuk Membeli ini", Toast.LENGTH_SHORT).show();
            }
        }





    }
}
