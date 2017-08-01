package com.example.user.kumat.Fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.kumat.Adapter.BukuHutangAdapter;
import com.example.user.kumat.Database.AktivitasKeuanganDatabase;
import com.example.user.kumat.Database.BukuHutangDatabase;
import com.example.user.kumat.Database.BukuHutangDatabase_Table;
import com.example.user.kumat.Database.ProfilDatabase;
import com.example.user.kumat.Database.ProfilDatabase_Table;
import com.example.user.kumat.Database.SaldoDatabase;
import com.example.user.kumat.Database.SaldoDatabase_Table;
import com.example.user.kumat.Database.ShopDatabase;
import com.example.user.kumat.IdGen;
import com.example.user.kumat.Listener.BukuHutangListener;
import com.example.user.kumat.MainActivity;
import com.example.user.kumat.R;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BukuHutangFragment extends Fragment implements BukuHutangListener{

    ArrayList<BukuHutangDatabase> listBukuHutang;

    RadioGroup groupHutang;
    EditText edtNama,edtNominal;
    Button btnSubmit;
    RecyclerView rvListHutang;

    BukuHutangAdapter adapter;
    
    RelativeLayout belumBeli;
    RelativeLayout sudahBeli;

    int pilihan=0;
    int idHutang;

    long saldo;
    int idAktivitas;
    int idQuick;

    int thisDay;
    int thisMonth;
    int thisYear;

    ImageView imgNoItem;

    public BukuHutangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_buku_hutang, container, false);
        groupHutang = (RadioGroup)v.findViewById(R.id.radio_radio_grup_hutang);
        edtNama=(EditText)v.findViewById(R.id.edit_nama_orang);
        edtNominal=(EditText)v.findViewById(R.id.edit_nominal_hutang);
        btnSubmit =(Button)v.findViewById(R.id.btn_submit_hutang);
        rvListHutang = (RecyclerView)v.findViewById(R.id.rv_hutang);
        imgNoItem = (ImageView)v.findViewById(R.id.no_item_hutang);
        sudahBeli = (RelativeLayout)v.findViewById(R.id.sudah_beli);
        belumBeli = (RelativeLayout)v.findViewById(R.id.belum_beli); 

        listBukuHutang = new ArrayList<>();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        cekSudahBeli();

