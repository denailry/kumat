package com.example.user.kumat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.user.kumat.Adapter.CreditAdapter;

public class CreditActivity extends AppCompatActivity {

    RecyclerView rvCredit;
    CreditAdapter adapter;

    String[] listJudul = {
            "google"
    };

    String[] listUrl = {
            "http://www.google.com"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Credits");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvCredit =(RecyclerView)findViewById(R.id.rv_credit);
        rvCredit.setHasFixedSize(true);
        rvCredit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new CreditAdapter(listJudul,listUrl,getApplicationContext());
        rvCredit.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
