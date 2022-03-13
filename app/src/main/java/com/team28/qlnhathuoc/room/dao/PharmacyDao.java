package com.team28.qlnhathuoc.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.team28.qlnhathuoc.room.entity.NhaThuoc;

import java.util.List;

@Dao
public interface PharmacyDao {
    @Insert
    void insertPharmacy(NhaThuoc nhaThuoc);

    @Update
    void updatePharmacy(NhaThuoc nhaThuoc);

    @Delete
    void deletePharmacy(NhaThuoc nhaThuoc);

    @Query("SELECT * FROM NHATHUOC WHERE maNT LIKE :maNT")
    NhaThuoc getPharmacyById(String maNT);

    @Query("SELECT * FROM NHATHUOC WHERE tenNT LIKE :tenNT")
    NhaThuoc getPharmacyByName(String tenNT);

    @Query("SELECT * FROM NhaThuoc")
    LiveData<List<NhaThuoc>> getAllPharmacy();

    @Query("SELECT EXISTS(SELECT * FROM HoaDon WHERE maNT LIKE :maNT)")
    boolean isPharmacyHasBill(String maNT);

}
