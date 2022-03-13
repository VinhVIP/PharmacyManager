package com.team28.qlnhathuoc.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.BillDao;
import com.team28.qlnhathuoc.room.dao.CTBanLeDao;
import com.team28.qlnhathuoc.room.dao.PharmacyDao;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;
import com.team28.qlnhathuoc.utils.Task;

import java.util.List;

public class BillViewModel extends AndroidViewModel {

    private BillDao billDao;
    private CTBanLeDao ctBanLeDao;
    private PharmacyDao pharmacyDao;

    public LiveData<List<HoaDonWithThuoc>> billsList;


    public BillViewModel(@NonNull Application application) {
        super(application);

        billDao = MyDatabase.getInstance(application).billDao();
        ctBanLeDao = MyDatabase.getInstance(application).ctBanLeDao();
        pharmacyDao = MyDatabase.getInstance(application).pharmacyDao();

        billsList = billDao.getAllBills();
    }

    public CTBanLe getCTBanLe(String maThuoc, String soHD) {
        Task.GetCTBanLeTask task = new Task.GetCTBanLeTask(ctBanLeDao);
        try {
            return task.execute(maThuoc, soHD).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public NhaThuoc getPharmacyById(String maNT) {
        Task.GetPharmacyByIdTask task = new Task.GetPharmacyByIdTask(pharmacyDao);
        try {
            return task.execute(maNT).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
