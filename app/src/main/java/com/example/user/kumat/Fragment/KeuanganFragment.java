package com.example.user.kumat.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kumat.Adapter.QuickButtonAdapter;
import com.example.user.kumat.Database.AktivitasKeuanganDatabase;
import com.example.user.kumat.Database.QuickButtonDatabase;
import com.example.user.kumat.Database.SaldoDatabase;
import com.example.user.kumat.Database.SaldoDatabase_Table;
import com.example.user.kumat.Listener.QuickButtonListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeuanganFragment extends Fragment implements QuickButtonListener{

    EditText detailNama,detailHarga;
    Button btnSubmit,btnDate;
    RecyclerView rvQuickButton;
    TextView txtSaldo,tanggal;
    ArrayList<QuickButtonDatabase> listQuickButton;
    QuickButtonAdapter adapter;
    RadioGroup radioPengeluaranPemasukan;
    ImageView imgNoItem;

    int pilihan=0;
    int saldo;
    int id=1;
    int idx;
    int idxQuick;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    String namaBarang;
    int hargaBarang;

    public KeuanganFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keuangan, container, false);

        detailNama=(EditText)view.findViewById(R.id.detail_nama);
        detailHarga=(EditText)view.findViewById(R.id.detail_harga);
        tanggal=(TextView)view.findViewById(R.id.detail_tanggal);
        btnDate=(Button)view.findViewById(R.id.btn_date);
        radioPengeluaranPemasukan = (RadioGroup)view.findViewById(R.id.radio_grup_penge);
        btnSubmit=(Button)view.findViewById(R.id.btn_submit);
        rvQuickButton=(RecyclerView)view.findViewById(R.id.rv_quick);
        txtSaldo=(TextView)view.findViewById(R.id.text_saldo);
        imgNoItem = (ImageView)view.findViewById(R.id.no_item);
        listQuickButton=new ArrayList<>();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


        //inisialisasi calendar
        setHariIni();

        //load data saldo dan index
        printSaldo();


        //tentang recycler viewnya
        rvQuickButton.setHasFixedSize(true);
        rvQuickButton.setLayoutManager(new GridLayoutManager(getContext(), 4));

        adapter = new QuickButtonAdapter(listQuickButton,this);

        rvQuickButton.setAdapter(adapter);

        //load data data list quick button
        callListQuickButton();

        //DIBAWAH INI SUDAH PERINTAH PERINTAH BUTTON DAN SEMACAMNYA YANG DI KLIK DAN OPSIONAL

        //perintah ketika user ingin merubah tanggal
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showDate(day,month,year);
                Log.d("showDate", "onClick: "+day+","+month+","+year);
            }
        });

        //tuker tukeran pengeluaran dan pemasukan
        radioPengeluaranPemasukan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_pengeluaran){
                    pilihan=0;
                }else{
                    pilihan=1;
                }

            }
        });




        //submit data
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(detailNama.getText())||TextUtils.isEmpty(detailHarga.getText())){
                    Toast.makeText(getContext(),"Data tidak boleh kosong",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        namaBarang=detailNama.getText().toString();
                        hargaBarang=Integer.parseInt(detailHarga.getText().toString());

                        if (hargaBarang>saldo&&pilihan==0){
                            Toast.makeText(getContext(),"Nominal melebihi jumlah Saldo",Toast.LENGTH_SHORT).show();
                        }else{
                            if (pilihan==0){
                                pengeluaran();
                            }else{
                                pemasukan();
                            }
                            Snackbar.make(v,"Data berhasil dimasukkan", BaseTransientBottomBar.LENGTH_SHORT)
                                    .setAction("Action",null).show();
                            detailNama.setText("");
                            detailHarga.setText("");

                        }
                    }catch (NumberFormatException e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setHariIni() {
        calendar=Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if ((day<10)&&(month+1<10)){
            tanggal.setText("0"+day+" - 0"+(month+1)+" - "+year);
        }else{
            if (day<10){
                tanggal.setText("0"+day+" - "+(month+1)+" - "+year);
            }else if (month+1<10){
                tanggal.setText(day+" - 0"+(month+1)+" - "+year);
            }else {
                tanggal.setText(day+" - "+(month+1)+" - "+year);
            }
        }
    }

    //prosedur ketika salah satu quickbutton di click
    @Override
    public void onClickItem(QuickButtonDatabase quickButton) {
        //langsung ke pengeluaran
        namaBarang=quickButton.getNama();
        hargaBarang=quickButton.getHarga();

        if (hargaBarang>saldo){
            Toast.makeText(getContext(),"Nominal melebihi jumlah Saldo",Toast.LENGTH_SHORT).show();
        }else{
            pengeluaran();
            Snackbar.make(getView(),"Data berhasil dimasukkan", BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("Action",null).show();
        }

    }

    private void pengeluaran(){
        AktivitasKeuanganDatabase aktivitasKeuangan = new AktivitasKeuanganDatabase();
        aktivitasKeuangan.setId(idx);
        aktivitasKeuangan.setNamaBarang(namaBarang);
        aktivitasKeuangan.setHargaBarang(hargaBarang);
        aktivitasKeuangan.setTipe(0);
        aktivitasKeuangan.setTanggal(day);
        aktivitasKeuangan.setBulan(month+1);
        aktivitasKeuangan.setTahun(year);
        aktivitasKeuangan.save();

        idx=idx+1;

        saldo=saldo-hargaBarang;
        SaldoDatabase sald = new SaldoDatabase();
        sald.setId(id);
        sald.setIndex(idx);
        sald.setIndexQuick(idxQuick);
        sald.setSaldo(saldo);
        sald.save();
        txtSaldo.setText(((MainActivity)getActivity()).editRupiah(String.valueOf(saldo)));

    }

    private void pemasukan(){
        AktivitasKeuanganDatabase aktivitasKeuangan = new AktivitasKeuanganDatabase();
        aktivitasKeuangan.setId(idx);
        aktivitasKeuangan.setNamaBarang(namaBarang);
        aktivitasKeuangan.setHargaBarang(hargaBarang);
        aktivitasKeuangan.setTipe(1);
        aktivitasKeuangan.setTanggal(day);
        aktivitasKeuangan.setBulan(month+1);
        aktivitasKeuangan.setTahun(year);
        aktivitasKeuangan.save();

        idx=idx+1;

        saldo=saldo+hargaBarang;
        SaldoDatabase sald = new SaldoDatabase();
        sald.setId(id);
        sald.setIndex(idx);
        sald.setIndexQuick(idxQuick);
        sald.setSaldo(saldo);
        sald.save();
        txtSaldo.setText(((MainActivity)getActivity()).editRupiah(String.valueOf(saldo)));

    }



    //menerima dari parent activity hasil dari SET DATE
    public void refreshDate(int day,int month,int year){
        this.day=day;
        this.month=month;
        this.year=year;

        if ((day<10)&&(month+1<10)){
            tanggal.setText("0"+day+" - 0"+(month+1)+" - "+year);
        }else{
            if (day<10){
                tanggal.setText("0"+day+" - "+(month+1)+" - "+year);
            }else if (month+1<10){
                tanggal.setText(day+" - 0"+(month+1)+" - "+year);
            }else {
                tanggal.setText(day+" - "+(month+1)+" - "+year);
            }
        }

        Log.d("refreshDate", "refreshDate: "+day+","+(month+1)+"'"+year);
    }

    //menerima perintah dari onCreateView, untuk load data saldo dan index-index
    private void printSaldo() {
        List<SaldoDatabase> saldoSearch = new Select()
                .from(SaldoDatabase.class)
                .where(SaldoDatabase_Table.id.is(id))
                .queryList();
        if (saldoSearch.size()==0){
            txtSaldo.setText("Rp 0,-");
            saldo=0;
            SaldoDatabase saldo=new SaldoDatabase();
            saldo.setId(id);
            idx=1;
            saldo.setIndex(idx);
            saldo.setIndexQuick(idx);
            saldo.setSaldo(0);
            saldo.save();
            Log.d("saldo", saldo.getId()+","+saldo.getSaldo()+","+saldo.getIndex()+","+saldo.getIndexQuick());
        }else{
            saldo=saldoSearch.get(0).getSaldo();
            idx=saldoSearch.get(0).getIndex();
            idxQuick=saldoSearch.get(0).getIndexQuick();
            String saldoo = ((MainActivity)getActivity()).editRupiah(String.valueOf(saldo));
            txtSaldo.setText(saldoo);
        }
    }

    //prosedur untuk load data quick button
    public void callListQuickButton() {
        listQuickButton=new ArrayList<>();
        List<QuickButtonDatabase> quickSearch= new Select().
                from(QuickButtonDatabase.class).
                queryList();
        for (QuickButtonDatabase listquick : quickSearch){
            QuickButtonDatabase quick = new QuickButtonDatabase();
            quick.setId(listquick.getId());
            quick.setNama(listquick.getNama());
            quick.setHarga(listquick.getHarga());
            quick.setIcon(listquick.getIcon());
            listQuickButton.add(quick);
            Log.d("sizequick", "size : "+quickSearch.size()+", nama : "+quick.getNama()+", harga : "+String.valueOf(quick.getHarga()));
        }
        if (listQuickButton.size()==0){
            imgNoItem.setVisibility(View.VISIBLE);
        }else{
            imgNoItem.setVisibility(View.GONE);
        }
        Collections.reverse(listQuickButton);
        adapter.refreshData(listQuickButton);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("tes", "onStart");
    }
    @Override
    public void onResume(){
        super.onResume();
        setHariIni();
        printSaldo();
        Log.d("tes", "onResume");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d("tes", "onPause");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d("tes", "onStop");
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d("tes", "onDestroyView");
        ((MainActivity)getActivity()).removeD(999);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("tes", "onDestroy");
    }
    @Override
    public void onDetach(){
        super.onDetach();
        Log.d("tes", "onDetach");
    }



}
