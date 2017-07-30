package com.example.user.kumat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.user.kumat.Database.PengaturanDatabase;
import com.raizlabs.android.dbflow.sql.language.Select;

public class PengaturanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pengaturan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PengaturanDatabase setting = new Select()
                .from(PengaturanDatabase.class)
                .querySingle();
        if(setting == null) {
            setting = new PengaturanDatabase(true, true, true);
            setting.save();
        }

        Log.d("TEST-1", String.valueOf(
                setting.isNotifHomeAllowed() + " " + setting.isNotifPOAllowed() + " " + setting.isSyncAllowed()
        ));
        ((CheckBox) findViewById(R.id.ck_notif_home)).setChecked(!setting.isNotifHomeAllowed());
        ((CheckBox) findViewById(R.id.ck_notif_po)).setChecked(!setting.isNotifPOAllowed());
        ((CheckBox) findViewById(R.id.ck_sync)).setChecked(!setting.isSyncAllowed());
        ((LinearLayout) findViewById(R.id.cnt_sync)).setVisibility(View.GONE);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        PengaturanDatabase setting = new Select()
                .from(PengaturanDatabase.class)
                .querySingle();

        switch(view.getId()) {
            case R.id.ck_notif_home:
                setting.setNotifHomeAllowed(!checked);
                break;
            case R.id.ck_notif_po:
                setting.setNotifPOAllowed(!checked);
                break;
            case R.id.ck_sync:
                setting.setSyncAllowed(!checked);
                break;
        }
        setting.save();
        Log.d("TEST-2", String.valueOf(
                setting.isNotifHomeAllowed() + " " + setting.isNotifPOAllowed() + " " + setting.isSyncAllowed()
        ));
    }
}
