package com.team28.qlnhathuoc.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"soHD", "maThuoc"})
public class CTBanLe {
    @NonNull
    public String soHD;
    @NonNull
    public String maThuoc;
    public int soLuong;

    public CTBanLe(@NonNull String soHD, @NonNull String maThuoc, int soLuong) {
        this.soHD = soHD;
        this.maThuoc = maThuoc;
        this.soLuong = soLuong;
    }

}
