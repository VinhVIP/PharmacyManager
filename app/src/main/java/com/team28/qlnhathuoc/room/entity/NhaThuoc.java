package com.team28.qlnhathuoc.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

// tenNT là khóa duy nhất (unique)
@Entity(indices = {@Index(value = {"tenNT"}, unique = true)})
public class NhaThuoc implements Serializable {
    @PrimaryKey
    @NonNull
    public String maNT;

    public String tenNT;

    public String diaChi;

    public NhaThuoc(@NonNull String maNT, String tenNT, String diaChi) {
        this.maNT = maNT;
        this.tenNT = tenNT;
        this.diaChi = diaChi;
    }
}
