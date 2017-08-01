package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by User on 29/06/2017.
 */
@Table( database = MyDatabase.class)
public class QuickButtonDatabase extends BaseModel {
    @Column @PrimaryKey private int id;
    @Column private int icon;
    @Column private String nama;
    @Column private long harga;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIcon(){
        return icon;
    }
    public void setIcon(int icon){
        this.icon=icon;
    }

    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama=nama;
    }

    public long getHarga(){
        return harga;
    }
    public void setHarga(long harga){
        this.harga=harga;
    }
}
