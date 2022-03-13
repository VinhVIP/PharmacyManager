package com.team28.qlnhathuoc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.activity.BillCreateActivity;
import com.team28.qlnhathuoc.activity.BillDetailActivity;
import com.team28.qlnhathuoc.adapter.BillAdapter;
import com.team28.qlnhathuoc.databinding.FragmentBillBinding;
import com.team28.qlnhathuoc.model.BillObj;
import com.team28.qlnhathuoc.model.MedicineObj;
import com.team28.qlnhathuoc.model.PharmacyObj;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.room.entity.relations.HoaDonWithThuoc;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.viewmodel.BillViewModel;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment {

    private FragmentBillBinding binding;

    private BillAdapter adapter;

    private BillViewModel viewModel;

    public BillFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(BillViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BillAdapter(this);
        binding.recyclerBill.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recyclerBill.setAdapter(adapter);

        viewModel.billsList.observe(getActivity(), billsList -> {
            for (HoaDonWithThuoc bill : billsList) {
                int total = 0;
                for (Thuoc medicine : bill.thuocList) {
                    CTBanLe ctBanLe = viewModel.getCTBanLe(medicine.maThuoc, bill.hoaDon.soHD);
                    if (ctBanLe != null) {
                        total += medicine.donGia * ctBanLe.soLuong;
                    }
                }

                bill.totalMoney = total;
            }
            adapter.setAdapter(billsList);
        });

        binding.fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), BillCreateActivity.class));
        });
    }

    public void goToBillDetail(HoaDonWithThuoc bill) {
        PharmacyObj pharmacyObj = new PharmacyObj(bill.pharmacy.maNT, bill.pharmacy.tenNT, bill.pharmacy.diaChi);

        List<MedicineObj> medicineObjList = new ArrayList<>();
        for (Thuoc medicine : bill.thuocList) {
            CTBanLe ctBanLe = viewModel.getCTBanLe(medicine.maThuoc, bill.hoaDon.soHD);

            MedicineObj obj = new MedicineObj(medicine);
            obj.soLuong = ctBanLe.soLuong;
            medicineObjList.add(obj);
        }

        BillObj billObj = new BillObj(bill.hoaDon.soHD, bill.hoaDon.ngayHD, pharmacyObj, medicineObjList, bill.totalMoney);

        Intent intent = new Intent(getActivity(), BillDetailActivity.class);
        intent.putExtra(Constants.BILL_DETAIL, billObj);
        startActivity(intent);
    }
}