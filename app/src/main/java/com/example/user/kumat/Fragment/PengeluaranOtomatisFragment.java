package com.example.user.kumat.Fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kumat.Adapter.PengeluaranOtomatisAdapter;
import com.example.user.kumat.Database.PengeluaranOtomatisDatabase;
import com.example.user.kumat.Database.PengeluaranOtomatisDatabase_Table;
import com.example.user.kumat.Listener.PengeluaranOtomatisListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PengeluaranOtomatisFragment extends Fragment implements PengeluaranOtomatisListener{

    PengeluaranOtomatisDatabase pengeluaranOtomatisDatabase;
    EditText boxNamaOtomatis,boxNominalOtomatis,boxTanggalOtomatis;
    TextView jamOtomatis;
    Button btnJamOtomatis, btnSubmitOtomatis;
    RecyclerView rvPengeluaranOtomatis;
    ArrayList<PengeluaranOtomatisDatabase> listPengeluaranOtomatis;

    PengeluaranOtomatisAdapter adapter;

    String namaBarang;
    long hargaBarang;

    private Calendar calendar;
    private int year, month, day,hour, minute;

    int idOtomatis;

    ImageView imgNoItem;

    public PengeluaranOtomatisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pengeluaran_otomatis, container, false);


        boxNamaOtomatis = (EditText)view.findViewById(R.id.nama_otomatis);
        boxNominalOtomatis= (EditText)view.findViewById(R.id.nominal_otomatis);
        boxTanggalOtomatis = (EditText)view.findViewById(R.id.tanggal_otomatis);
        jamOtomatis = (TextView)view.findViewById(R.id.jam_otomatis);
        btnJamOtomatis = (Button)view.findViewById(R.id.btn_jam_otomatis);
        btnSubmitOtomatis = (Button)view.findViewById(R.id.btn_submit_otomatis);
        rvPengeluaranOtomatis = (RecyclerView)view.findViewById(R.id.rv_pengeluaran_otomatis);
        imgNoItem = (ImageView)view.findViewById(R.id.no_item_otomatis);
        listPengeluaranOtomatis=new ArrayList<>();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //set hari dan jam ini
        setHaridanJam();

        //recyclerview

        rvPengeluaranOtomatis.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvPengeluaranOtomatis.setLayoutManager(llm);

        adapter = new PengeluaranOtomatisAdapter(listPengeluaranOtomatis,this,getContext());
        rvPengeluaranOtomatis.setAdapter(adapter);

        callDataPengeluaranOtomatis();


        btnJamOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showDialCuy(995,hour,minute);
            }
        });

        btnSubmitOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(boxNamaOtomatis.getText())||TextUtils.isEmpty(boxNominalOtomatis.getText())||TextUtils.isEmpty(boxTanggalOtomatis.getText())){
                    Toast.makeText(getContext(),"Data tidak boleh kosong",Toast.LENGTH_SHORT).show();
                }else{
                    try {


                        namaBarang = boxNamaOtomatis.getText().toString();
                        hargaBarang = Integer.parseInt(boxNominalOtomatis.getText().toString());
                        day = Integer.parseInt(boxTanggalOtomatis.getText().toString());

                        if ((day<1)||(day>28)){
                            Toast.makeText(getContext(),"Tanggal harus diantara 1 - 28",Toast.LENGTH_SHORT).show();
                        }else{
                            PengeluaranOtomatisDatabase pengel = new PengeluaranOtomatisDatabase();
                            pengel.setId(idOtomatis);
                            pengel.setNamaBarang(namaBarang);
                            pengel.setHargaBarang(hargaBarang);
                            pengel.setTanggal(day);
                            pengel.setJam(hour);
                            pengel.setMenit(minute);
                            pengel.save();

                            callDataPengeluaranOtomatis();

                            boxNamaOtomatis.setText("");
                            boxNominalOtomatis.setText("");
                            boxTanggalOtomatis.setText("");
                            
                            siapkanNotif(pengel);

                            Snackbar.make(getView(),"Data berhasil dimasukkan", BaseTransientBottomBar.LENGTH_SHORT)
                                    .setAction("Action",null).show();
                            
                        }

                    }catch (NumberFormatException e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        //recyclerview blm ada

    }

    private void siapkanNotif(PengeluaranOtomatisDatabase pengel) {

        ((MainActivity)getActivity()).setNotif(pengel);

    }

    private void callDataPengeluaranOtomatis() {
        listPengeluaranOtomatis= new ArrayList<>();

        List<PengeluaranOtomatisDatabase> otomatisSearch = new Select()
                .from(PengeluaranOtomatisDatabase.class)
                .queryList();
        for (PengeluaranOtomatisDatabase list : otomatisSearch){
            PengeluaranOtomatisDatabase penge = new PengeluaranOtomatisDatabase();

            penge.setId(list.getId());
            penge.setNamaBarang(list.getNamaBarang());
            penge.setHargaBarang(list.getHargaBarang());
            penge.setTanggal(list.getTanggal());
            penge.setJam(list.getJam());
            penge.setMenit(list.getMenit());
            listPengeluaranOtomatis.add(penge);
        }

        if (listPengeluaranOtomatis.size()==0){
            idOtomatis=1;
            imgNoItem.setVisibility(View.VISIBLE);
        }else{
            Collections.reverse(listPengeluaranOtomatis);
            idOtomatis=listPengeluaranOtomatis.get(0).getId()+1;
            imgNoItem.setVisibility(View.GONE);
        }

        //refresh adapter
        adapter.refreshData(listPengeluaranOtomatis);


    }

    private void setHaridanJam() {

        calendar = new GregorianCalendar();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        Log.d("haridanjam", "setHaridanJam: "+hour+", minute : "+minute);

        setDataTextJam(hour,minute);

    }


    private void setDataTextJam(int hour, int minute){
        if ((hour<10)&&(minute<10)){
            jamOtomatis.setText("0"+hour+" : 0"+minute);
        }else{
            if (hour<10){
                jamOtomatis.setText("0"+hour+" : "+minute);
            }else if (minute<10){
                jamOtomatis.setText(hour+" : 0"+minute);
            }else{
                jamOtomatis.setText(hour+" : "+minute);
            }
        }
    }


    public void setelahDapatJam(int hour, int minute){
        this.hour=hour;
        this.minute=minute;
        setDataTextJam(hour,minute);
        ((MainActivity)getActivity()).removeD(995);
    }



    @Override
    public void onClickDeleteOtomatis(PengeluaranOtomatisDatabase pengeluaranOtomatisDatabase) {
        this.pengeluaranOtomatisDatabase = pengeluaranOtomatisDatabase;

        ((MainActivity)getActivity()).munculHapusPengeluaranOtomatis(pengeluaranOtomatisDatabase);

    }

    public void onTerimaDelete(){
        int idDelete = pengeluaranOtomatisDatabase.getId();

        List<PengeluaranOtomatisDatabase> pengeluaranSearch = new Select()
                .from(PengeluaranOtomatisDatabase.class)
                .where(PengeluaranOtomatisDatabase_Table.id.is(idDelete))
                .queryList();
        PengeluaranOtomatisDatabase pengeluaran = pengeluaranSearch.get(0);
        pengeluaran.delete();

        callDataPengeluaranOtomatis();

        ((MainActivity)getActivity()).deletNotif(pengeluaranOtomatisDatabase);


        Log.d("deleee", "onClickDeleteOtomatis");

        Toast.makeText(getContext(),"deleted",Toast.LENGTH_SHORT).show();
    }

    public void notifyData(){
        adapter.notifyDataSetChanged();
    }
}
