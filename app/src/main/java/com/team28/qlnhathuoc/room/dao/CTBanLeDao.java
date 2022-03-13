package com.team28.qlnhathuoc.room.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.team28.qlnhathuoc.room.entity.CTBanLe;

@Dao
public interface CTBanLeDao {

    @Query("SELECT * FROM CTBanLe WHERE maThuoc LIKE :maThuoc AND soHD LIKE :soHD")
    CTBanLe getCTBanLe(String maThuoc, String soHD);
}
