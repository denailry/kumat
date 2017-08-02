package com.example.user.kumat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kumat.Database.ProfilDatabase;
import com.example.user.kumat.Database.ProfilDatabase_Table;
import com.example.user.kumat.Database.WishlistDatabase;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    ImageView imgProfil;
    TextView txtUsername,txtEmail, txtSaldoMinimum;
    ImageView btnEditUsername, btnEditEmail, btnEditSaldoMinimum;

    RelativeLayout backgroundEdit;
    EditText edtUbah;
    Button btnSave;

    Bitmap image;

    int pilihan;

    ProfilDatabase profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_profil);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgProfil = (ImageView)findViewById(R.id.img_foto_profil);
        txtUsername = (TextView)findViewById(R.id.txt_nama_profil);
        txtEmail = (TextView)findViewById(R.id.txt_email_profil);
        txtSaldoMinimum = (TextView)findViewById(R.id.txt_saldo_minimum);
        btnEditEmail = (ImageView)findViewById(R.id.edit_email_profil);
        btnEditUsername = (ImageView)findViewById(R.id.edit_nama_profil);
        btnEditSaldoMinimum = (ImageView)findViewById(R.id.edt_minimum_saldo);

        backgroundEdit = (RelativeLayout)findViewById(R.id.background_profil);
        edtUbah = (EditText) findViewById(R.id.edt_profil);
        btnSave = (Button)findViewById(R.id.btn_save_profil);

        backgroundEdit.setVisibility(View.GONE);

        callProfil();

        btnEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundEdit.setVisibility(View.VISIBLE);
                edtUbah.setHint("Username");
                edtUbah.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                edtUbah.setText("");
                pilihan = 1;

            }
        });

        btnEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundEdit.setVisibility(View.VISIBLE);
                edtUbah.setHint("Email");
                edtUbah.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                edtUbah.setText("");
                pilihan = 2;

            }
        });

        btnEditSaldoMinimum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundEdit.setVisibility(View.VISIBLE);
                edtUbah.setHint("Nominal");
                edtUbah.setInputType(InputType.TYPE_CLASS_NUMBER);
                edtUbah.setText("");
                pilihan = 3 ;
            }
        });

        backgroundEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundEdit.setVisibility(View.GONE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtUbah.getText())){
                    Toast.makeText(ProfilActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if (pilihan==1 || pilihan==2){
                    String data = edtUbah.getText().toString();

                    if (pilihan==1){
                        profil.setUsername(data);
                    }else {
                        profil.setEmail(data);
                    }

                    profil.save();
                    callProfil();

                    backgroundEdit.setVisibility(View.GONE);

                    Toast.makeText(ProfilActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                }else{

                    try {

                        int data = Integer.parseInt(edtUbah.getText().toString());

                        profil.setSaldoMinimum(data);
                        profil.save();
                        callProfil();

                        backgroundEdit.setVisibility(View.GONE);

                        Toast.makeText(ProfilActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                    }catch (NumberFormatException e){
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        imgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(ProfilActivity.this.getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)  {
            Uri fullPhotoUri = data.getData();

            try {
                image = MediaStore.Images.Media.getBitmap(ProfilActivity.this.getContentResolver(),
                        fullPhotoUri);
                image = WishlistDatabase.fitImageSize(image);
                profil.setImage(image);
                imgProfil.setImageBitmap(image);
                profil.save();
            } catch (IOException e) {
                Log.d(getClass().getSimpleName(), "Failed to get image.");
            }
        }
    }

    private void callProfil() {
        profil = new Select()
                .from(ProfilDatabase.class)
                .where(ProfilDatabase_Table.id.is(1))
                .querySingle();

        txtUsername.setText(profil.getUsername());
        txtEmail.setText(profil.getEmail());
        txtSaldoMinimum.setText("Saldo Minimum : "+editRupiah(String.valueOf(profil.getSaldoMinimum())));
        if (profil.getIkon()!= null){
            imgProfil.setImageBitmap(profil.getIkon());
        }
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

        if (backgroundEdit.getVisibility()==View.VISIBLE){
            backgroundEdit.setVisibility(View.GONE);
        }else{
            super.onBackPressed();

        }
    }

    public String editRupiah(String rupiah){
        String edtRuiah = rupiah;
        int length = edtRuiah.length();

        //100000

        while (length>3){
            Log.d("rupiah", "editRupiah: "+length+", "+edtRuiah);
            edtRuiah = edtRuiah.substring(0,(length-3))+"."+edtRuiah.substring((length-3),edtRuiah.length());
            length = length-3;
        }

        edtRuiah = "Rp " + edtRuiah + ",-";

        return edtRuiah;
    }
}
