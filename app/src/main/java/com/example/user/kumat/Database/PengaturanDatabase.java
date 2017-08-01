package com.example.user.kumat.Database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by denail on 17/07/30.
 */

@Table( database = MyDatabase.class)
public class PengaturanDatabase extends BaseModel {

    @PrimaryKey @Column private int id;
    @Column private boolean isNotifHomeAllowed;
    @Column private boolean isNotifPOAllowed;
    @Column private boolean isSyncAllowed;

    public PengaturanDatabase() {
        this.id = 1;
        this.isNotifHomeAllowed = true;
        this.isNotifPOAllowed = true;
        this.isSyncAllowed = true;
    }

    public PengaturanDatabase(boolean isNotifHomeAllowed, boolean isNotifPOAllowed, boolean isSyncAllowed) {
        this.id = 1;
        this.isNotifHomeAllowed = isNotifHomeAllowed;
        this.isNotifPOAllowed = isNotifPOAllowed;
        this.isSyncAllowed = isSyncAllowed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNotifHomeAllowed() {
        return isNotifHomeAllowed;
    }

    public void setNotifHomeAllowed(boolean notifHomeAllowed) {
        isNotifHomeAllowed = notifHomeAllowed;
    }

    public boolean isNotifPOAllowed() {
        return isNotifPOAllowed;
    }

    public void setNotifPOAllowed(boolean notifPOAllowed) {
        isNotifPOAllowed = notifPOAllowed;
    }

    public boolean isSyncAllowed() {
        return isSyncAllowed;
    }

    public void setSyncAllowed(boolean syncAllowed) {
        isSyncAllowed = syncAllowed;
    }
}
