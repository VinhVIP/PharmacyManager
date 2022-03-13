package com.team28.qlnhathuoc.model;

import com.team28.qlnhathuoc.room.entity.Thuoc;

import java.io.Serializable;

public class MedicineObj implements Serializable {
    public String maThuoc;
    public String tenThuoc;
    public String dvt;
    public float donGia;

    public int soLuong;

    public MedicineObj(String maThuoc, String tenThuoc, String dvt, float donGia, int soLuong) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.dvt = dvt;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public MedicineObj(Thuoc medicine) {
        this.maThuoc = medicine.maThuoc;
        this.tenThuoc = medicine.tenThuoc;
        this.dvt = medicine.donViTinh;
        this.donGia = medicine.donGia;
    }

    public Thuoc toThuoc() {
        Thuoc medicine = new Thuoc(maThuoc, tenThuoc, dvt, donGia);
        medicine.soLuong = soLuong;
        return medicine;
    }
}
