package com.team28.qlnhathuoc.room.entity.relations;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.Relation;

import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.Thuoc;

import java.io.Serializable;
import java.util.List;

public class ThuocWithHoaDon  implements Serializable {
    @Embedded
    public Thuoc thuoc;
    @Relation(
            parentColumn = "maThuoc",
            entityColumn = "soHD",
            associateBy = @Junction(CTBanLe.class)
    )
    public List<HoaDon> hoaDonList;

    @Ignore
    public int total;
}
