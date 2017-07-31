package com.example.user.kumat.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kumat.Adapter.AktivitasKeuanganAdapter;
import com.example.user.kumat.Database.AktivitasKeuanganDatabase;
import com.example.user.kumat.Database.AktivitasKeuanganDatabase_Table;
import com.example.user.kumat.Database.BukuHutangDatabase;
import com.example.user.kumat.Database.BukuHutangDatabase_Table;
import com.example.user.kumat.Database.SaldoDatabase;
import com.example.user.kumat.Database.SaldoDatabase_Table;
import com.example.user.kumat.Database.WishlistTransactionDatabase;
import com.example.user.kumat.Database.WishlistTransactionDatabase_Table;
import com.example.user.kumat.Listener.AktivitasKeuanganListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AktivitasKeuanganFragment extends Fragment implements AktivitasKeuanganListener {


    RecyclerView rvAktivitasKeuangan;
    ArrayList<AktivitasKeuanganDatabase> listAktivitasKeuangan;
    AktivitasKeuanganAdapter adapter;

    TextView txtTotalPengeluaran,txtTotalPemasukan;

    ImageView imgNoItem;

    int pilihanPeriode;

    //variabel saldo
    long saldo;
    int idxQuick;
    int idxAktivitas;

    long totalPemasukan ;
    long totalPengeluaran;

    int day1,mont1,year1,day2,mont2,year2;

    RadioGroup radioGroupPilihanWaktu;

    boolean clickDelete = false;

    int pilihan =1;

    private Calendar calendar;
    private int year, month, day;

    AktivitasKeuanganDatabase aktivitasKeuanganDatabase;

    public AktivitasKeuanganFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aktivitas_keuangan, container, false);

        rvAktivitasKeuangan =(RecyclerView)view.findViewById(R.id.rv_aktivitas_keuangan);
        txtTotalPengeluaran=(TextView)view.findViewById(R.id.total_pengeluaran);
        txtTotalPemasukan = (TextView)view.findViewById(R.id.total_pemasukan);
        radioGroupPilihanWaktu = (RadioGroup)view.findViewById(R.id.radio_grup_waktu);
        imgNoItem = (ImageView)view.findViewById(R.id.no_item_aktivitas);
        listAktivitasKeuangan=new ArrayList<>();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //inisialisasi hari ini
        calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //inisialisasi saldo
        List<SaldoDatabase> saldoSearch = new Select()
                .from(SaldoDatabase.class)
                .where(SaldoDatabase_Table.id.is(1))
                .queryList();
        saldo=saldoSearch.get(0).getSaldo();
        idxQuick=saldoSearch.get(0).getIndexQuick();
        idxAktivitas=saldoSearch.get(0).getIndex();


        //tentang recycler view
        rvAktivitasKeuangan.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvAktivitasKeuangan.setLayoutManager(llm);

        adapter = new AktivitasKeuanganAdapter(listAktivitasKeuangan,this,getContext());

        rvAktivitasKeuangan.setAdapter(adapter);

        callSemuaData();

        radioGroupPilihanWaktu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId) {

                if (checkedId == R.id.radio_semua){
                    pilihan=1;
                    callSemua();
                }else if (checkedId == R.id.radio_pengeluaran_aktivitas){
                    pilihan=2;
                    callPengeluaran();
                }else{
                    pilihan=3;
                    callPemasukan();
                }
            }
        });

    }

    private void callSemuaData() {
        listAktivitasKeuangan = new ArrayList<>();
        totalPemasukan=0;
        totalPengeluaran=0;

        List<AktivitasKeuanganDatabase> aktivitasSearch = new Select()
                .from(AktivitasKeuanganDatabase.class)
                .queryList();
        for (AktivitasKeuanganDatabase list : aktivitasSearch){
            AktivitasKeuanganDatabase aktiv = new AktivitasKeuanganDatabase();
            aktiv.setId(list.getId());
            aktiv.setNamaBarang(list.getNamaBarang());
            aktiv.setHargaBarang(list.getHargaBarang());
            aktiv.setTipe(list.getTipe());
            aktiv.setTanggal(list.getTanggal());
            aktiv.setBulan(list.getBulan());
            aktiv.setTahun(list.getTahun());
            listAktivitasKeuangan.add(aktiv);

            //jumlahkan total pengeluaran/pemasukan
            if (list.getTipe()==0 || list.getTipe()==2 || list.getTipe()==4){
                totalPengeluaran=totalPengeluaran+list.getHargaBarang();
            }else{
                totalPemasukan=totalPemasukan+list.getHargaBarang();
            }

        }

        Collections.reverse(listAktivitasKeuangan);
        callListAktivitasKeuangan(listAktivitasKeuangan);
    }

    private void callBulanData(){
        listAktivitasKeuangan = new ArrayList<>();
        totalPemasukan=0;
        totalPengeluaran=0;

        ConditionGroup condition = ConditionGroup.clause()
                .and(AktivitasKeuanganDatabase_Table.bulan.is(month+1))
                .and(AktivitasKeuanganDatabase_Table.tahun.is(year));

        List<AktivitasKeuanganDatabase> aktivitasSearch = new Select()
                .from(AktivitasKeuanganDatabase.class)
                .where(condition)
                .queryList();

        for (AktivitasKeuanganDatabase list : aktivitasSearch){
            AktivitasKeuanganDatabase aktiv = new AktivitasKeuanganDatabase();
            aktiv.setId(list.getId());
            aktiv.setNamaBarang(list.getNamaBarang());
            aktiv.setHargaBarang(list.getHargaBarang());
            aktiv.setTipe(list.getTipe());
            aktiv.setTanggal(list.getTanggal());
            aktiv.setBulan(list.getBulan());
            aktiv.setTahun(list.getTahun());
            listAktivitasKeuangan.add(aktiv);

            //jumlahkan total pengeluaran/pemasukan
            if (list.getTipe()==0 || list.getTipe()==2 || list.getTipe()==4){
                totalPengeluaran=totalPengeluaran+list.getHargaBarang();
            }else{
                totalPemasukan=totalPemasukan+list.getHargaBarang();
            }

        }

        Collections.reverse(listAktivitasKeuangan);
        callListAktivitasKeuangan(listAktivitasKeuangan);

    }

    private void callHariData(){
        listAktivitasKeuangan = new ArrayList<>();
        totalPemasukan=0;
        totalPengeluaran=0;

        ConditionGroup condition = ConditionGroup.clause()
                .and(AktivitasKeuanganDatabase_Table.tanggal.is(day))
                .and(AktivitasKeuanganDatabase_Table.bulan.is(month+1))
                .and(AktivitasKeuanganDatabase_Table.tahun.is(year));

        List<AktivitasKeuanganDatabase> aktivitasSearch = new Select()
                .from(AktivitasKeuanganDatabase.class)
                .where(condition)
                .queryList();

        for (AktivitasKeuanganDatabase list : aktivitasSearch){
            AktivitasKeuanganDatabase aktiv = new AktivitasKeuanganDatabase();
            aktiv.setId(list.getId());
            aktiv.setNamaBarang(list.getNamaBarang());
            aktiv.setHargaBarang(list.getHargaBarang());
            aktiv.setTipe(list.getTipe());
            aktiv.setTanggal(list.getTanggal());
            aktiv.setBulan(list.getBulan());
            aktiv.setTahun(list.getTahun());
            listAktivitasKeuangan.add(aktiv);

            //jumlahkan total pengeluaran/pemasukan
            if (list.getTipe()==0 || list.getTipe()==2 || list.getTipe()==4){
                totalPengeluaran=totalPengeluaran+list.getHargaBarang();
            }else{
                totalPemasukan=totalPemasukan+list.getHargaBarang();
            }

        }

        Collections.reverse(listAktivitasKeuangan);
        callListAktivitasKeuangan(listAktivitasKeuangan);

    }

    private void callCustomData(int dayPilihan1, int monthPilihan1, int yearPilihan1, int dayPilihan2, int monthPilihan2, int yearPilihan2){
        listAktivitasKeuangan = new ArrayList<>();
        totalPemasukan=0;
        totalPengeluaran=0;

        List<AktivitasKeuanganDatabase> aktivitasSearch = new Select()
                .from(AktivitasKeuanganDatabase.class)
                .queryList();

        for (AktivitasKeuanganDatabase list : aktivitasSearch){

            boolean cek1;
            if ((list.getTanggal()==dayPilihan2)&&(list.getBulan()==monthPilihan2)&&(list.getTahun()==yearPilihan2)){
                cek1=true;
            }else{
                cek1 = cekTanggal(list.getTanggal(),list.getBulan(),list.getTahun(),dayPilihan2,monthPilihan2,yearPilihan2);
            }

            boolean cek2;
            if ((list.getTanggal()==dayPilihan1)&&(list.getBulan()==monthPilihan1)&&(list.getTahun()==yearPilihan1)){
                cek2=true;
            }else{
                cek2 = cekTanggal(dayPilihan1,monthPilihan1,yearPilihan1,list.getTanggal(),list.getBulan(),list.getTahun());
            }


            if ((cek1)&&(cek2)){
                AktivitasKeuanganDatabase aktiv = new AktivitasKeuanganDatabase();
                aktiv.setId(list.getId());
                aktiv.setNamaBarang(list.getNamaBarang());
                aktiv.setHargaBarang(list.getHargaBarang());
                aktiv.setTipe(list.getTipe());
                aktiv.setTanggal(list.getTanggal());
                aktiv.setBulan(list.getBulan());
                aktiv.setTahun(list.getTahun());
                listAktivitasKeuangan.add(aktiv);

                //jumlahkan total pengeluaran/pemasukan
                if (list.getTipe()==0 || list.getTipe()==2 || list.getTipe()==4){
                    totalPengeluaran=totalPengeluaran+list.getHargaBarang();
                }else{
                    totalPemasukan=totalPemasukan+list.getHargaBarang();
                }
            }

        }

        Collections.reverse(listAktivitasKeuangan);
        callListAktivitasKeuangan(listAktivitasKeuangan);



    }

    private void setTotalPengeluaranPemasukan() {
        String totPengeluaran = ((MainActivity)getActivity()).editRupiah(String.valueOf(totalPengeluaran));
        String totPemasukan = ((MainActivity)getActivity()).editRupiah(String.valueOf(totalPemasukan));

        txtTotalPengeluaran.setText(totPengeluaran);
        txtTotalPemasukan.setText(totPemasukan);
    }

    private void callSemua(){
        ArrayList<AktivitasKeuanganDatabase> listAktivitasKeuanganEdit = new ArrayList<>();
        totalPemasukan=0;
        totalPengeluaran=0;

        listAktivitasKeuanganEdit=listAktivitasKeuangan;

        for (int i = 0; i < listAktivitasKeuangan.size(); i++){
            if (listAktivitasKeuangan.get(i).getTipe()==0 || listAktivitasKeuangan.get(i).getTipe()==2 || listAktivitasKeuangan.get(i).getTipe()==4){
                totalPengeluaran+=listAktivitasKeuangan.get(i).getHargaBarang();
            }else {
                totalPemasukan+=listAktivitasKeuangan.get(i).getHargaBarang();
            }
        }

        callListAktivitasKeuangan(listAktivitasKeuanganEdit);
    }

    private void callPengeluaran(){
        ArrayList<AktivitasKeuanganDatabase> listAktivitasKeuanganEdit = new ArrayList<>();
        totalPemasukan=0;
        totalPengeluaran=0;

        for (int i = 0; i < listAktivitasKeuangan.size(); i++){
            if (listAktivitasKeuangan.get(i).getTipe()==0 || listAktivitasKeuangan.get(i).getTipe()==2 || listAktivitasKeuangan.get(i).getTipe()==4){
                listAktivitasKeuanganEdit.add(listAktivitasKeuangan.get(i));
                totalPengeluaran += listAktivitasKeuangan.get(i).getHargaBarang();
            }
        }

        callListAktivitasKeuangan(listAktivitasKeuanganEdit);

    }

    private void callPemasukan(){
        ArrayList<AktivitasKeuanganDatabase> listAktivitasKeuanganEdit = new ArrayList<>();
        totalPemasukan=0;
        totalPengeluaran=0;

        for (int i = 0; i < listAktivitasKeuangan.size(); i++){
            if (listAktivitasKeuangan.get(i).getTipe()==1 || listAktivitasKeuangan.get(i).getTipe()==3 || listAktivitasKeuangan.get(i).getTipe()==5){
                listAktivitasKeuanganEdit.add(listAktivitasKeuangan.get(i));
                totalPemasukan += listAktivitasKeuangan.get(i).getHargaBarang();
            }
        }

        callListAktivitasKeuangan(listAktivitasKeuanganEdit);

    }

    private void callListAktivitasKeuangan(ArrayList<AktivitasKeuanganDatabase> list) {

        if (list.size()==0){
            imgNoItem.setVisibility(View.VISIBLE);
        }else{
            imgNoItem.setVisibility(View.GONE);
        }

        ArrayList<AktivitasKeuanganDatabase> listEdit = sortDate(list);
        setTotalPengeluaranPemasukan();
        adapter.refreshData(listEdit);

    }

    @Override
    public void onClickDelete(AktivitasKeuanganDatabase aktivitasKeuanganDatabase) {

        this.aktivitasKeuanganDatabase = aktivitasKeuanganDatabase;

        ((MainActivity)getActivity()).munculHapusAktivitas(aktivitasKeuanganDatabase);

    }

    public void onTerimaDelete(){
        int idDelete = aktivitasKeuanganDatabase.getId();


        if ((aktivitasKeuanganDatabase.getTipe()==1 || aktivitasKeuanganDatabase.getTipe()==3 || aktivitasKeuanganDatabase.getTipe()==5) && aktivitasKeuanganDatabase.getHargaBarang()>saldo){
            Toast.makeText(getContext(),"Nominal melebihi",Toast.LENGTH_SHORT).show();
        }else{

            boolean cek = true;

            if (aktivitasKeuanganDatabase.getTipe()>=2 && aktivitasKeuanganDatabase.getTipe()<=5){
                if (aktivitasKeuanganDatabase.getTipe()==4 || aktivitasKeuanganDatabase.getTipe()==5){
                    List<BukuHutangDatabase> bukuSearch = new Select()
                            .from(BukuHutangDatabase.class)
                            .where(BukuHutangDatabase_Table.idAktivitas.is(idDelete))
                            .queryList();

                    BukuHutangDatabase buku = bukuSearch.get(0);
                    buku.setMuncul(true);
                    buku.save();
                    //id nya masih hancur euy
                }else{
                    List<BukuHutangDatabase> bukuSearch = new Select()
                            .from(BukuHutangDatabase.class)
                            .where(BukuHutangDatabase_Table.id.is(idDelete))
                            .queryList();
                    BukuHutangDatabase buku = bukuSearch.get(0);

                    if (buku.isMuncul()){
                        buku.delete();
                    }else{
                        cek=false;
                        Toast.makeText(getContext(), "Perintah tidak dapat dilakukan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            /**
             * DARI DANIEL
             */
            WishlistTransactionDatabase record = new Select()
                    .from(WishlistTransactionDatabase.class)
                    .where(WishlistTransactionDatabase_Table.id.eq(aktivitasKeuanganDatabase.getId()))
                    .querySingle();
            if(record != null) {
                if (record.deleteTransaction(aktivitasKeuanganDatabase)) {
                    aktivitasKeuanganDatabase.delete();
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

                    if (pilihanPeriode == 0) {
                        callSemuaData();
                    } else if (pilihanPeriode == 1) {
                        callBulanData();
                    } else if (pilihanPeriode == 2) {
                        callHariData();
                    } else if (pilihanPeriode == 3) {
                        callCustomData(day1, mont1, year1, day2, mont2, year2);
                    }

                    if (pilihan == 1) {
                        callSemua();
                    } else if (pilihan == 2) {
                        callPengeluaran();
                    } else if (pilihan == 3) {
                        callPemasukan();
                    }
                } else {
                    Toast.makeText(getContext(), "Cannot be Deleted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (cek){
                List<AktivitasKeuanganDatabase> aktivitasSearch = new Select()
                        .from(AktivitasKeuanganDatabase.class)
                        .where(AktivitasKeuanganDatabase_Table.id.is(idDelete))
                        .queryList();

                if (aktivitasKeuanganDatabase.getTipe()==0 || aktivitasKeuanganDatabase.getTipe()==2 || aktivitasKeuanganDatabase.getTipe()==4){
                    saldo = saldo+aktivitasKeuanganDatabase.getHargaBarang();
                }else{
                    saldo=saldo-aktivitasKeuanganDatabase.getHargaBarang();
                }

                AktivitasKeuanganDatabase aktiv = aktivitasSearch.get(0);
                aktiv.delete();
                Toast.makeText(getContext(),"deleted",Toast.LENGTH_SHORT).show();

                if (pilihanPeriode==0){
                    callSemuaData();
                }else if (pilihanPeriode==1){
                    callBulanData();
                }else if (pilihanPeriode==2){
                    callHariData();
                }else if (pilihanPeriode==3){
                    callCustomData(day1,mont1,year1,day2,mont2,year2);
                }

                if (pilihan==1){
                    callSemua();
                }else if (pilihan==2){
                    callPengeluaran();
                }else if (pilihan==3){
                    callPemasukan();
                }

                SaldoDatabase saldoDatabase = new SaldoDatabase();
                saldoDatabase.setId(1);
                saldoDatabase.setSaldo(saldo);
                saldoDatabase.setIndex(idxAktivitas);
                saldoDatabase.setIndexQuick(idxQuick);
                saldoDatabase.save();

            }


        }

    }

    public void setelahPilihPeriode(int pilihanPeriode,int dayPilihan1, int monthPilihan1, int yearPilihan1, int dayPilihan2, int monthPilihan2, int yearPilihan2){

        radioGroupPilihanWaktu.clearCheck();
        radioGroupPilihanWaktu.check(R.id.radio_semua);
        pilihan=1;

        day1=dayPilihan1;
        day2=dayPilihan2;
        mont1=monthPilihan1;
        mont2=monthPilihan2;
        year1=yearPilihan1;
        year2=yearPilihan2;

        this.pilihanPeriode=pilihanPeriode;

        if (pilihanPeriode==0){
            callSemuaData();
        }else if (pilihanPeriode==1){
            callBulanData();
        }else if (pilihanPeriode==2){
            callHariData();
        }else if (pilihanPeriode==3){
            callCustomData(dayPilihan1,monthPilihan1,yearPilihan1,dayPilihan2,monthPilihan2,yearPilihan2);
        }
    }

    public ArrayList<AktivitasKeuanganDatabase> sortDate(ArrayList<AktivitasKeuanganDatabase> list){
        boolean cek ;
        ArrayList<AktivitasKeuanganDatabase> listEdit = new ArrayList<>();

        if(list.size()!=0){
            if (list.size()>1){
                for (int i=1; i<list.size();i++){
                    for (int y=0; y<list.size()-i;y++){
                        AktivitasKeuanganDatabase list1 = list.get(y);
                        AktivitasKeuanganDatabase list2 = list.get(y+1);
                        cek = cekTanggal(list1.getTanggal(),list1.getBulan(),list1.getTahun(),list2.getTanggal(),list2.getBulan(),list2.getTahun());
                        if (cek){
                            //swap
                            Log.d("swapcuyy", "swap "+i+", "+list1.getTanggal()+", "+list1.getBulan()+", "+list2.getTanggal()+", "+list2.getBulan());
                            list.set(y,list2);
                            list.set((y+1),list1);
                        }
                    }
                }
            }
        }

        listEdit=list;

        return listEdit;
    }

    public boolean cekTanggal(int tanggal1,int bulan1,int tahun1,int tanggal2,int bulan2,int tahun2){
        //cth : (1) 12-10-2017 , (2) 11-6-2018,  akan menghasilkan true
        // tanggal yang sama akan menghasilkan false
        boolean cek = false;

        if (tahun2>tahun1){
            cek=true;
        }else{
            if (tahun2<tahun1){
                cek=false;
            }else{
                if (bulan2>bulan1){
                    cek=true;
                }else {
                    if (bulan2<bulan1){
                        cek=false;
                    }else{
                        if (tanggal2>tanggal1){
                            cek=true;
                        }
                    }
                }
            }
        }



        return cek;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((MainActivity)getActivity()).removeD(998);
        ((MainActivity)getActivity()).removeD(997);
    }

    public void notifyLagi(){
        adapter.notifyDataSetChanged();
    }

}
