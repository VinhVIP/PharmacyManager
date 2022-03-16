package com.team28.qlnhathuoc.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.activity.MedicineFormActivity;
import com.team28.qlnhathuoc.adapter.MedicineAdapter;
import com.team28.qlnhathuoc.databinding.FilterMedicineBinding;
import com.team28.qlnhathuoc.databinding.FragmentMedicineBinding;
import com.team28.qlnhathuoc.room.entity.CTBanLe;
import com.team28.qlnhathuoc.room.entity.HoaDon;
import com.team28.qlnhathuoc.room.entity.NhaThuoc;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.room.entity.relations.ThuocWithHoaDon;
import com.team28.qlnhathuoc.utils.Constants;
import com.team28.qlnhathuoc.utils.Helpers;
import com.team28.qlnhathuoc.viewmodel.MedicineViewModel;

import java.util.ArrayList;
import java.util.List;

public class MedicineFragment extends Fragment {

    private FragmentMedicineBinding binding;

    private MedicineViewModel viewModel;

    private MedicineAdapter adapter;

    private List<ThuocWithHoaDon> medicineList;

    public MedicineFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMedicineBinding.inflate(getLayoutInflater(), container, false);
        viewModel = new ViewModelProvider(getActivity()).get(MedicineViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerMedicine.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new MedicineAdapter(this);
        binding.recyclerMedicine.setAdapter(adapter);

        viewModel.medicineBillList.observe(getActivity(), medicineList -> {
            for(ThuocWithHoaDon medicine : medicineList){
                int total = 0;
                for(HoaDon hoaDon:medicine.hoaDonList){
                    CTBanLe ctBanLe = viewModel.getCTBanLe(medicine.thuoc.maThuoc, hoaDon.soHD);
                    if(ctBanLe != null){
                        total += ctBanLe.soLuong;
                    }
                }
                medicine.total = total;
            }

            this.medicineList = medicineList;
            filterSearch(getKeyword());
        });

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), MedicineFormActivity.class);
            startActivity(intent);
        });

        binding.btnClear.setOnClickListener(v -> binding.edSearch.setText(""));
//        binding.btnFilter.setOnClickListener(v -> showDialogFilter());

        searchMedicine();
    }

    private void searchMedicine() {
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterSearch(getKeyword());
            }
        });
    }

    private void filterSearch(String keyword) {
        if (keyword.isEmpty()) {
            adapter.setAdapter(medicineList);
            return;
        }

        List<ThuocWithHoaDon> searchList = new ArrayList<>();
        for (ThuocWithHoaDon medicine : medicineList) {
            if (medicine.thuoc.maThuoc.toLowerCase().contains(keyword) || medicine.thuoc.tenThuoc.toLowerCase().contains(keyword)) {
                searchList.add(medicine);
            }
        }

        adapter.setAdapter(searchList);
    }

    private String getKeyword() {
        return binding.edSearch.getText().toString().trim().toLowerCase();
    }

    private void showDialogFilter() {
        Dialog dialog = new Dialog(this.getContext());
        FilterMedicineBinding dialogBinding = FilterMedicineBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    public void goToEditMedicine(Thuoc medicine) {
        Intent intent = new Intent(this.getActivity(), MedicineFormActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.MATHUOC, medicine.maThuoc);
        bundle.putString(Constants.TENTHUOC, medicine.tenThuoc);
        bundle.putString(Constants.DVT, medicine.donViTinh);
        bundle.putFloat(Constants.DONGIA, medicine.donGia);

        intent.putExtra(Constants.REQUEST_ACTION, bundle);
        startActivity(intent);
    }

    public void showDialogDeleteMedicine(Thuoc medicine) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Xác nhận xóa");
//        alertDialogBuilder.setIcon(R.drawable.question);
        builder.setMessage(String.format("Bạn thực sự muốn xóa thuốc %s ?", medicine.tenThuoc));
        builder.setCancelable(true);

        builder.setPositiveButton("Đồng ý", (arg0, arg1) -> {
            if (viewModel.canDeleteMedicine(medicine)) {
                viewModel.deleteMedicine(medicine);
            } else {
                Helpers.showToast(this.getContext(), "Nhà thuốc đã có dữ liệu nên không thể xóa!");
            }
        });
        builder.setNeutralButton("Hủy", (dialog, which) -> {
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}