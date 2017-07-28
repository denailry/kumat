package com.example.user.kumat.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.kumat.Adapter.WishlistAdapter;
import com.example.user.kumat.Database.WishlistDatabase;
import com.example.user.kumat.Database.WishlistDatabase_Table;
import com.example.user.kumat.Listener.OnFragmentDestroyListener;
import com.example.user.kumat.Listener.OnItemSaveListener;
import com.example.user.kumat.Listener.WishlistListener;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by denail on 17/07/13.
 */

public class WishlistFragment extends Fragment implements WishlistListener {

    public static Boolean isActive;

    private RecyclerView rvWishlist;
    private WishlistAdapter adapter;
    private CardView cvCreateNew, cvTabunganku;
    private ImageView ivNoItem;
    private ArrayList<WishlistDatabase> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        cvCreateNew = (CardView) view.findViewById(R.id.cv_create_new);
        cvTabunganku = (CardView) view.findViewById(R.id.cv_tabunganku);
        rvWishlist = (RecyclerView) view.findViewById(R.id.rv_wishlist);
        ivNoItem = (ImageView) view.findViewById(R.id.iv_no_item);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refresh();

        isActive = true;
        adapter = new WishlistAdapter(itemList, this, getActivity().getContentResolver());
        rvWishlist.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvWishlist.setAdapter(adapter);

        cvCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(new WishlistDatabase(0L));
            }
        });
        cvTabunganku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistDatabase tabunganku = new Select()
                        .from(WishlistDatabase.class)
                        .where(WishlistDatabase_Table.id.eq(1L))
                        .querySingle();
                onItemClick(tabunganku);
            }
        });
    }

    private ArrayList<WishlistDatabase> loadData() {
        WishlistDatabase tabunganku = new Select()
                .from(WishlistDatabase.class)
                .where(WishlistDatabase_Table.id.eq(1L))
                .querySingle();
        if(tabunganku == null) {
            tabunganku = new WishlistDatabase(1L, "Tabunganku", 0, 0, null);
            tabunganku.save();
        }

        List<WishlistDatabase> listData = new Select().from(WishlistDatabase.class).queryList();
        ArrayList<WishlistDatabase> dataSet = new ArrayList<>();

        for(WishlistDatabase data : listData) {
            if(data.getId() != 1L) {
                dataSet.add(data);
            }
        }

        Collections.sort(dataSet, new Comparator<WishlistDatabase>() {
            @Override
            public int compare(WishlistDatabase left, WishlistDatabase right) {
                if(left.getRealPersentase() > right.getRealPersentase()) {
                    return -1;
                } else if(left.getRealPersentase() < right.getRealPersentase()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        return dataSet;
    }

    @Override
    public void onItemClick(WishlistDatabase wishlist) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(wishlist.getId() == 0) {
            WishlistCreateFragment createFragment = new WishlistCreateFragment();
            createFragment.setOnItemSaveListener(new onSave());
            createFragment.setOnFragmentDestroyListener(new FragmentDestroyListener());
            ft.add(R.id.root_fragment_wishlist, createFragment, "CREATE WISHLIST");
        } else {
            WishlistDatabase item = new Select()
                    .from(WishlistDatabase.class)
                    .where(WishlistDatabase_Table.id.eq(wishlist.getId()))
                    .querySingle();
            WishlistDetailFragment detailFragment = new WishlistDetailFragment(item);
            detailFragment.setOnItemSaveListener(new onSave());
            detailFragment.setOnFragmentDestroyListener(new FragmentDestroyListener());
            ft.add(R.id.root_fragment_wishlist, detailFragment, "DETAIL WISHLIST");
        }

        ft.addToBackStack(null);
        ft.commit();
        isActive = false;
    }

    private void refresh() {
        itemList = loadData();
        if(itemList.size() > 0) {
            ivNoItem.setVisibility(View.GONE);
        } else {
            ivNoItem.setVisibility(View.VISIBLE);
        }
    }

    private class onSave implements OnItemSaveListener {
        @Override
        public void onItemSave() {
            refresh();
            adapter.refreshData(itemList);
        }
    }

    private class FragmentDestroyListener implements OnFragmentDestroyListener {
        @Override
        public void onFragmentDestroy() {
            isActive = true;
        }
    }
}