package com.team28.qlnhathuoc.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;

import java.util.List;

@Dao
public interface BillDao {

    @Query("SELECT * FROM HoaDon")
    LiveData<List<HoaDonWithThuoc>> getAllBills();

    @Query("SELECT COUNT(*) FROM HOADON")
    int getBillsSize();

    @Insert
    void insertBill(HoaDon hoaDon);

    @Insert
    void insertBillDetail(CTBanLe ctBanLe);
}
