package com.team28.qlnhathuoc.ui.chart.chart_medicine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.CTBanLeDao;
import com.team28.qlnhathuoc.room.dao.MedicineDao;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.relations.ThuocWithHoaDon;
import com.team28.qlnhathuoc.utils.Task;

import java.util.List;

public class ChartMedicineViewModel extends AndroidViewModel {

    private MedicineDao medicineDao;
    private CTBanLeDao ctBanLeDao;

    public LiveData<List<ThuocWithHoaDon>> medicineBillList;

    public ChartMedicineViewModel(@NonNull Application application) {
        super(application);

        medicineDao = MyDatabase.getInstance(application).medicineDao();
        ctBanLeDao = MyDatabase.getInstance(application).ctBanLeDao();

        medicineBillList = medicineDao.getAllMedicineBill();
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
}
