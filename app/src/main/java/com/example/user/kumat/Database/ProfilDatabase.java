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

    @Column @PrimaryKey private int id;
    @Column private String username;
    @Column private String email;
    @Column private double xp;
    @Column private long koin;
    @Column private Blob fotoProfil;
    @Column private boolean bukuHutang;
    @Column private Integer updateId;

    public ProfilDatabase() {}

    public ProfilDatabase(int id, String username, String email, double xp, long koin, Blob fotoProfil, boolean bukuHutang, Integer updateId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.xp = xp;
        this.koin = koin;
        this.fotoProfil = fotoProfil;
        this.bukuHutang = bukuHutang;
        this.updateId = updateId;
    }

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
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

    public void setKoin(long koin) {
        this.koin = koin;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public long getKoin() {
        return koin;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
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
