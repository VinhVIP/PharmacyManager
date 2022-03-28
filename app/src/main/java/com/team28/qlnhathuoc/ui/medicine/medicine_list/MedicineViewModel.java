package com.team28.qlnhathuoc.ui.medicine.medicine_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.MedicineDao;
import com.team28.qlnhathuoc.room.entity.Thuoc;

import java.util.List;

public class MedicineViewModel extends AndroidViewModel {

    private MedicineDao medicineDao;
    public LiveData<List<Thuoc>> medicineList;

    public MedicineViewModel(@NonNull Application application) {
        super(application);

        medicineDao = MyDatabase.getInstance(application).medicineDao();
        medicineList = medicineDao.getAllMedicine();
    }
}
