package com.team28.qlnhathuoc.room.entity.relations;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.Relation;

import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.room.entity.Thuoc;

import java.io.Serializable;
import java.util.List;

public class HoaDonWithThuoc implements Serializable {
    @Embedded
    public HoaDon hoaDon;
    @Relation(
            parentColumn = "soHD",
            entityColumn = "maThuoc",
            associateBy = @Junction(CTBanLe.class)
    )
    public List<Thuoc> thuocList;

    @Relation(parentColumn = "maNT", entityColumn = "maNT")
    public NhaThuoc pharmacy;

    @Ignore
    public int totalMoney;
}
