package com.team28.qlnhathuoc.ui.bill.bill_create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.team28.qlnhathuoc.databinding.FragmentBottomSheetChangeAmoutBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;

public class BottomSheetChangeAmountFragment extends BottomSheetDialogFragment {

    private FragmentBottomSheetChangeAmoutBinding binding;

    private BillPreviewConfirmFragment previewFragment;
    private BillCreateViewModel viewModel;

    public Thuoc medicine;

    public BottomSheetChangeAmountFragment(Thuoc medicine, BillPreviewConfirmFragment previewFragment) {
        this.medicine = medicine;
        this.previewFragment = previewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBottomSheetChangeAmoutBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(BillCreateViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvMaThuoc.setText(medicine.maThuoc);
        binding.tvTenThuoc.setText(medicine.tenThuoc);
        binding.tvNumber.setText(medicine.soLuong + "");

        binding.btnMinus.setOnClickListener(v -> {
            previewFragment.minusAmount(medicine);
            binding.tvNumber.setText(medicine.soLuong + "");
        });

        binding.btnPlus.setOnClickListener(v -> {
            previewFragment.plusAmount(medicine);
            binding.tvNumber.setText(medicine.soLuong + "");
        });
    }

}
