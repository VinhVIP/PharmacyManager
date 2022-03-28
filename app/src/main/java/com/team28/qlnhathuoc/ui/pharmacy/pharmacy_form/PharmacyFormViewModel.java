package com.team28.qlnhathuoc.ui.pharmacy.pharmacy_form;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.PharmacyDao;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.utils.Task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PharmacyFormViewModel extends AndroidViewModel {

    private PharmacyDao pharmacyDao;

    // Những yêu cầu không cần trả về kết quả gì thì dùng Executor cho gọn
    private Executor executor;

    public PharmacyFormViewModel(@NonNull Application application) {
        super(application);

        pharmacyDao = MyDatabase.getInstance(application).pharmacyDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insertPharmacy(NhaThuoc nhaThuoc) {
        executor.execute(() -> pharmacyDao.insertPharmacy(nhaThuoc));
    }

    public void updatePharmacy(NhaThuoc nhaThuoc) {
        executor.execute(() -> pharmacyDao.updatePharmacy(nhaThuoc));
    }

    public void deletePharmacy(NhaThuoc nhaThuoc) {
        executor.execute(() -> pharmacyDao.deletePharmacy(nhaThuoc));
    }

    public boolean canDeletePharmacy(NhaThuoc nhaThuoc) {
        return !pharmacyDao.isPharmacyHasBill(nhaThuoc.maNT);
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

    public NhaThuoc getPharmacyByName(String tenNT) {
        Task.GetPharmacyByNameTask task = new Task.GetPharmacyByNameTask(pharmacyDao);
        try {
            return task.execute(tenNT).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
