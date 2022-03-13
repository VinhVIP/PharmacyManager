package com.team28.qlnhathuoc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BillObj implements Serializable {
    public String soHD;
    public Date ngayHD;

    public PharmacyObj pharmacyObj;
    public List<MedicineObj> medicineObjList;

    public float total;

    public BillObj(String soHD, Date ngayHD, PharmacyObj pharmacyObj, List<MedicineObj> medicineObjList, float total) {
        this.soHD = soHD;
        this.ngayHD = ngayHD;
        this.pharmacyObj = pharmacyObj;
        this.medicineObjList = medicineObjList;
        this.total = total;
    }
}
