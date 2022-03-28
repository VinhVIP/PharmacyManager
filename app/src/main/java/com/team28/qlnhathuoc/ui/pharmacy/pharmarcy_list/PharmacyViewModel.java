package com.team28.qlnhathuoc.ui.pharmacy.pharmarcy_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.PharmacyDao;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PharmacyViewModel extends AndroidViewModel {

    private PharmacyDao pharmacyDao;
    private Executor executor;

    public final LiveData<List<NhaThuoc>> pharmacyList;

    public PharmacyViewModel(@NonNull Application application) {
        super(application);

        pharmacyDao = MyDatabase.getInstance(application).pharmacyDao();
        executor = Executors.newSingleThreadExecutor();

        pharmacyList = pharmacyDao.getAllPharmacy();
    }
}
