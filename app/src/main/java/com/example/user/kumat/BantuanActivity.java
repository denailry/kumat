package com.example.user.kumat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.user.kumat.Adapter.BantuanAdapter;

public class BantuanActivity extends AppCompatActivity {

    RecyclerView rvBantuan;
    BantuanAdapter adapter;

    String[] judul ={
            "Info Penting",
            "Input Data Keuangan",
            "Buku Aktivitas Keuangan",
            "Wishlist",
            "Quick Button",
            "Pengeluaran Otomatis",
            "Buku Hutang",
            "Credit"
    } ;

    String[] isi = {
            "Kumat! (Aku Hemat) adalah aplikasi pencatatan keuangan yang interaktif dan memiliki fitur-fitur yang sangat membantu anda untuk bisa hidup hemat",
            "Pada fitur ini pengguna dapat menginput Pengeluaran/Pemasukan sesuai data-data yang diinginkan pengguna, pada fitur ini juga menampilkan saldo total pengguna.",
            "Pada fitur ini pengguna dapat melihat catatan keuangan yang telah digunakan oleh pengguna. pengguna dapat melihat aktivitas keuangannya semua,perbulan,perhari,maupun kostumisasi",
            "Pada fitur ini pengguna dapat menabung tanpa target dan juga dapat menabung hal hal yang diinginkan pengguna dilengkapi targetnya",
            "Pada fitur ini pengguna dapat menginput data pengeluaran yang sifatnya konstan tapi tidak tergantung waktu. fitur ini akan ditampilkan di widget homescreen (jika pengguna menaruhnya) dan juga ada di fitur Input Data Keuangan",
            "Pada fitur ini pengguna dapat menginput data pengeluaran yang sifatnya konstan di harga dan waktu. pengeluaran tersebut akan otomatis tereksekusi dan diberitahukan melalui notifikasi pada smartphone anda",
            "Fitur ini adalah salah satu fitur yang dapat dibeli menggunakan koin di Kumat Shop. Pada fitur ini pengguna dapat menginput  data Hutang maupun Piutang pengguna",
            "Go to Credit"
    };

    RelativeLayout backgroundIklan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bantuan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backgroundIklan = (RelativeLayout)findViewById(R.id.background_iklan);

        backgroundIklan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundIklan.setVisibility(View.GONE);
            }
        });

        rvBantuan = (RecyclerView)findViewById(R.id.rv_bantuan);
        rvBantuan.setHasFixedSize(true);
        rvBantuan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new BantuanAdapter(judul,isi,getApplicationContext());

        rvBantuan.setAdapter(adapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if (backgroundIklan.getVisibility()==View.VISIBLE){
            backgroundIklan.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

}
