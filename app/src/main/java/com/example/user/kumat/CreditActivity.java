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
            "Trash Bin Icon made by Egor Rumnyantsev",
            "Notebook icon by Madebyoliver",
            "Car icon by Creaticca Creative Agency",
            "Plate fork and knife, Bike icon, Paintbrush and palette, Air Transport, Computer (Modified), Food, Calendar.by Freepik",
            "Star Icon, Gasoline, Bag Briefcase Folder Office Pencil Stationary Carry, Bus (Modified), Book, Motorcycle (Modified), Heart, Telephone, Noodle by anonymous",
            "Clock icon shared by Ocal",
            "Book icon by Charles Riccardi, US",
            " Food Icon by Sebastioan Langer, DE",
            "Photo (modified) icon by DesignBolts",
            "Debt icon by Iconfinder ApS",
            "Flash icon by Iconnice",
            "Gears by anonymous"
    };

    String[] listUrl = {
            "https://www.flaticon.com",
            "https://www.flaticon.com",
            "https://www.flaticon.com",
            "https://www.flaticon.com",
            "https://www.onlinewebfonts.com",
            "https://http://www.clker.com",
            "https://thenounproject.com",
            "https://thenounproject.com",
            "http://m.veryicon.com/",
            "https://www.iconfinder.com",
            "https://www.flaticon.com",
            "http://simpleicon.com"

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
