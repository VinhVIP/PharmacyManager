package com.team28.qlnhathuoc.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.CTBanLeDao;
import com.team28.qlnhathuoc.room.dao.MedicineDao;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.room.entity.relations.ThuocWithHoaDon;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MedicineViewModel extends AndroidViewModel {

    private MedicineDao medicineDao;
    private CTBanLeDao ctBanLeDao;
    private Executor executor;

    public LiveData<List<ThuocWithHoaDon>> medicineBillList;
    public LiveData<List<Thuoc>> medicineList;

    public MedicineViewModel(@NonNull Application application) {
        super(application);

        medicineDao = MyDatabase.getInstance(application).medicineDao();
        ctBanLeDao = MyDatabase.getInstance(application).ctBanLeDao();
        executor = Executors.newSingleThreadExecutor();

        medicineBillList = medicineDao.getAllMedicineBill();
        medicineList = medicineDao.getAllMedicine();
    }

    public void insertMedicine(Thuoc medicine) {
        executor.execute(() -> medicineDao.insertMedicine(medicine));
    }

    public void updateMedicine(Thuoc medicine) {
        executor.execute(() -> medicineDao.updateMedicine(medicine));
    }

    public void deleteMedicine(Thuoc medicine) {
        executor.execute(() -> medicineDao.deleteMedicine(medicine));
    }

    public boolean canDeleteMedicine(Thuoc medicine) {
        return !medicineDao.isMedicineSold(medicine.maThuoc);
    }

    public Thuoc getMedicineById(String maThuoc) {
        GetMedicineByIdTask task = new GetMedicineByIdTask(medicineDao);
        try {
            return task.execute(maThuoc).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CTBanLe getCTBanLe(String maThuoc, String soHD) {
        GetCTBanLeTask task = new GetCTBanLeTask(ctBanLeDao);
        try {
            return task.execute(maThuoc, soHD).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GetMedicineByIdTask extends AsyncTask<String, Void, Thuoc> {
        private final MedicineDao dao;

        private GetMedicineByIdTask(MedicineDao dao) {
            this.dao = dao;
        }

        @Override
        protected Thuoc doInBackground(String... strings) {
            return dao.getMedicineById(strings[0]);
        }
    }

    private static class GetCTBanLeTask extends AsyncTask<String, Void, CTBanLe> {
        private final CTBanLeDao dao;

        private GetCTBanLeTask(CTBanLeDao dao) {
            this.dao = dao;
        }

        @Override
        protected CTBanLe doInBackground(String... strings) {
            return dao.getCTBanLe(strings[0], strings[1]);
        }
    }
}
