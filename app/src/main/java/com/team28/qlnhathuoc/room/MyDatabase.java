package com.team28.qlnhathuoc.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.team28.qlnhathuoc.room.dao.BillDao;
import com.team28.qlnhathuoc.room.dao.CTBanLeDao;
import com.team28.qlnhathuoc.room.dao.MedicineDao;
import com.team28.qlnhathuoc.room.dao.PharmacyDao;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Converters;

@Database(entities = {
        NhaThuoc.class,
        Thuoc.class,
        HoaDon.class,
        CTBanLe.class
}, version = 1)
@TypeConverters({Converters.class})
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance;

    public abstract PharmacyDao pharmacyDao();
    public abstract MedicineDao medicineDao();
    public abstract CTBanLeDao ctBanLeDao();
    public abstract BillDao billDao();

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyDatabase.class, "QLNhaThuoc")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
