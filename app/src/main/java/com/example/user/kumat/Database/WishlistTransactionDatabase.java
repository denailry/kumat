package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by denail on 17/07/24.
 */

@Table(database = MyDatabase.class)
public class WishlistTransactionDatabase extends BaseModel {
    @Column @PrimaryKey int id;
    @Column Long wishlistId;

    public WishlistTransactionDatabase() {}

    public WishlistTransactionDatabase(int id, Long wishlistId) {
        this.id = id;
        this.wishlistId = wishlistId;
    }

    public int getId() {
        return id;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public boolean deleteTransaction(AktivitasKeuanganDatabase record) {
        WishlistDatabase wishlist = new Select()
                .from(WishlistDatabase.class)
                .where(WishlistDatabase_Table.id.eq(wishlistId))
                .querySingle();
        if(wishlist != null) {
            SaldoDatabase saldo = new Select()
                    .from(SaldoDatabase.class)
                    .querySingle();
            if(record.getTipe() == 0) {
                if(!wishlist.decTabungan(record.getHargaBarang(), true)) {
                    return false;
                };
                saldo.setSaldo(saldo.getSaldo() + record.getHargaBarang());
            } else {
                if(!wishlist.addTabungan(record.getHargaBarang(), true)) {
                    return false;
                }
                saldo.setSaldo(saldo.getSaldo() - record.getHargaBarang());
            }
            wishlist.save();
            saldo.save();
            this.delete();
            return true;
        }

        return false;
    }
}