//        thisDay = ((MainActivity)getActivity()).thisDay;
//        thisMonth = ((MainActivity)getActivity()).thisMonth;
//        thisYear = ((MainActivity)getActivity()).thisYear;

        thisDay = parseDay(IdGen.generateTimeId());
        thisMonth = parseMonth(IdGen.generateTimeId());
        thisYear = parseYear(IdGen.generateTimeId());

        rvListHutang.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvListHutang.setLayoutManager(llm);

        callSaldo();

        adapter = new BukuHutangAdapter(listBukuHutang,this,getContext());
        rvListHutang.setAdapter(adapter);

        callHutangData();

        groupHutang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId==R.id.radio_hutang){
                    pilihan=0;
                    callHutangData();
                }else{
                    pilihan=1;
                    callPiutangData();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtNama.getText())||TextUtils.isEmpty(edtNominal.getText())){
                    Toast.makeText(getContext(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    try {

                        String namaOrang = edtNama.getText().toString();
                        long nominalHarga = Integer.parseInt(edtNominal.getText().toString());

                        if (pilihan==0){
                            BukuHutangDatabase bukuHutang = new BukuHutangDatabase();
                            bukuHutang.setId(idAktivitas);
                            bukuHutang.setNama(namaOrang);
                            bukuHutang.setNominal(nominalHarga);
                            bukuHutang.setTipe(pilihan);
                            bukuHutang.setMuncul(true);
                            bukuHutang.setIdAktivitas(-1);
                            bukuHutang.save();

                            Log.d("hutang", "onClick: "+idAktivitas);

                            edtNama.setText("");
                            edtNominal.setText("");

                            //pemasukan
                            pemasukan(3,"(Hutang) "+namaOrang,nominalHarga,thisDay,thisMonth,thisYear);
                            callHutangData();

                        }else{
                            if (nominalHarga>saldo){
                                Toast.makeText(getContext(), "Nominal melebihi saldo", Toast.LENGTH_SHORT).show();
                            }else{
                                BukuHutangDatabase bukuHutang = new BukuHutangDatabase();
                                bukuHutang.setId(idAktivitas);
                                bukuHutang.setNama(namaOrang);
                                bukuHutang.setNominal(nominalHarga);
                                bukuHutang.setTipe(pilihan);
                                bukuHutang.setMuncul(true);
                                bukuHutang.setIdAktivitas(-1);
                                bukuHutang.save();

                                Log.d("hutang", "onClick: "+idAktivitas);

                                edtNama.setText("");
                                edtNominal.setText("");

                                //pengeluaran
                                pengeluaran(2, "(Piutang) "+namaOrang,nominalHarga,thisDay,thisMonth,thisYear);

                                callPiutangData();

                            }
                        }


                    }catch (NumberFormatException e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void cekSudahBeli() {
        ProfilDatabase profil = new Select()
                .from(ProfilDatabase.class)
                .where(ProfilDatabase_Table.username.is(((MainActivity)getActivity()).usernameAktif))
                .querySingle();
        if (profil.isBukuHutang()){
            belumBeli.setVisibility(View.GONE);
            sudahBeli.setVisibility(View.VISIBLE);
        }else{
            belumBeli.setVisibility(View.VISIBLE);
            sudahBeli.setVisibility(View.GONE);
        }

    }

    private void callSaldo() {
        List<SaldoDatabase> saldoSearch = new Select()
                .from(SaldoDatabase.class)
                .where(SaldoDatabase_Table.id.is(1))
                .queryList();

        SaldoDatabase sald;
        sald = saldoSearch.get(0);

        saldo = sald.getSaldo();
        idAktivitas = sald.getIndex();
        idQuick = sald.getIndexQuick();

        Log.d("hutang", "callSaldo: "+idAktivitas);
    }

    private void callPiutangData() {
        listBukuHutang = new ArrayList<>();

        List<BukuHutangDatabase> hutangSearch= new Select()
                .from(BukuHutangDatabase.class)
                .queryList();
        for (BukuHutangDatabase list : hutangSearch){
            BukuHutangDatabase buku = new BukuHutangDatabase();
            buku.setId(list.getId());
            buku.setNama(list.getNama());
            buku.setNominal(list.getNominal());
            buku.setTipe(list.getTipe());
            buku.setMuncul(list.isMuncul());
            buku.setIdAktivitas(list.getIdAktivitas());
            if (list.getTipe()==1 && list.isMuncul()){
                listBukuHutang.add(buku);
            }
        }

        if (hutangSearch.size()==0){
            idHutang=1;
        }else{
            Collections.reverse(listBukuHutang);
            Collections.reverse(hutangSearch);
            idHutang=hutangSearch.get(0).getId()+1;
        }

        if (listBukuHutang.size()==0){
            imgNoItem.setVisibility(View.VISIBLE);
        }else{
            imgNoItem.setVisibility(View.GONE);
        }

        //call adapter
        adapter.refreshData(listBukuHutang);
    }
    private void callHutangData() {
        listBukuHutang = new ArrayList<>();

        List<BukuHutangDatabase> hutangSearch= new Select()
                .from(BukuHutangDatabase.class)
                .queryList();
        for (BukuHutangDatabase list : hutangSearch){
            BukuHutangDatabase buku = new BukuHutangDatabase();
            buku.setId(list.getId());
            buku.setNama(list.getNama());
            buku.setNominal(list.getNominal());
            buku.setTipe(list.getTipe());
            buku.setMuncul(list.isMuncul());
            buku.setIdAktivitas(list.getIdAktivitas());
            if (list.getTipe()==0 && list.isMuncul()){
                listBukuHutang.add(buku);
            }
        }

        if (hutangSearch.size()==0){
            idHutang=1;
        }else{
            Collections.reverse(listBukuHutang);
            Collections.reverse(hutangSearch);
            idHutang=hutangSearch.get(0).getId()+1;
        }

        if (listBukuHutang.size()==0){
            imgNoItem.setVisibility(View.VISIBLE);
        }else{
            imgNoItem.setVisibility(View.GONE);
        }

        //call adapter
        adapter.refreshData(listBukuHutang);
    }

    @Override
    public void onClickBayar(BukuHutangDatabase bukuHutangDatabase) {
        int tipe = bukuHutangDatabase.getTipe();

        if (tipe==0){
            //pengeluaran
            if (bukuHutangDatabase.getNominal()>saldo){
                Toast.makeText(getContext(), "Nominal melebihi saldo", Toast.LENGTH_SHORT).show();
            }else{

                List<BukuHutangDatabase> buku = new Select()
                        .from(BukuHutangDatabase.class)
                        .where(BukuHutangDatabase_Table.id.is(bukuHutangDatabase.getId()))
                        .queryList();
                BukuHutangDatabase bukuHutang = buku.get(0);
                bukuHutang.setMuncul(false);
                bukuHutang.setIdAktivitas(idAktivitas);
                bukuHutang.save();

                Log.d("hutang", "onClickBayar: "+bukuHutang.getId()+", "+bukuHutang.getIdAktivitas());

                Toast.makeText(getContext(), "Hutang telah dibayar", Toast.LENGTH_SHORT).show();

                pengeluaran(4,"(Bayar) "+bukuHutangDatabase.getNama(),bukuHutangDatabase.getNominal(),thisDay,thisMonth,thisYear);

                callHutangData();
            }
        }else{
            //pemasukan

            List<BukuHutangDatabase> buku = new Select()
                    .from(BukuHutangDatabase.class)
                    .where(BukuHutangDatabase_Table.id.is(bukuHutangDatabase.getId()))
                    .queryList();
            BukuHutangDatabase bukuHutang = buku.get(0);
            bukuHutang.setMuncul(false);
            bukuHutang.setIdAktivitas(idAktivitas);
            bukuHutang.save();

            pemasukan(5,"(Dibayar) "+bukuHutangDatabase.getNama(),bukuHutangDatabase.getNominal(),thisDay,thisMonth,thisYear);

            Toast.makeText(getContext(), "Piutang telah dibayar", Toast.LENGTH_SHORT).show();

            callPiutangData();

        }

    }

    public void pengeluaran(int tipe,String namaBarang, long hargaBarang,int day, int month, int year){
        AktivitasKeuanganDatabase aktivitasKeuangan = new AktivitasKeuanganDatabase();
        aktivitasKeuangan.setId(idAktivitas);
        aktivitasKeuangan.setNamaBarang(namaBarang);
        aktivitasKeuangan.setHargaBarang(hargaBarang);
        aktivitasKeuangan.setTipe(tipe);
        aktivitasKeuangan.setTanggal(day);
        aktivitasKeuangan.setBulan(month+1);
        aktivitasKeuangan.setTahun(year);
        aktivitasKeuangan.save();

        Log.d("bukuu", "pengeluaran: "+day+","+month+","+year);

        idAktivitas++;

        saldo = saldo-hargaBarang;
        SaldoDatabase sald = new SaldoDatabase();
        sald.setId(1);
        sald.setIndex(idAktivitas);
        sald.setIndexQuick(idQuick);
        sald.setSaldo(saldo);
        sald.save();

    }

    public void pemasukan(int tipe,String namaBarang, long hargaBarang,int day, int month, int year){
        AktivitasKeuanganDatabase aktivitasKeuangan = new AktivitasKeuanganDatabase();
        aktivitasKeuangan.setId(idAktivitas);
        aktivitasKeuangan.setNamaBarang(namaBarang);
        aktivitasKeuangan.setHargaBarang(hargaBarang);
        aktivitasKeuangan.setTipe(tipe);
        aktivitasKeuangan.setTanggal(day);
        aktivitasKeuangan.setBulan(month+1);
        aktivitasKeuangan.setTahun(year);
        aktivitasKeuangan.save();

        idAktivitas++;

        saldo = saldo+hargaBarang;
        SaldoDatabase sald = new SaldoDatabase();
        sald.setId(1);
        sald.setIndex(idAktivitas);
        sald.setIndexQuick(idQuick);
        sald.setSaldo(saldo);
        sald.save();

    }

    private int parseDay(long timeId) {
        long x = timeId % 10;
        timeId = timeId / 10;
        x = x  + (timeId % 10) * 10;

        return (int) x;
    }

    private int parseMonth(long timeId) {
        timeId = timeId/100;
        long x = timeId % 10;
        timeId = timeId/10;
        x = x + (timeId % 10) * 10;

        return (int) x;
    }

    private int parseYear(long timeId) {
        timeId = timeId/10000;

        long x = timeId;

        return (int) x;
    }
}
