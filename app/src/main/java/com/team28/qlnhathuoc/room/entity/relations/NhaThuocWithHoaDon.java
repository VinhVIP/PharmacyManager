package com.team28.qlnhathuoc.room.entity.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;

import java.io.Serializable;
import java.util.List;

public class NhaThuocWithHoaDon implements Serializable {
    @Embedded
    public NhaThuoc nhaThuoc;
    @Relation(parentColumn = "maNT", entityColumn = "maNT")
    public List<HoaDon> hoaDonList;
}
