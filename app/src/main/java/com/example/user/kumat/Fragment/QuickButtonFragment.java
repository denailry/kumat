package com.example.user.kumat.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.kumat.Adapter.DataQuickButtonAdapter;
import com.example.user.kumat.Adapter.IconAdapter;
import com.example.user.kumat.Database.QuickButtonDatabase;
import com.example.user.kumat.Database.QuickButtonDatabase_Table;
import com.example.user.kumat.Listener.DataQuickButtonListener;
import com.example.user.kumat.Listener.IconListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.MyWidgetProvider;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickButtonFragment extends Fragment implements DataQuickButtonListener{

    RecyclerView rvIcon,rvDataQuick;
    ArrayList<Integer> listIcon=new ArrayList<>();

    QuickButtonDatabase quickButtonDatabase;

    ArrayList<QuickButtonDatabase> listQuickButton = new ArrayList<>();

    DataQuickButtonAdapter quickAdapter;

    ImageView imgAddPhoto;

    int thisIcon = 0;

    int idQuick;

    EditText boxNamaBarang,boxNominalBarang;

    Button btnSubmiDataQuick;

    String namaBarang;
    int hargaBarang;

    ImageView imgNoItem;

    public QuickButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quick_button, container, false);

        rvIcon = (RecyclerView)view.findViewById(R.id.rv_icon);
        imgAddPhoto = (ImageView)view.findViewById(R.id.img_add_photo);
        rvDataQuick=(RecyclerView)view.findViewById(R.id.rv_data_quick);
        boxNamaBarang = (EditText)view.findViewById(R.id.nama_quick);
        boxNominalBarang = (EditText)view.findViewById(R.id.nominal_quick);
        btnSubmiDataQuick = (Button)view.findViewById(R.id.btn_submit_data_quick);
        imgNoItem = (ImageView)view.findViewById(R.id.no_item_quick);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        imgAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).munculAddPhotoIcon();

            }
        });

        //call list quick button
        rvDataQuick.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvDataQuick.setLayoutManager(llm);

        quickAdapter = new DataQuickButtonAdapter(listQuickButton,this);
        rvDataQuick.setAdapter(quickAdapter);

        callQuickButtonData();


        btnSubmiDataQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(boxNamaBarang.getText())||TextUtils.isEmpty(boxNominalBarang.getText())){
                    Toast.makeText(getContext(),"Data tidak boleh kosong",Toast.LENGTH_SHORT).show();
                }else if (thisIcon == 0){
                    Toast.makeText(getContext(), "Anda belum memilih Icon", Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        namaBarang= boxNamaBarang.getText().toString();
                        hargaBarang=Integer.parseInt(boxNominalBarang.getText().toString());

                        QuickButtonDatabase quickButtonDatabase = new QuickButtonDatabase();
                        quickButtonDatabase.setId(idQuick);
                        quickButtonDatabase.setIcon(thisIcon);
                        quickButtonDatabase.setNama(namaBarang);
                        quickButtonDatabase.setHarga(hargaBarang);
                        quickButtonDatabase.save();

                        boxNominalBarang.setText("");
                        boxNamaBarang.setText("");

                        thisIcon=0;
                        imgAddPhoto.setImageResource(R.drawable.icon_add);

                        Snackbar.make(v,"Data berhasil dimasukkan", BaseTransientBottomBar.LENGTH_SHORT)
                                .setAction("Action",null).show();

                        callQuickButtonData();

                        updateWidget();


                    }catch (NumberFormatException e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }

    private void updateWidget() {

        Intent intent_meeting_update=new  Intent(getContext(),MyWidgetProvider.class);
        intent_meeting_update.setAction(MyWidgetProvider.UPDATE_MEETING_ACTION);

        getContext().sendBroadcast(intent_meeting_update);


    }


    private void callQuickButtonData() {
        listQuickButton = new ArrayList<>();

        List<QuickButtonDatabase> quickSearch = new Select()
                .from(QuickButtonDatabase.class)
                .queryList();
        for (QuickButtonDatabase list : quickSearch){
            QuickButtonDatabase quick = new QuickButtonDatabase();
            quick.setId(list.getId());
            quick.setIcon(list.getIcon());
            quick.setNama(list.getNama());
            quick.setHarga(list.getHarga());
            listQuickButton.add(quick);
        }

        if (listQuickButton.size()==0){
            idQuick=1;
            imgNoItem.setVisibility(View.VISIBLE);
        }else {
            Collections.reverse(listQuickButton);
            idQuick=listQuickButton.get(0).getId()+1;
            imgNoItem.setVisibility(View.GONE);
        }

        //refresh adapter
        quickAdapter.refreshData(listQuickButton);


    }

    @Override
    public void onClickDataDelete(QuickButtonDatabase quickButtonDatabase) {
        this.quickButtonDatabase = quickButtonDatabase;
        ((MainActivity)getActivity()).munculHapusQuickButton(quickButtonDatabase);

    }

    public void onTerimaDelete(){
        int idDelete= quickButtonDatabase.getId();

        List<QuickButtonDatabase> quickSearch = new Select()
                .from(QuickButtonDatabase.class)
                .where(QuickButtonDatabase_Table.id.is(idDelete))
                .queryList();
        QuickButtonDatabase quick = quickSearch.get(0);
        quick.delete();
        Toast.makeText(getContext(),"deleted",Toast.LENGTH_SHORT).show();

        callQuickButtonData();
        updateWidget();
    }

    public void onIconClicked(int icon){

            thisIcon = icon;
            imgAddPhoto.setImageResource(thisIcon);

    }
}
