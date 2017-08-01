package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by User on 16/07/2017.
 */

@Table( database = MyDatabase.class)
public class BukuHutangDatabase extends BaseModel {

    @Column @PrimaryKey private int id;
    @Column private String nama;
    @Column private long nominal;
    @Column private int tipe; // tipe 0 = hutang, 1 = piutang
    @Column private boolean muncul;
    @Column private int idAktivitas;

    public BukuHutangDatabase() {
    }

    public BukuHutangDatabase(int id, String nama, long nominal, int tipe, boolean muncul, int idAktivitas) {
        this.id = id;
        this.nama = nama;
        this.nominal = nominal;
        this.tipe = tipe;
        this.muncul = muncul;
        this.idAktivitas = idAktivitas;
    }

    public void setIdAktivitas(int idAktivitas) {
        this.idAktivitas = idAktivitas;
    }

    public int getIdAktivitas() {
        return idAktivitas;
    }

    public void setMuncul(boolean muncul) {
        this.muncul = muncul;
    }

    public boolean isMuncul() {
        return muncul;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNominal(long nominal) {
        this.nominal = nominal;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }

    public int getId() {
        return id;
    }

    public long getNominal() {
        return nominal;
    }

    public int getTipe() {
        return tipe;
    }

    public String getNama() {
        return nama;
    }
}
