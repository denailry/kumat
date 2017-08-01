package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by User on 29/06/2017.
 */
@Table( database = MyDatabase.class)
public class AktivitasKeuanganDatabase extends BaseModel {
    /**
     * TIPE:
     * 0 = pengeluaran
     * 1 = pemasukan
     * 2 = pengeluaran(piutang)
     * 3 = pemasukan(ngutang)
     * 4 = pengeluaran(bayar utang)
     * 5 = pemasukan(piutang dibayar)
     */

    @Column @PrimaryKey private int id;
    @Column private String namaBarang;
    @Column private int tipe;
    @Column private long hargaBarang;
    @Column private int tanggal;
    @Column private int bulan;
    @Column private int tahun;

    public AktivitasKeuanganDatabase() {
    }

    public AktivitasKeuanganDatabase(int id, String namaBarang, int tipe, long hargaBarang, int tanggal, int bulan, int tahun) {
        this.id = id;
        this.namaBarang = namaBarang;
        this.tipe = tipe;
        this.hargaBarang = hargaBarang;
        this.tanggal = tanggal;
        this.bulan = bulan;
        this.tahun = tahun;
    }

    public int getTipe() {
        return tipe;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }

    public int getBulan() {
        return bulan;
    }

    public long getHargaBarang() {
        return hargaBarang;
    }

    public int getId() {
        return id;
    }

    public int getTahun() {
        return tahun;
    }

    public int getTanggal() {
        return tanggal;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setBulan(int bulan) {
        this.bulan = bulan;
    }

    public void setHargaBarang(long hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    public void setTanggal(int tanggal) {
        this.tanggal = tanggal;
    }
}
