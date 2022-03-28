package com.team28.qlnhathuoc.ui.bill.bill_create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team28.qlnhathuoc.adapter.BillDetailAdapter;
import com.team28.qlnhathuoc.adapter.SpinnerPharmacyAdapter;
import com.team28.qlnhathuoc.databinding.FragmentBillPreviewConfirmBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Helpers;

import java.util.stream.Collectors;


public class BillPreviewConfirmFragment extends Fragment {

    private FragmentBillPreviewConfirmBinding binding;

    private BillCreateViewModel viewModel;

    private BillDetailAdapter adapter;
    private SpinnerPharmacyAdapter spinnerAdapter;

    public BillPreviewConfirmFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillPreviewConfirmBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(BillCreateViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupSpinner();
    }

    private void setupRecyclerView() {
        adapter = new BillDetailAdapter(this);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnConfirmBill.setOnClickListener(v -> {
            if (viewModel.getTotalMoney().getValue() == 0f) {
                Toast.makeText(getContext(), "Vui lòng chọn thuốc muốn mua!", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.insertBill();
                Toast.makeText(getContext(), "Lập hóa đơn thành công", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        viewModel.getTotalMoney().observe(getActivity(), totalMoney -> {
            binding.tvTotalMoney.setText(Helpers.formatCurrency(totalMoney) + " đ");

        });

        viewModel.medicinesChoose.observe(getActivity(), medicinesChooseList -> {
            adapter.setAdapter(medicinesChooseList
                    .stream().filter(medicine -> medicine.soLuong > 0)
                    .collect(Collectors.toList()));
        });
    }

    private void setupSpinner() {
        viewModel.pharmacyList.observe(getActivity(), pharmacyList -> {
            spinnerAdapter = new SpinnerPharmacyAdapter(pharmacyList, this);
            binding.spinnerNT.setAdapter(spinnerAdapter);
        });

        binding.spinnerNT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.pharmacyChoose = spinnerAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void changeMedicineAmount(Thuoc medicine) {
        BottomSheetChangeAmountFragment fragment = new BottomSheetChangeAmountFragment(medicine, this);
        fragment.show(getActivity().getSupportFragmentManager(), "change_amount");
    }

    public void minusAmount(Thuoc medicine) {
        viewModel.minus(medicine);
        adapter.setAdapter(viewModel.medicinesChoose.getValue()
                .stream().filter(m -> m.soLuong > 0)
                .collect(Collectors.toList()));
    }

    public void plusAmount(Thuoc medicine) {
        viewModel.plus(medicine);
        adapter.setAdapter(viewModel.medicinesChoose.getValue()
                .stream().filter(m -> m.soLuong > 0)
                .collect(Collectors.toList()));
    }
}