package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by User on 29/06/2017.
 */

@Table( database = MyDatabase.class)
public class SaldoDatabase extends BaseModel{
    @Column
    @PrimaryKey
    int id;
    @Column
    long saldo;
    @Column
    int index;
    @Column
    int indexQuick;

    public int getIndexQuick() {
        return indexQuick;
    }

    public void setIndexQuick(int indexQuick) {
        this.indexQuick = indexQuick;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public long getSaldo(){
        return saldo;
    }
    public void setSaldo(long saldo){
        this.saldo=saldo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
