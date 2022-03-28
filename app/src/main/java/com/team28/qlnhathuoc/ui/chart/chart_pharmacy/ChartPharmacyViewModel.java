package com.team28.qlnhathuoc.ui.chart.chart_pharmacy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team28.qlnhathuoc.room.MyDatabase;
import com.team28.qlnhathuoc.room.dao.BillDao;
import com.team28.qlnhathuoc.room.dao.CTBanLeDao;
import com.team28.qlnhathuoc.room.dao.PharmacyDao;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;
import com.team28.qlnhathuoc.utils.Helpers;
import com.team28.qlnhathuoc.utils.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ChartPharmacyViewModel extends AndroidViewModel {

    private BillDao billDao;
    private PharmacyDao pharmacyDao;
    private CTBanLeDao ctBanLeDao;

    private List<NhaThuoc> pharmacyList;
    private Map<NhaThuoc, List<HoaDonWithThuoc>> mapAll;

    public MutableLiveData<Map<NhaThuoc, List<Float>>> mapPharmacy = new MutableLiveData<>();

    public ChartPharmacyViewModel(@NonNull Application application) {
        super(application);

        billDao = MyDatabase.getInstance(application).billDao();
        pharmacyDao = MyDatabase.getInstance(application).pharmacyDao();
        ctBanLeDao = MyDatabase.getInstance(application).ctBanLeDao();

        getPharmacyList();
        getBillOfMedicines();
    }

    private void getPharmacyList() {
        Task.GetListPharmacyTask task = new Task.GetListPharmacyTask(pharmacyDao);
        try {
            pharmacyList = task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getBillOfMedicines() {
        mapAll = new HashMap<>();

        for (NhaThuoc pharmacy : pharmacyList) {
            Task.GetBillsOfPharmacyTask task = new Task.GetBillsOfPharmacyTask(billDao);
            try {
                List<HoaDonWithThuoc> billsOfPharmacy = task.execute(pharmacy.maNT).get();
                mapAll.put(pharmacy, billsOfPharmacy);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void filter(int year) {
        Map<NhaThuoc, List<Float>> mapYear = new HashMap<>();

        for (NhaThuoc pharmacy : mapAll.keySet()) {
            mapYear.put(pharmacy, new ArrayList<>());
            for (int quarter = 1; quarter <= 4; quarter++) {
                float total = 0f;
                for (HoaDonWithThuoc bill : mapAll.get(pharmacy)) {
                    if (Helpers.isDateOfQuarterYear(bill.hoaDon.ngayHD, quarter, year)) {
                        for (Thuoc medicine : bill.thuocList) {
                            Task.GetCTBanLeTask task = new Task.GetCTBanLeTask(ctBanLeDao);
                            try {
                                CTBanLe ctBanLe = task.execute(medicine.maThuoc, bill.hoaDon.soHD).get();
                                total += ctBanLe.soLuong * medicine.donGia;
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                mapYear.get(pharmacy).add(total);
            }
        }
        mapPharmacy.postValue(mapYear);
    }
}
