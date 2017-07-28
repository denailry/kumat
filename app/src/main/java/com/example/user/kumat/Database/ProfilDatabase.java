package com.example.user.kumat.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.ByteArrayOutputStream;

/**
 * Created by User on 23/07/2017.
 */

@Table( database = MyDatabase.class)
public class ProfilDatabase extends BaseModel {

    @Column
    @PrimaryKey
    int id;
    @Column
    String Username;
    @Column
    String email;
    @Column
    double xp;
    @Column
    int koin;
    @Column
    Blob fotoProfil;
    @Column
    boolean bukuHutang;
    @Column
    int lvl;
    @Column
    private Integer updateId;

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBukuHutang(boolean bukuHutang) {
        this.bukuHutang = bukuHutang;
    }

    public boolean isBukuHutang() {
        return bukuHutang;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFotoProfil(Blob fotoProfil) {
        this.fotoProfil = fotoProfil;
    }

    public void setKoin(int koin) {
        this.koin = koin;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public Blob getFotoProfil() {
        return fotoProfil;
    }

    public double getXp() {
        return xp;
    }

    public int getKoin() {
        return koin;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return Username;
    }
    public void setImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        image = ProfilDatabase.fitImageSize(image);

        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        this.fotoProfil = new Blob(baos.toByteArray());
    }

    public static Bitmap fitImageSize(Bitmap image) {
        while(image.getByteCount() > 5000000) {
            Integer width = image.getWidth();
            Integer height = image.getHeight();

            image = Bitmap.createScaledBitmap(image, width/2, height/2, true);
        }

        return image;
    }

    public Bitmap getIkon() {
        byte[] imageData;

        if(this.fotoProfil != null) {
            imageData = this.fotoProfil.getBlob();
        } else {
            return null;
        }

        Bitmap image;
        if(imageData != null) {
            image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            return image;
        } else {
            return null;
        }
    }
}
