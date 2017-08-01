package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by User on 24/07/2017.
 */

@Table( database = MyDatabase.class)
public class ShopDatabase extends BaseModel {

    @Column @PrimaryKey private int id;
    @Column private String judul;
    @Column private int foto;
    @Column private int koin;

    public int getKoin() {
        return koin;
    }

    public int getFoto() {
        return foto;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public void setKoin(int koin) {
        this.koin = koin;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
