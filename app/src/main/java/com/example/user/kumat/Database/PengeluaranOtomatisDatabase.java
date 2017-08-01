package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by User on 09/07/2017.
 */

@Table( database = MyDatabase.class)
public class PengeluaranOtomatisDatabase extends BaseModel {

    @Column
    @PrimaryKey
    int id;

    @Column
    String namaBarang;
    @Column
    long hargaBarang;
    @Column
    int tanggal;
    @Column
    int jam;
    @Column
    int menit;

    public void setId(int id) {
        this.id = id;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }


    public void setHargaBarang(long hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public void setJam(int jam) {
        this.jam = jam;
    }

    public void setMenit(int menit) {
        this.menit = menit;
    }


    public void setTanggal(int tanggal) {
        this.tanggal = tanggal;
    }

    public int getTanggal() {
        return tanggal;
    }


    public int getId() {
        return id;
    }

    public long getHargaBarang() {
        return hargaBarang;
    }


    public int getJam() {
        return jam;
    }

    public int getMenit() {
        return menit;
    }

    public String getNamaBarang() {
        return namaBarang;
    }
}
