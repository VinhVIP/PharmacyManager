package com.team28.qlnhathuoc.room.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
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

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] icon;

    public Thuoc(@NonNull String maThuoc, String tenThuoc, String donViTinh, float donGia, byte[] icon) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.icon = icon;
    }

    // Field này sẽ không được tạo
    @Ignore
    public int soLuong;
}
