package com.team28.qlnhathuoc.fragment;

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
import com.team28.qlnhathuoc.adapter.MedicineBillAdapter;
import com.team28.qlnhathuoc.databinding.FragmentBillChooseMedicineBinding;
import com.team28.qlnhathuoc.room.entity.Thuoc;
import com.team28.qlnhathuoc.utils.Helpers;
import com.team28.qlnhathuoc.viewmodel.BillCreateViewModel;

public class BillChooseMedicineFragment extends Fragment {

    private FragmentBillChooseMedicineBinding binding;

    private BillCreateViewModel viewModel;

    private MedicineBillAdapter adapter;

    public BillChooseMedicineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillChooseMedicineBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(BillCreateViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupButtonPreviewBill();

    }

    private void setupButtonPreviewBill() {
        viewModel.getTotalMoney().observe(getActivity(), totalMoney -> {
            binding.btnPreviewBill.setText("Xem danh sách - " + Helpers.formatCurrency(totalMoney) + " đ");
        });

        binding.btnPreviewBill.setOnClickListener(v -> {
            viewModel.isShowPreviewBill = true;
            ((BillCreateActivity) getActivity()).previewBill();
        });
    }

    private void setupRecyclerView() {
        adapter = new MedicineBillAdapter(this);
        binding.recyclerMedicine.setAdapter(adapter);
        binding.recyclerMedicine.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.medicinesLiveData.observe(getActivity(), medicineList -> {
            adapter.setAdapter(medicineList);

            viewModel.medicinesChoose.postValue(medicineList);

            viewModel.medicinesLiveData.removeObservers(getActivity());
        });
    }

    public void minus(Thuoc medicine, int position) {
        viewModel.minus(medicine);
        adapter.notifyItemChanged(position);
    }

    public void plus(Thuoc medicine, int position) {
        viewModel.plus(medicine);
        adapter.notifyItemChanged(position);
    }
}