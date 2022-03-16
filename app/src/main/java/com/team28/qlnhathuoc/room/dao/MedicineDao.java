package com.team28.qlnhathuoc.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.room.entity.relations.ThuocWithHoaDon;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    void insertMedicine(Thuoc thuoc);

    @Update
    void updateMedicine(Thuoc thuoc);

    @Delete
    void deleteMedicine(Thuoc thuoc);

    @Query("SELECT * FROM Thuoc WHERE maThuoc LIKE :maThuoc")
    Thuoc getMedicineById(String maThuoc);

    @Query("SELECT * FROM Thuoc")
    LiveData<List<ThuocWithHoaDon>> getAllMedicineBill();

    @Query("SELECT * FROM Thuoc")
    LiveData<List<Thuoc>> getAllMedicine();

    @Query("SELECT EXISTS(SELECT * FROM CTBanLe WHERE maThuoc LIKE :maThuoc)")
    boolean isMedicineSold(String maThuoc);
}
