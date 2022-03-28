package com.team28.qlnhathuoc.ui.medicine.medicine_form;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.MedicineDao;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MedicineFormViewModel extends AndroidViewModel {

    private MedicineDao medicineDao;
    private Executor executor;

    public MedicineFormViewModel(@NonNull Application application) {
        super(application);

        medicineDao = MyDatabase.getInstance(application).medicineDao();
        executor = Executors.newSingleThreadExecutor();
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
        Task.GetMedicineByIdTask task = new Task.GetMedicineByIdTask(medicineDao);
        try {
            return task.execute(maThuoc).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
