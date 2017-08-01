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

    @Column @PrimaryKey private int id;
    @Column private String namaBarang;
    @Column private long hargaBarang;
    @Column private int tanggal;
    @Column private int jam;
    @Column private int menit;

    public PengeluaranOtomatisDatabase() {}

    public PengeluaranOtomatisDatabase(int id, String namaBarang, long hargaBarang, int tanggal, int jam, int menit) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
        this.tanggal = tanggal;
        this.jam = jam;
        this.menit = menit;
    }

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
