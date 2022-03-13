package com.team28.qlnhathuoc.model;

import com.team28.qlnhathuoc.room.entity.NhaThuoc;

import java.io.Serializable;

public class PharmacyObj implements Serializable {
    public String maNT;
    public String tenNT;
    public String diaChi;

    public PharmacyObj(String maNT, String tenNT, String diaChi) {
        this.maNT = maNT;
        this.tenNT = tenNT;
        this.diaChi = diaChi;
    }

    public PharmacyObj(NhaThuoc pharmacy) {
        this.maNT = pharmacy.maNT;
        this.tenNT = pharmacy.tenNT;
        this.diaChi = pharmacy.diaChi;
    }
}
