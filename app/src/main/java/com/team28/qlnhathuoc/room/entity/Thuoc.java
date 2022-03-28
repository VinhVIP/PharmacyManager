package com.team28.qlnhathuoc.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Thuoc implements Serializable {
    @PrimaryKey
    @NonNull
    public String maThuoc;

    public String tenThuoc;

    public String donViTinh;

    public float donGia;

    public Thuoc(@NonNull String maThuoc, String tenThuoc, String donViTinh, float donGia) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
    }

    @Ignore
    public int soLuong;
}
