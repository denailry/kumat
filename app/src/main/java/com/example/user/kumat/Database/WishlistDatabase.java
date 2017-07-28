package com.example.user.kumat.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.user.kumat.IdGen;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.ByteArrayOutputStream;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Created by denail on 17/07/10.
 */

/**
 * TODO 1   : IMAGE ORIENTATION
 * TODO 2   : addTabungan -- isRequireSaldo == false
 *          : problem   : Connect to AktivitasKeuanganDatabase
 *                        Cannot give AktivitasKeuangan new id
 * TODO 3   : decTabungan -- isRequireSaldo == false
 *          : problem   : uangnya mau ke mana? Tabunganku atau ke pengeluaran?
 */

@Table(database = MyDatabase.class)
public class WishlistDatabase extends BaseModel {

    @Column
    @PrimaryKey
    Long id;
    @Column
    String nama;
    @Column
    Integer target;
    @Column
    Integer tabungan;
    @Column
    String persentase;
    @Column
    Blob image;

    private Integer amountFromSaldo;
    private Integer amountFromTabunganku;

    public WishlistDatabase() {
        this(0L, "Create New", 0, 0, null);
    }

    public WishlistDatabase(Long id) {
        this(id, "", 0, 0, null);
    }

    public WishlistDatabase(Long id, String nama, Integer target, Integer tabungan, Bitmap image) {
        this.id = id;
        this.nama = nama;
        this.target = target;
        this.tabungan = tabungan;
        this.amountFromSaldo = 0;
        this.amountFromTabunganku = 0;
        setImage(image);
        setPersentase();
    }

    public Long getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
        setPersentase();
    }

    public Integer getTabungan() {
        return tabungan;
    }

    public Boolean addTabungan(int amount, boolean isRequireSaldo) {
        if(isRequireSaldo) {
            SaldoDatabase saldo = new Select().from(SaldoDatabase.class).querySingle();
            if(saldo.getSaldo() < amount + this.amountFromSaldo) {
                return false;
            }
            this.tabungan= this.tabungan + amount;
            this.amountFromSaldo = this.amountFromSaldo + amount;
            setPersentase();
            return true;
        } else {
            this.tabungan = this.tabungan + amount;
            if(this.id != 1L) { //Bukan "Tabunganku"
                WishlistDatabase tabunganku = new Select()
                        .from(WishlistDatabase.class)
                        .where(WishlistDatabase_Table.id.eq(1L))
                        .querySingle();
                if(tabunganku.getTabungan() < amount + this.amountFromTabunganku) {
                    return false;
                }
                this.tabungan = this.tabungan + amount;
                this.amountFromTabunganku = this.amountFromTabunganku + amount;
                setPersentase();
            }
            return true;
        }
    }

    public Boolean decTabungan(int amount, boolean isRequireSaldo) {
        if(this.tabungan < amount) {
            return false;
        }

        this.tabungan = this.tabungan - amount;
        setPersentase();
        if(isRequireSaldo) {
            this.amountFromSaldo = this.amountFromSaldo - amount;
        } else {
            this.tabungan = this.tabungan - amount;
            if(this.id != 1L) { //Bukan "Tabunganku"
                this.amountFromTabunganku = this.amountFromTabunganku - amount;
            }
        }

        return true;
    }

    public String getPersentase() {
        if(this.target == 0) {
            return this.persentase;
        } else {
            return this.persentase + "%";
        }
    }

    private void setPersentase() {
        if(target == 0) {
            this.persentase = tabungan.toString();
        } else {
            Integer persentase = (this.tabungan *100)/this.target;
            this.persentase = persentase.toString();
        }
    }

    public double getRealPersentase() {
        if(target == 0) {
            return 1000;
        } else {
            Log.d("SORT-P", String.valueOf(this.tabungan) + " " +
                String.valueOf(this.target) + " " +
                String.valueOf(this.tabungan*100/this.target));
            return this.tabungan*100/this.target;
        }
    }

    public void setImage(Bitmap image) {
        if(image != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image = WishlistDatabase.fitImageSize(image);
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            this.image = new Blob(baos.toByteArray());
        }
    }

    public Bitmap getImage() {
        byte[] imageData;
        if(this.image != null) {
            imageData = this.image.getBlob();
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        } else {
            return null;
        }
    }

    public static Bitmap fitImageSize(Bitmap image) {
        double ratio = sqrt(image.getByteCount()/500000);
        if(ratio > 1) {
            double scaledWidth = image.getWidth()/ratio;
            double scaledHeight = image.getHeight()/ratio;
            image = Bitmap.createScaledBitmap(image,
                    (int) scaledWidth,
                    (int) scaledHeight,
                    true);
        }
        return image;
    }

    public void commit() {
        if(amountFromSaldo != null && amountFromSaldo != 0) {
            int tipe;
            if(amountFromSaldo > 0) {
                tipe = 0;
            } else {
                tipe = 1;
            }
            SaldoDatabase saldo = new Select().from(SaldoDatabase.class).querySingle();
            SaldoDatabase newSaldo = new SaldoDatabase();
            newSaldo.setId(saldo.getId());
            newSaldo.setIndex(saldo.getIndex() + 1);
            newSaldo.setIndexQuick(saldo.getIndexQuick());
            newSaldo.setSaldo(saldo.getSaldo()-amountFromSaldo);
            newSaldo.save();

            AktivitasKeuanganDatabase aktivitasKeuangan = new AktivitasKeuanganDatabase();
            aktivitasKeuangan.setId(saldo.getIndex());
            if(this.id == 1) {
                aktivitasKeuangan.setNamaBarang("(T) Tabunganku");
            } else {
                aktivitasKeuangan.setNamaBarang("(T) " + this.nama);
            }
            aktivitasKeuangan.setHargaBarang(abs(amountFromSaldo));
            aktivitasKeuangan.setTipe(tipe);
            int time[] = IdGen.generateTime();
            aktivitasKeuangan.setTanggal(time[0]);
            aktivitasKeuangan.setBulan(time[1] + 1);
            aktivitasKeuangan.setTahun(time[2]);
            aktivitasKeuangan.save();

            WishlistTransactionDatabase transact = new WishlistTransactionDatabase(saldo.getIndex(), this.id);
            transact.save();
        }

        if(amountFromTabunganku != 0) {
            WishlistDatabase tabunganku = new Select()
                    .from(WishlistDatabase.class)
                    .where(WishlistDatabase_Table.id.eq(1L))
                    .querySingle();
            if(amountFromTabunganku > 0) {
                tabunganku.decTabungan(amountFromSaldo, false);
            } else {
                tabunganku.addTabungan(abs(amountFromSaldo), false);
            }
            tabunganku.save();
        }

        this.save();
    }
}
