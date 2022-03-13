package com.team28.qlnhathuoc.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class HoaDon {
    @PrimaryKey
    @NonNull
    public String soHD;
    public Date ngayHD;
    public String maNT;

    public HoaDon(@NonNull String soHD, Date ngayHD, String maNT) {
        this.soHD = soHD;
        this.ngayHD = ngayHD;
        this.maNT = maNT;
    }
}
