package com.team28.qlnhathuoc.ui.bill.bill_create;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.BillDao;
import com.team28.qlnhathuoc.room.dao.MedicineDao;
import com.team28.qlnhathuoc.room.dao.PharmacyDao;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BillCreateViewModel extends AndroidViewModel {

    private MedicineDao medicineDao;
    private BillDao billDao;
    private PharmacyDao pharmacyDao;

    public LiveData<List<Thuoc>> medicinesLiveData;
    public LiveData<List<NhaThuoc>> pharmacyList;

    public MutableLiveData<List<Thuoc>> medicinesChoose = new MutableLiveData<>();

    private MutableLiveData<Float> totalMoney = new MutableLiveData<>(0f);

    public NhaThuoc pharmacyChoose;
    public boolean isShowPreviewBill = false;

    public BillCreateViewModel(@NonNull Application application) {
        super(application);

        medicineDao = MyDatabase.getInstance(application).medicineDao();
        billDao = MyDatabase.getInstance(application).billDao();
        pharmacyDao = MyDatabase.getInstance(application).pharmacyDao();

        medicinesLiveData = medicineDao.getAllMedicine();
        pharmacyList = pharmacyDao.getAllPharmacy();


    }

    private int getBillSize() {
        Task.GetBillsSizeTask task = new Task.GetBillsSizeTask(billDao);
        try {
            int billSize = task.execute().get();
            return billSize;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insertBill() {
        String maHD = "HD" + getBillSize();
        HoaDon bill = new HoaDon(maHD, new Date(), pharmacyChoose.maNT);
        billDao.insertBill(bill);
        for (Thuoc medicine : medicinesChoose.getValue()) {
            if (medicine.soLuong > 0) {
                billDao.insertBillDetail(new CTBanLe(bill.soHD, medicine.maThuoc, medicine.soLuong));
            }
        }
    }

    public void minus(Thuoc medicine) {
        if (medicine.soLuong > 0) {
            medicine.soLuong--;
            totalMoney.postValue(totalMoney.getValue() - medicine.donGia);
        }
    }

    public void plus(Thuoc medicine) {
        medicine.soLuong++;
        totalMoney.postValue(totalMoney.getValue() + medicine.donGia);
    }

    public LiveData<Float> getTotalMoney() {
        return totalMoney;
    }

}
